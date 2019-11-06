package com.example.facetest.util;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.example.facetest.activity.MainActivity;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

public class CountTimer extends CountDownTimer implements Robot.TtsListener, OnGoToLocationStatusChangedListener {
    private Context context;
    Robot robotTime=Robot.getInstance();
    Boolean code=false;


    /**
     * 参数 millisInFuture       倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     */
    public CountTimer(long millisInFuture, long countDownInterval,Context context) {
        super(millisInFuture, countDownInterval);
        this.context=context;
    }
    // 计时完毕回到主页面
    @Override
    public void onFinish() {
        // 注册监听事件
        robotTime.addTtsListener(this);
        robotTime.addOnGoToLocationStatusChangedListener(this);
        robotTime.speak(TtsRequest.create("没人跟我玩，那我回去充电咯",false));
        code=true;
    }
    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
    }

    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        switch (ttsRequest.getStatus()){
            case COMPLETED:
                if (code==true)
                    robotTime.goTo("home base");
                break;
        }
    }

    @Override
    public void onGoToLocationStatusChanged(String s, String s1, int i, String s2) {
        switch (s1){
            case "complete":
                if (code==true){
                    code=false;
                    context.startActivity(new Intent(context, MainActivity.class));
                    robotTime.removeTtsListener(this);
                    robotTime.removeOnGoToLocationStatusChangedListener(this);
                }
                break;
            case "abort"://中途打断
                if (code==true){
                    robotTime.removeTtsListener(this);
                    robotTime.removeOnGoToLocationStatusChangedListener(this);
                    code=false;
                }
                break;
        }
    }

}