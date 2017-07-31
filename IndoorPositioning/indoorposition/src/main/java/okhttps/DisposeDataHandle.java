package okhttps;

/**
 * Created by King on 2017/7/30.
 */

public class DisposeDataHandle {
    public  DisposeDataListener mListener = null;
    public Class<?> mClass = null;

    public DisposeDataHandle(DisposeDataListener mListener) {
        this.mListener = mListener;
    }
    public DisposeDataHandle(DisposeDataListener mListener, Class<?> mClass) {
        this.mListener = mListener;
        this.mClass = mClass;
    }
}
