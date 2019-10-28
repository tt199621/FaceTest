package com.example.facetest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.Random;

/**
 * 展厅模式
 */
public class ExhibitionModeActivity extends AppCompatActivity implements View.OnClickListener {


    private Robot robot;
    String[] randomPlay={"我已经进入展厅模式啦，要来参观下展厅吗",
            "进入展厅状态，我要当个小导游啦",
            "进入展厅状态，我带你参观吧"};

    private ImageView introduction,returnhome,switch_btn,conf_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_mode);
        initView();
        randomPlay();
    }

    //进入时随机播放
    public void randomPlay(){
        robot.speak(TtsRequest.create(randomPlay[new Random().nextInt(randomPlay.length)],false));
    }

    public void initView(){
        robot=Robot.getInstance();
        introduction=findViewById(R.id.introduction);//展位介绍按钮
        returnhome=findViewById(R.id.returnhome);//返回屏保页
        switch_btn=findViewById(R.id.switch_btn);//切换模式
        conf_btn=findViewById(R.id.conf_btn);//设置
        conf_btn.setOnClickListener(this);
        switch_btn.setOnClickListener(this);
        returnhome.setOnClickListener(this);
        introduction.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.introduction://展位介绍
                startActivity(new Intent(this, ExhibitionItemActivity.class));
                break;
            case R.id.conf_btn://设置
                startActivity(new Intent(this,PassWordActivity.class));
                break;
            case R.id.returnhome:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
