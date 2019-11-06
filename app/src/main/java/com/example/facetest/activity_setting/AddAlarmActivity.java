package com.example.facetest.activity_setting;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;
import com.example.facetest.receiver.AlarmReceiver;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ImageView finish;
    private TextView add_alarm_time;
    private Spinner add_alarm_spinner;
    private EditText add_alarm_location,add_alarm_tips;
    private Button save_alarm;
    private String[] types={"工作","学习","私事","其他"};
    private String type="工作";
    private Robot robot=Robot.getInstance();

    private Calendar calendar;  //日期类
    private int Year;       //年
    private int month;      //月
    private int day;        //日
    private int hour;       //时
    private int minute;     //分
    private static final int ONE_DAY_TIME = 1000*60*60*24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        initView();
    }

    public void initView(){
        finish=findViewById(R.id.finish);
        add_alarm_spinner=findViewById(R.id.add_alarm_spinner);//类型选择
        add_alarm_time=findViewById(R.id.add_alarm_time);//时间
        add_alarm_time.setOnClickListener(this);
        add_alarm_location=findViewById(R.id.add_alarm_location);//地点
        add_alarm_tips=findViewById(R.id.add_alarm_tips);//备注
        save_alarm=findViewById(R.id.save_alarm);//保存
        finish.setOnClickListener(this);
        add_alarm_spinner.setOnItemSelectedListener(this);
        save_alarm.setOnClickListener(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,types);
        add_alarm_spinner.setAdapter(adapter);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.add_alarm_time://选择时间
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
                            add_alarm_time.setText(""+Year+"年"+month+"月"+day+"日   "+"0"+i+":"+i1);
                        }
                        if (i >= 10 && i1 < 10) {
                            add_alarm_time.setText(""+Year+"年"+month+"月"+day+"日   "+i+":"+"0"+i1);
                        }
                        if (i>=10&&i1>=10){
                            add_alarm_time.setText(""+Year+"年"+month+"月"+day+"日   "+i+":"+i1);
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
                        add_alarm_time.setText(""+i+"年"+(i1+1)+"月"+i2+"日   ");
                        //以下赋值给全局变量，是为了后面的时间选择器，选择时间的时候不会获取不到日期！
                        Year=i;//年
                        month=i1+1;//月
                        day=i2;//日
                    }
                },Year,month-1,day).show();//记得使用show才能显示悬浮窗
                break;
            case R.id.save_alarm:
                if (add_alarm_time.getText().toString().equals("")){
                    robot.speak(TtsRequest.create("时间不能为空",false));
                }else {
                    Intent intent =new Intent(this, AlarmReceiver.class);
                    intent.setAction("1");
                    PendingIntent sender= PendingIntent.getBroadcast(this, 0, intent, 0);
                    AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                    long mTimeInfo = calendar.getTimeInMillis();
                    long actualTime = mTimeInfo > System.currentTimeMillis()
                            ? mTimeInfo : mTimeInfo + ONE_DAY_TIME;
                    alarm.set(AlarmManager.RTC_WAKEUP, actualTime, sender);
                    Log.d("canlendar",""+actualTime);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.add_alarm_spinner:
                type=adapterView.getItemAtPosition(i).toString();//类型
                Log.d("Alarmtype",""+type);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
