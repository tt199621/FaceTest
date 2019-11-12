package com.example.facetest.activity_setting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facetest.R;
import com.example.facetest.adapter.AlarmAdapter;
import com.example.facetest.bean.AlarmBean;
import com.example.facetest.receiver.AlarmReceiver;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 查看日程安排
 */
public class LookAlarmActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish;
    private TextView typestr,timestr,addresstr,remarksstr;
    private Button snoozebtn,work_iknow;
    private ListDataSave save;
    private List<AlarmBean> alarmBeans;
    private Calendar calendar;  //日期类
    private int Year;       //年
    private int month;      //月
    private int day;        //日
    private int hour;       //时
    private int minute;     //分
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_alarm);
        initView();
        initData();
    }

    public void initView(){
        save=new ListDataSave(this,"alarmDB");
        alarmBeans=new ArrayList<>();
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        typestr=findViewById(R.id.typestr);
        timestr=findViewById(R.id.timestr);
        addresstr=findViewById(R.id.addresstr);
        remarksstr=findViewById(R.id.remarksstr);
        snoozebtn=findViewById(R.id.snoozebtn);//五分钟后提醒
        snoozebtn.setOnClickListener(this);
        work_iknow=findViewById(R.id.work_iknow);//我知道了
        work_iknow.setOnClickListener(this);
    }

    public void initData(){
        typestr.setText("类型："+AlarmReceiver.type);
        timestr.setText("时间："+AlarmReceiver.time);
        addresstr.setText("地址："+AlarmReceiver.locations);
        remarksstr.setText("备注："+AlarmReceiver.tips);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.snoozebtn://五分钟后提醒
                //实例化日期类
                calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR);//获取当前年
                month = calendar.get(Calendar.MONTH)+1;//获取月份，加1是因为月份是从0开始计算的
                day = calendar.get(Calendar.DATE);//获取日
                hour = calendar.get(Calendar.HOUR_OF_DAY);//获取小时
                minute = calendar.get(Calendar.MINUTE);//获取分钟
                calendar.set(Calendar.YEAR,Year);
                calendar.set(Calendar.MONTH,month-1); //也可以填数字，0-11,一月为0
                calendar.set(Calendar.DATE, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                Intent intent =new Intent(this, AlarmReceiver.class);
                intent.setAction(AlarmReceiver.action);
                PendingIntent sender= PendingIntent.getBroadcast(this, Integer.parseInt(AlarmReceiver.action), intent, 0);
                AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                long mTimeInfo = calendar.getTimeInMillis();
                Log.d("delayTime",""+(mTimeInfo+5*60*1000)+" action:"+AlarmReceiver.action);
                alarm.set(AlarmManager.RTC_WAKEUP, mTimeInfo+5*60*1000, sender);//设置闹钟
                AlarmBean alarmBean=new AlarmBean();
                alarmBean.setAction(AlarmReceiver.action);//action标识
                alarmBean.setType(""+AlarmReceiver.type);//类型
                if (hour<10&&(minute+5)>=10){
                    if ((minute+5)<60){
                        alarmBean.setTime(""+""+Year+"年"+month+"月"+day+"日   "+"0"+hour+":"+(minute+5));//时间
                    }else {
                        alarmBean.setTime(""+""+Year+"年"+month+"月"+day+"日   "+"0"+(hour+1)+":"+"0"+(minute+5-60));//时间
                    }
                }
                if (hour >= 10 && (minute+5)< 10) {
                    alarmBean.setTime(""+""+Year+"年"+month+"月"+day+"日   "+hour+":"+"0"+(minute+5));//时间
                }
                if (hour>=10&&(minute+5)>=10){
                    if ((minute+5)<60){
                        alarmBean.setTime(""+""+Year+"年"+month+"月"+day+"日   "+hour+":"+(minute+5));//时间
                    }else {
                        alarmBean.setTime(""+""+Year+"年"+month+"月"+day+"日   "+(hour+1)+":"+"0"+(minute+5-60));//时间
                    }
                }
                alarmBean.setLocation(""+AlarmReceiver.locations);//地点
                alarmBean.setTips(""+AlarmReceiver.tips);//备注
                alarmBean.setStartTime(AlarmAdapter.startTime+5*60*1000);//响铃时间
                alarmBeans.clear();
                alarmBeans=save.getAlarm("alarm");
                //移除之前的闹钟
                for (int i = 0; i < alarmBeans.size(); i++) {
                    if (alarmBean.getAction().equals(alarmBeans.get(i).getAction())){
                        alarmBeans.remove(i);
                    }
                }
                alarmBeans.add(alarmBean);
                save.setAlarm("alarm",alarmBeans);//存入数据库
                finish();
                break;
            case R.id.work_iknow://我知道了
                finish();
                break;
        }
    }
}
