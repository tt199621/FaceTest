package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;

public class SettingAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView finish,setting_alarm_add;
    private RecyclerView recycler_setting_alarm;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        initView();
    }

    public void initView(){
        finish=findViewById(R.id.finish);
        setting_alarm_add=findViewById(R.id.setting_alarm_add);
        recycler_setting_alarm=findViewById(R.id.recycler_setting_alarm);
        linearLayoutManager=new LinearLayoutManager(this);
        finish.setOnClickListener(this);
        setting_alarm_add.setOnClickListener(this);
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
