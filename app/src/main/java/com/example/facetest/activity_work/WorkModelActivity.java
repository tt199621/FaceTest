package com.example.facetest.activity_work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.facetest.R;
import com.example.facetest.activity.ContactsActivity;
import com.example.facetest.activity.MainActivity;
import com.example.facetest.activity.PassWordActivity;
import com.example.facetest.activity_exhibition.ExhibitionModeActivity;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.PackageName;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.Random;

public class WorkModelActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView returnhome_work,switch_btn_work,conf_btn_work,data_plan,video_meeting,contacts_work,weatherbtn_work,news_work,chat_work,music_qq_work;
    private Robot robot;
    String[] randomPlay={"我已经进入工作模式啦，要来看看您的日程安排吗",
            "进入工作状态，我要当个小秘书啦",
            "进入工作状态，我要当个小助理啦"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_model);
        initView();
        randomPlay();
    }

    //进入时随机播放
    public void randomPlay(){
        robot.speak(TtsRequest.create(randomPlay[new Random().nextInt(randomPlay.length)],false));
    }

    public void initView(){
        robot=Robot.getInstance();
        returnhome_work=findViewById(R.id.returnhome_work);
        returnhome_work.setOnClickListener(this);
        switch_btn_work=findViewById(R.id.switch_btn_work);
        switch_btn_work.setOnClickListener(this);
        conf_btn_work=findViewById(R.id.conf_btn_work);
        conf_btn_work.setOnClickListener(this);
        data_plan=findViewById(R.id.data_plan);
        data_plan.setOnClickListener(this);
        video_meeting=findViewById(R.id.video_meeting);
        video_meeting.setOnClickListener(this);
        contacts_work=findViewById(R.id.contacts_work);
        contacts_work.setOnClickListener(this);
        weatherbtn_work=findViewById(R.id.weatherbtn_work);
        weatherbtn_work.setOnClickListener(this);
        news_work=findViewById(R.id.news_work);
        news_work.setOnClickListener(this);
        chat_work=findViewById(R.id.chat_work);
        chat_work.setOnClickListener(this);
        music_qq_work=findViewById(R.id.music_qq_work);
        music_qq_work.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //打开外部APP
    private void openOtherApp(String packagename) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packagename);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.returnhome_work://回首页
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.switch_btn_work://切换模式
                startActivity(new Intent(this, ExhibitionModeActivity.class));
                finish();
                break;
            case R.id.conf_btn_work://设置
                startActivity(new Intent(this, PassWordActivity.class));
                break;
            case R.id.data_plan://日程安排
               /* startActivity(new Intent(this, SettingActivity.class));
                finish();*/
                break;
            case R.id.video_meeting://视频会议
                startActivity(new Intent(this, ContactsActivity.class));
                break;
            case R.id.contacts_work://联系人
                startActivity(new Intent(this, ContactsActivity.class));
                break;
            case R.id.weatherbtn_work://天气
                openOtherApp(PackageName.weather);
                break;
            case R.id.news_work://新闻
                openOtherApp(PackageName.news);
                break;
            case R.id.chat_work://闲聊
                openOtherApp(PackageName.chat);
                break;
            case R.id.music_qq_work://音乐
                openOtherApp(PackageName.music);
                break;
        }
    }
}
