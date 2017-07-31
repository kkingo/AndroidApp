package okhttps;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by King on 2017/7/30.
 */

public class JsonCallBack implements Callback {

    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;
    private static String CONNECT_ERROR = "Network connection has problem";
    private static String EMPTY_ERROR = "Server responses nothing";
    public JsonCallBack(DisposeDataListener listener) {
        this.mDeliveryHandler = new Handler(Looper.myLooper());
        this.mListener = listener;

    }

    @Override
    public void onFailure(Call call, IOException e) {
            Log.d("response","failed");
            mDeliveryHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onFailure(CONNECT_ERROR);
                }
            });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        Log.d("response",result);
        if(result != null) {
            mDeliveryHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onSuccess(result);
                }
            });
        }else {
            mListener.onFailure(EMPTY_ERROR);
        }
    }
}
