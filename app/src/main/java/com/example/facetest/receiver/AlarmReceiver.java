package com.example.facetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.facetest.activity_setting.ShowAlarmActivity;
import com.example.facetest.bean.AlarmBean;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;

import java.util.ArrayList;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    Robot robot=Robot.getInstance();
    private ListDataSave save;
    private List<AlarmBean> alarmBeans;
    public static String type,time,locations,tips,action;
    public static long startTime;

    @Override
    public void onReceive(final Context context, Intent intent) {
        save=new ListDataSave(context,"alarmDB");
        alarmBeans=new ArrayList<>();
        alarmBeans=save.getAlarm("alarm");
        Log.d("delayTime","action="+intent.getAction());
        for (int i = 0; i < alarmBeans.size(); i++) {
            if (intent.getAction().equals(alarmBeans.get(i).getAction())){
                type=alarmBeans.get(i).getType();
                time=alarmBeans.get(i).getTime();
                locations=alarmBeans.get(i).getLocation();
                tips=alarmBeans.get(i).getTips();
                action=alarmBeans.get(i).getAction();
                startTime=alarmBeans.get(i).getStartTime();
            }
        }
        Intent intent1 = new Intent();
        intent1.setClass(context, ShowAlarmActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent1);
    }


}
