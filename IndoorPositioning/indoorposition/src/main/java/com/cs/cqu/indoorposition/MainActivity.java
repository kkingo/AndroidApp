package com.cs.cqu.indoorposition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.cqu.indoorposition.mapview.FloorPlanView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import entity.AccessPiont;
import okhttp3.OkHttpClient;
import okhttps.DisposeDataListener;
import okhttps.OkHttpClients;

public class MainActivity extends AppCompatActivity {
    final  private  String TAG = "MAIN";
    private FloorPlanView floorMap;
    private Timer mtimer;
    private TimerTask mTimerTask;
    private OkHttpClient okHttpClient;
    private String url = "http://172.28.32.180:3000/api/getfingerprints";
    private List<AccessPiont> apList ;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.id_map_layout);
        final TextView textview = (TextView)findViewById(R.id.tv_display);
        floorMap = new FloorPlanView(this);
        floorMap.invalidate();
        linearLayout.addView(floorMap);
        OkHttpClients client = new OkHttpClients();
        Log.d("response","send start");
        client.sendGetRequest(url, null, new DisposeDataListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("response","on Main Thread "+response);
                apList = gson.fromJson(response , new TypeToken<List<AccessPiont>>(){}.getType());
            }

            @Override
            public void onFailure(String error) {
                Log.d("response","on Main Thread "+error);
            }
        });
    }

}
