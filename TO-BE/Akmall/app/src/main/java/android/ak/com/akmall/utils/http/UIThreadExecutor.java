package android.ak.com.akmall.utils.http;

/**
 * Created by gkduduu on 2016. 11. 02..
 */
public class UIThreadExecutor extends BaseExecutor implements Runnable {
    @Override
    //@UiThread
    public void run() {
        this._operationListener.onRequestOperation(null);
    }

    public UIThreadExecutor setOperationListener(RequestOperationListener operationListener) {
        _operationListener = operationListener;
        return this;
    }
}
