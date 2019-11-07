package com.example.facetest.adapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.facetest.R;
import com.example.facetest.activity_setting.EditAlarmActivity;
import com.example.facetest.bean.AlarmBean;
import com.example.facetest.receiver.AlarmReceiver;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    Context context;
    List<AlarmBean> list;
    Robot robot=Robot.getInstance();
    public static String type,time,location,tips,action;
    public static long startTime=0;
    private ListDataSave save;


    public AlarmAdapter(Context context,List<AlarmBean> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        save=new ListDataSave(context,"alarmDB");
        //闹钟类型图片
        if (list.get(i).getType().equals("工作")){
            Glide.with(context).load(R.drawable.work).placeholder(R.drawable.screensaver1).into(holder.item_alarm_type);
        }
        if (list.get(i).getType().equals("学习")){
            Glide.with(context).load(R.drawable.study).placeholder(R.drawable.screensaver1).into(holder.item_alarm_type);
        }
        if (list.get(i).getType().equals("私事")){
            Glide.with(context).load(R.drawable.privates).placeholder(R.drawable.screensaver1).into(holder.item_alarm_type);
        }
        if (list.get(i).getType().equals("其他")){
            Glide.with(context).load(R.drawable.other).placeholder(R.drawable.screensaver1).into(holder.item_alarm_type);
        }
        holder.item_alarm_time.setText("时间："+list.get(i).getTime());//时间
        holder.item_alarm_location.setText("地点："+list.get(i).getLocation());//地点
        holder.item_alarm_tips.setText("备注："+list.get(i).getTips());//备注
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转编辑页
                type=list.get(i).getType();
                time=list.get(i).getTime();
                location=list.get(i).getLocation();
                tips=list.get(i).getTips();
                action=list.get(i).getAction();
                startTime=list.get(i).getStartTime();
                context.startActivity(new Intent(context, EditAlarmActivity.class));
            }
        });
        holder.item_alarm_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.waring);
                builder.setTitle("删除此条日程安排？");
                builder.setMessage(" ");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int x) {
                        Intent intent =new Intent(context, AlarmReceiver.class);
                        intent.setAction(list.get(i).getAction());
                        PendingIntent sender= PendingIntent.getBroadcast(context, Integer.parseInt(list.get(i).getAction()), intent, 0);
                        AlarmManager alarm= (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        alarm.cancel(sender);//取消闹钟
                        list.remove(i);
                        save.setAlarm("alarm",list);//删除数据库数据
                        Toast.makeText(context, "删除成功"+i, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View thisView;
        TextView item_alarm_time,item_alarm_location,item_alarm_tips;//时间、地点、备注
        ImageView item_alarm_type,item_alarm_remove;//工作类型、删除按钮
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thisView=itemView;
            item_alarm_time=itemView.findViewById(R.id.item_alarm_time);
            item_alarm_location=itemView.findViewById(R.id.item_alarm_location);
            item_alarm_tips=itemView.findViewById(R.id.item_alarm_tips);
            item_alarm_type=itemView.findViewById(R.id.item_alarm_type);
            item_alarm_remove=itemView.findViewById(R.id.item_alarm_remove);
        }
    }
}

