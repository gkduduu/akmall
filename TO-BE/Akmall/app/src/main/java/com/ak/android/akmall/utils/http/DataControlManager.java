package com.ak.android.akmall.utils.http;

import android.os.Handler;
import android.os.Message;

import com.arasthel.asyncjob.AsyncJob;

import java.util.ArrayDeque;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * create by gkduduu 2016.11.02
 *
 * 앱 내에 발생하는 통신 및 순차적으로 실행되는 스케쥴링 처리를 담당합니다.
 *
 * runScheduledCommand
 * runScheduledCommandOnAsync
 *
 * 위의 명령어를 통하여 동기/비동기 방식을 선택하여 처리할 수 있습니다.
 *
 *
 */
public class DataControlManager implements Executor, Observer {
    private static final DataControlManager _sharedInstance = new DataControlManager();
    public static DataControlManager getInstance() {
        return _sharedInstance;
    }
    private final Queue _tasks = new ArrayDeque(0);
    private boolean _isPaused = false;
    private Runnable _active;

    private final Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseExecutor.EXECUTE_SUCCEEDED:
                    BaseExecutor executor = (BaseExecutor)msg.obj;
                    if (executor._completionListener != null) {
                        try {
                            executor._completionListener.onDataControlCompleted(executor._responseData);
                        } catch (Exception e) {
                            _active = null;
                            e.printStackTrace();
                            if (executor._failureListener != null) {
                                executor._failureListener.onDataControlFailed(executor._responseData, e);
                            }
                        }
                    }
                    executor.notifyExecuteCompletion();
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    public void execute(final Runnable command) {
        this._tasks.add(command);

        if (_active == null) {
            runScheduledCommand();
        }
    }

    private void runScheduledCommand() {
        if ((_active = (Runnable)_tasks.poll()) == null) {
            return;
        }

        new Thread(_active).start();

        if (_active instanceof BaseExecutor) {
            ((BaseExecutor)_active).addObserver(this);
            _handler.sendMessage(_handler.obtainMessage(BaseExecutor.EXECUTE_SUCCEEDED, _active));
        }
    }

    public void addSchedule(final Runnable command) {
        if (command == null) {
            return;
        }

        this._tasks.add(command);
    }

    synchronized public void runScheduledCommandOnAsync() {
        if (((_active = (Runnable)_tasks.poll()) == null) ||
                _isPaused) {
            return;
        }

        if (_active instanceof UIThreadExecutor) {
            executeExecutor((UIThreadExecutor) _active);
        }
        else {
            executeExecutor((DataControlHttpExecutor) _active);
        }
    }

    private void executeExecutor(final UIThreadExecutor executor) {
        try {
            executor.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_tasks.size() > 0) {
            runScheduledCommandOnAsync();
        }
    }

    private void executeExecutor(final DataControlHttpExecutor executor) {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                executor.run();

                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        try {
                            if (executor instanceof BaseExecutor && executor._completionListener != null && executor._responseData != null) {
                                executor._completionListener.onDataControlCompleted(executor._responseData);
                            }

                            if (_tasks.size() > 0) {
                                runScheduledCommandOnAsync();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (executor._failureListener != null) {
                                executor._failureListener.onDataControlFailed(null, null);
                            }
                            _tasks.clear();
                        }
                    }
                });
            }
        });
    }

    synchronized public void runScheduledCommandOnSync() {
        if ((_active = (Runnable)_tasks.poll()) == null) {
            return;
        }

        _active.run();

        try {
            if (((BaseExecutor)_active)._completionListener != null) {
                ((BaseExecutor) _active)._completionListener.onDataControlCompleted(((BaseExecutor) _active)._responseData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (((BaseExecutor) _active)._failureListener != null) {
                ((BaseExecutor) _active)._failureListener.onDataControlFailed(null, null);
            }

            _tasks.clear();
        }
    }

    public void pauseSchedule() {
        _isPaused = true;
    }

    public void resumeSchedule() {
        _isPaused = false;
        runScheduledCommandOnAsync();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (_tasks.size() > 0) {
            runScheduledCommand();
        }
    }

    public void removeAllSchedule() {
        _tasks.removeAll(_tasks);
//        if (AppManager.isDebug()) {
//            return;
//        }
//        else {
//            _tasks.removeAll(_tasks);
//        }
    }
}