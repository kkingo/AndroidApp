package com.example.controlpcpro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.controlpcpro.R;
import com.example.controlpcpro.adapter.Find_tab_Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Page02 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page02, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_FindFragment_title);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_FindFragment_pager);

        FragmentPagerAdapter fAdapter = null;

        List<Fragment> list_fragment;                                //定义要装fragment的列表
        List<String> list_title;

        Frag01 frag01 = new Frag01();
        Frag02 frag02 = new Frag02();

        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(frag01);
        list_fragment.add(frag02);

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add("当前WIFI");
        list_title.add("搜集信息");

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));

        fAdapter = new Find_tab_Adapter(getChildFragmentManager(),list_fragment,list_title);

        //viewpager加载adapter
        viewPager.setAdapter(fAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewPager);
        //tab_FindFragment_title.set
    }

}
