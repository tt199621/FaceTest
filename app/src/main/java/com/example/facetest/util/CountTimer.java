package com.example.facetest.util;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.example.facetest.activity.MainActivity;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class CountTimer extends CountDownTimer {
    private Context context;

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
        context.startActivity(new Intent(context, MainActivity.class));
        Robot robot=Robot.getInstance();
        robot.speak(TtsRequest.create("没人跟我玩，那我回去充电咯",false));
        robot.goTo("home base");
    }
    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
    }
}