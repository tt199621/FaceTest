package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facetest.R;
import com.example.facetest.activity.MainActivity;
import com.example.facetest.util.BaseDispatchTouchActivity;

/**
 * 设置页面
 */
public class SettingActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish_setting,home_setting;
    private TextView setting_welcom_img,setting_welcom_words,setting_activities,setting_exhibition_introduce,setting_answer,setting_data_plan,setting_password,setting_default_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    public void  initView(){
        finish_setting=findViewById(R.id.finish_setting);
        home_setting=findViewById(R.id.home_setting);
        finish_setting.setOnClickListener(this);
        home_setting.setOnClickListener(this);
        setting_welcom_words=findViewById(R.id.setting_welcom_words);//迎宾词
        setting_activities=findViewById(R.id.setting_activities);//活动介绍
        setting_exhibition_introduce=findViewById(R.id.setting_exhibition_introduce);//展厅介绍
        setting_answer=findViewById(R.id.setting_answer);//业务问答
        setting_data_plan=findViewById(R.id.setting_data_plan);//日程安排
        setting_password=findViewById(R.id.setting_password);//口令
        setting_default_mode=findViewById(R.id.setting_default_mode);//默认模式
        setting_default_mode.setOnClickListener(this);
        setting_welcom_img=findViewById(R.id.setting_welcom_img);//设置轮播图
        setting_welcom_img.setOnClickListener(this);
        setting_welcom_words.setOnClickListener(this);
        setting_activities.setOnClickListener(this);
        setting_exhibition_introduce.setOnClickListener(this);
        setting_answer.setOnClickListener(this);
        setting_data_plan.setOnClickListener(this);
        setting_password.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_welcom_img://设置轮播图
                startActivity(new Intent(this,SettingBannerActivity.class));
                break;
            case R.id.setting_welcom_words://设置迎宾词
                startActivity(new Intent(this,SettingWelcomWordsActivity.class));
                break;
            case R.id.setting_activities://设置活动介绍
                startActivity(new Intent(this,SettingActivitiesActivity.class));
                break;
            case R.id.setting_exhibition_introduce://设置展厅介绍
                startActivity(new Intent(this,SettingExhibitonActivity.class));
                break;
            case R.id.setting_answer://设置业务问答
                startActivity(new Intent(this,SettingSpeakActivity.class));
                break;
            case R.id.setting_data_plan://设置日程安排
                startActivity(new Intent(this,SettingAlarmActivity.class));
                break;
            case R.id.setting_password://设置口令
                startActivity(new Intent(this,ModifyPassword.class));
                break;
            case R.id.setting_default_mode://默认模式
                startActivity(new Intent(this,SettingDefaultModeActivity.class));
                break;
            case R.id.finish_setting:
                finish();
                break;
            case R.id.home_setting:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
