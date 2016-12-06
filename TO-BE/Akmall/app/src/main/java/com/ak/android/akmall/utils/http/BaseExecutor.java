package
        com.ak.android.akmall.utils.http;

import java.util.Observable;

/**
 * Created by gkduduu on 2016. 11. 02..
 */
public class BaseExecutor extends Observable {
    public static final int EXECUTE_SUCCEEDED = 1;
    public static final int EXECUTE_FAILED = 2;

    public String _id;
    protected RequestCompletionListener _completionListener = null;
    protected RequestFailureListener _failureListener = null;
    protected RequestOperationListener _operationListener = null;
    protected Object _responseData = null;

    public void notifyExecuteCompletion(){
        this.setChanged();
        this.notifyObservers();
        this.deleteObservers();
    }
}
