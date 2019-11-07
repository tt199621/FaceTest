package com.example.facetest.activity_setting;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;
import com.example.facetest.adapter.AlarmAdapter;
import com.example.facetest.bean.AlarmBean;
import com.example.facetest.receiver.AlarmReceiver;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 编辑闹钟
 */
public class EditAlarmActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView finish;
    private TextView edit_alarm_time,type;
    private EditText edit_alarm_location,edit_alarm_tips;
    private Button save_alarm_edit;
    private Robot robot=Robot.getInstance();

    private Calendar calendar;  //日期类
    private int Year;       //年
    private int month;      //月
    private int day;        //日
    private int hour;       //时
    private int minute;     //分
    private static final int ONE_DAY_TIME = 1000*60*60*24;

    private ListDataSave save;
    private List<AlarmBean> alarmBeans;
    private Boolean code=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        initView();
    }

    public void initView(){
        save=new ListDataSave(this,"alarmDB");
        alarmBeans=new ArrayList<>();
        finish=findViewById(R.id.finish);
        edit_alarm_time=findViewById(R.id.edit_alarm_time);//时间
        edit_alarm_time.setOnClickListener(this);
        edit_alarm_location=findViewById(R.id.edit_alarm_location);//地点
        edit_alarm_tips=findViewById(R.id.edit_alarm_tips);//备注
        save_alarm_edit=findViewById(R.id.save_alarm_edit);//保存
        type=findViewById(R.id.type);//类型
        type.setText(AlarmAdapter.type);
        edit_alarm_time.setText(AlarmAdapter.time);
        edit_alarm_location.setText(AlarmAdapter.location);
        edit_alarm_tips.setText(AlarmAdapter.tips);
        finish.setOnClickListener(this);
        save_alarm_edit.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.edit_alarm_time://选择时间
                //实例化日期类
                calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR);//获取当前年
                month = calendar.get(Calendar.MONTH)+1;//获取月份，加1是因为月份是从0开始计算的
                day = calendar.get(Calendar.DATE);//获取日
                hour = calendar.get(Calendar.HOUR);//获取小时
                minute = calendar.get(Calendar.MINUTE);//获取分钟
                calendar.clear();
                //实例化时间选择器
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    //实现监听方法
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        //设置文本显示内容
                        if (i<10&&i1>=10){
                            edit_alarm_time.setText(""+Year+"年"+month+"月"+day+"日   "+"0"+i+":"+i1);
                        }
                        if (i >= 10 && i1 < 10) {
                            edit_alarm_time.setText(""+Year+"年"+month+"月"+day+"日   "+i+":"+"0"+i1);
                        }
                        if (i>=10&&i1>=10){
                            edit_alarm_time.setText(""+Year+"年"+month+"月"+day+"日   "+i+":"+i1);
                        }
                        hour=i;
                        minute=i1;
                        calendar.set(Calendar.YEAR,Year);
                        calendar.set(Calendar.MONTH,month-1); //也可以填数字，0-11,一月为0
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR, hour);
                        calendar.set(Calendar.MINUTE, minute);

                    }
                },hour,minute,false).show();//记得使用show才能显示！

                //实例化日期选择器悬浮窗
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    //实现监听方法
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //设置文本显示内容，i为年，i1为月，i2为日
                        edit_alarm_time.setText(""+i+"年"+(i1+1)+"月"+i2+"日   ");
                        //以下赋值给全局变量，是为了后面的时间选择器，选择时间的时候不会获取不到日期！
                        Year=i;//年
                        month=i1+1;//月
                        day=i2;//日
                    }
                },Year,month-1,day).show();//记得使用show才能显示悬浮窗
                break;
            case R.id.save_alarm_edit:
                if (edit_alarm_time.getText().toString().equals("")){
                    robot.speak(TtsRequest.create("时间不能为空",false));
                }else {
                    Intent intent =new Intent(this, AlarmReceiver.class);
                    intent.setAction(AlarmAdapter.action);
                    PendingIntent sender= PendingIntent.getBroadcast(this, Integer.parseInt(AlarmAdapter.action), intent, 0);
                    AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                    long actualTime=0;
                    if (code == true) {
                        long mTimeInfo = calendar.getTimeInMillis();
                        actualTime = mTimeInfo > System.currentTimeMillis()
                                ? mTimeInfo : mTimeInfo + ONE_DAY_TIME;
                    }else {
                        actualTime= AlarmAdapter.startTime;
                    }
                    AlarmBean alarmBean=new AlarmBean();
                    alarmBean.setAction(AlarmAdapter.action);//action标识
                    alarmBean.setType(""+AlarmAdapter.type);//类型
                    alarmBean.setTime(""+edit_alarm_time.getText().toString());//时间
                    alarmBean.setLocation(""+edit_alarm_location.getText().toString());//地点
                    alarmBean.setTips(""+edit_alarm_tips.getText().toString());//备注
                    alarmBeans.clear();
                    alarmBeans=save.getAlarm("alarm");
                    //移除之前的闹钟
                    for (int i = 0; i < alarmBeans.size(); i++) {
                        if (alarmBean.getAction().equals(alarmBeans.get(i).getAction())){
                            alarmBeans.remove(i);
                        }
                    }
                    alarm.set(AlarmManager.RTC_WAKEUP, actualTime, sender);//设置闹钟
                    Log.d("canlendar",""+actualTime+"----action: "+intent.getAction());
                    alarmBeans.add(alarmBean);
                    save.setAlarm("alarm",alarmBeans);//存入数据库
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

}
