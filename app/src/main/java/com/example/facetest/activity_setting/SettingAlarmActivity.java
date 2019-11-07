package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;
import com.example.facetest.adapter.AlarmAdapter;
import com.example.facetest.bean.AlarmBean;
import com.example.facetest.util.ListDataSave;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置闹钟列表页
 */
public class SettingAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView finish,setting_alarm_add;
    private RecyclerView recycler_setting_alarm;
    private LinearLayoutManager linearLayoutManager;
    private ListDataSave save;
    private List<AlarmBean> alarmBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        initView();
        initData();
    }

    public void initView(){
        save=new ListDataSave(this,"alarmDB");
        alarmBeans=new ArrayList<>();
        finish=findViewById(R.id.finish);
        setting_alarm_add=findViewById(R.id.setting_alarm_add);
        recycler_setting_alarm=findViewById(R.id.recycler_setting_alarm);
        linearLayoutManager=new LinearLayoutManager(this);
        finish.setOnClickListener(this);
        setting_alarm_add.setOnClickListener(this);
    }

    public void initData(){
        alarmBeans.clear();
        alarmBeans=save.getAlarm("alarm");
        AlarmAdapter adapter=new AlarmAdapter(this,alarmBeans);
        recycler_setting_alarm.setAdapter(adapter);
        recycler_setting_alarm.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmBeans.clear();
        alarmBeans=save.getAlarm("alarm");
        Toast.makeText(this, ""+alarmBeans.size(), Toast.LENGTH_SHORT).show();
        AlarmAdapter adapter=new AlarmAdapter(this,alarmBeans);
        recycler_setting_alarm.setAdapter(adapter);
        recycler_setting_alarm.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.setting_alarm_add:
                startActivity(new Intent(this,AddAlarmActivity.class));
                break;
        }
    }
}
