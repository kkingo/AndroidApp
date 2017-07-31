package com.example.controlpcpro.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlpcpro.R;
import com.example.controlpcpro.bean.Info;
import com.example.controlpcpro.fragment.Frag02;
import com.example.controlpcpro.ui.MyMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.controlpcpro.fragment.Frag01.saveData;
import static com.example.controlpcpro.fragment.Frag01.showDatas;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder>{

    private int tmp = 10;
    private Activity mActivity;
    private Timer timer;
    
    private List<Info> msgs = new ArrayList<>();

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView order, msg, tv_souji;
        public MyViewHolder(View view){
            super(view);
            order = (TextView) view.findViewById(R.id.id_order);
            msg = (TextView) view.findViewById(R.id.id_tv);
            tv_souji = (TextView) view.findViewById(R.id.id_tv_souji);
        }
    }

    public RVAdapter(List<Info> msgs, Activity mActivity) {
        this.msgs = msgs;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item,parent,false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        holder.msg.setText(msgs.get(position).getStatus());
        holder.order.setText(msgs.get(position).getOrder());
        if (msgs.get(position).getEnumStatus() == Frag02.Status.NOCELLECTED) {
            holder.tv_souji.setText("搜集数据");
            holder.msg.setText("未搜集数据");
        } else if (msgs.get(position).getEnumStatus() == Frag02.Status.COLLECTED){
            holder.tv_souji.setText("查看数据");
            holder.tv_souji.setBackgroundColor(Color.RED);
            holder.msg.setText("已搜集数据");
            holder.msg.setTextColor(Color.BLACK);
        }

        holder.tv_souji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (msgs.get(position).getEnumStatus() == Frag02.Status.COLLECTED) {
                    //如果已经搜集数据，那么就查看
                    showDatas();
                } else {
                    //弹出Dialog框
                    final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                    alertDialog.setCancelable(true);

                    final LayoutInflater inflater = mActivity.getLayoutInflater();
                    final View v = inflater.inflate(R.layout.dialog_main_info, null);

                    final LinearLayout ll_loading, ll_console;

                    ll_console = (LinearLayout) v.findViewById(R.id.id_console);
                    ll_loading = (LinearLayout) v.findViewById(R.id.id_loading);

                    final Button btn_cancel2 = (Button) v.findViewById(R.id.id_btn_cancel2);

                    final TextView tv_time = (TextView) v.findViewById(R.id.id_tv_time);
                    final TextView tv_status = (TextView) v.findViewById(R.id.id_tv_status);

                    v.findViewById(R.id.id_btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //timer.cancel();
                            alertDialog.dismiss();
                        }
                    });

                    v.findViewById(R.id.id_btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //saveData();
                            tmp = 10;
                            ll_loading.setVisibility(View.VISIBLE);
                            ll_console.setVisibility(View.GONE);

                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String[] strings = getPos(position);
                                            saveData(strings[0],strings[1]);
                                            tv_time.setText("" + tmp + "S");
                                            tmp--;
                                            if (tmp == -1) {
                                                timer.cancel();
                                                Toast.makeText(mActivity, "数据搜集完毕", Toast.LENGTH_SHORT).show();
                                                tv_status.setText("数据已经搜集完毕");
                                                tv_status.setTextColor(Color.parseColor("#FF0000"));
                                                btn_cancel2.setText("确定");
                                                //---------------
                                                msgs.get(position).setEnumStatus(Frag02.Status.COLLECTED);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            }, 0, 1000);
                            //alertDialog.dismiss();

                        }
                    });

                    btn_cancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setView(v);
                    alertDialog.show();
                }
            }
        });
    }

    private String[] getPos(int pos){
        String[] strings = new String[2];
        float x = 0.9f;
        float y = (pos + 1)*0.5f;

        strings[0] = x+"";
        strings[1] = y+"";

        return strings;
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

}