package com.cs.cqu.indoorpositioning;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private int[] res = {R.id.imageView_a,R.id.imageView_b,R.id.imageView_c,R.id.imageView_d,
                         R.id.imageView_e,R.id.imageView_f,R.id.imageView_g,R.id.imageView_h};
    private List<ImageView> imageViewlist = new ArrayList<ImageView>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i = 0; i < res.length; i++){
            ImageView imageView =(ImageView)findViewById(res[i]);
            imageView.setOnClickListener(this);
            imageViewlist.add(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_a:
                startAnimation();
                break;
            default:
                break;
        }
    }
    public void startAnimation(){
        for(int i =0 ; i<res.length ; i++){
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageViewlist.get(i),"translationY",0F,i*200);
            animator.setDuration(600);
            animator.start();
        }
    }
}
