package com.example.controlpcpro.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlpcpro.adapter.Find_tab_Adapter;
import com.example.controlpcpro.R;
import com.example.controlpcpro.adapter.FragMianPagerAdapter;
import com.example.controlpcpro.fragment.Frag01;
import com.example.controlpcpro.fragment.Frag02;
import com.example.controlpcpro.fragment.Page01;
import com.example.controlpcpro.fragment.Page02;
import com.example.controlpcpro.fragment.Page03;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public class MyMainActivity extends AppCompatActivity {

    private TextView tv_map,tv_souji,tv_upload;
    private ImageView iv_map,iv_souji,iv_upload;
    private LinearLayout ll_map,ll_souji, ll_upload;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;

    private final int LOCATION_REQUEST_CODE = 1;

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setMessage("位置权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请位置权限
                                ActivityCompat.requestPermissions(MyMainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                //申请位置权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        } else {
            //tvPermissionStatus.setTextColor(Color.GREEN);
            //tvPermissionStatus.setText("相机权限已申请");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //tvPermissionStatus.setTextColor(Color.GREEN);
                //tvPermissionStatus.setText("相机权限已申请");
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "权限已被禁止，请手动打开", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //private PagerAdapter mPagerAdapter;
    private List<View> mList = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity_main);
        requestPermission();
        init();
        initDatas();
    }

    private void initDatas() {
        mInflater = getLayoutInflater();
        Page01 page01 = new Page01();
        Page02 page02 = new Page02();
        Page03 page03 = new Page03();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(page01);
        fragments.add(page02);
        fragments.add(page03);

        mViewPager.setAdapter(
                new FragMianPagerAdapter(getSupportFragmentManager(),fragments));
    }

    private void init() {
        mViewPager = (ViewPager)findViewById(R.id.id_vp);
        ll_map = (LinearLayout)findViewById(R.id.id_ll_map);
        ll_souji = (LinearLayout)findViewById(R.id.id_ll_souji);
        ll_upload = (LinearLayout)findViewById(R.id.id_ll_upload);

        ll_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                resetImgs();
                iv_map.setImageResource(R.drawable.ic_map_selected);
                tv_map.setTextColor(Color.parseColor("#1597DC"));
            }
        });

        ll_souji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                resetImgs();
                iv_souji.setImageResource(R.drawable.ic_souji_selected);
                tv_souji.setTextColor(Color.parseColor("#1597DC"));
            }
        });

        ll_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
                resetImgs();
                iv_upload.setImageResource(R.drawable.ic_upload_selected);
                tv_upload.setTextColor(Color.parseColor("#1597DC"));
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.page01,null);
        View view2 = inflater.inflate(R.layout.page02,null);
        View view3 = inflater.inflate(R.layout.page03,null);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        resetImgs();
                        iv_map.setImageResource(R.drawable.ic_map_selected);
                        tv_map.setTextColor(Color.parseColor("#1597DC"));
                        break;
                    case 1:
                        resetImgs();
                        iv_souji.setImageResource(R.drawable.ic_souji_selected);
                        tv_souji.setTextColor(Color.parseColor("#1597DC"));
                        break;

                    case 2:
                        resetImgs();
                        iv_upload.setImageResource(R.drawable.ic_upload_selected);
                        tv_upload.setTextColor(Color.parseColor("#1597DC"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_map = (TextView)findViewById(R.id.id_tv_map);
        tv_souji = (TextView)findViewById(R.id.id_tv_souji);
        tv_upload = (TextView)findViewById(R.id.id_tv_upload);
        iv_map = (ImageView)findViewById(R.id.ic_map);
        iv_souji = (ImageView)findViewById(R.id.ic_souji);
        iv_upload = (ImageView)findViewById(R.id.ic_upload);

    }

    private void resetImgs() {
        iv_map.setImageResource(R.drawable.ic_map_noselected);
        iv_souji.setImageResource(R.drawable.ic_souji_noselected);
        iv_upload.setImageResource(R.drawable.ic_upload_noselected);

        tv_map.setTextColor(Color.parseColor("#bfbfbf"));
        tv_souji.setTextColor(Color.parseColor("#bfbfbf"));
        tv_upload.setTextColor(Color.parseColor("#bfbfbf"));
    }

}
