package com.example.facetest.util;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;


public class BaseDispatchTouchActivity extends AppCompatActivity implements Robot.NlpListener {
    private CountTimer countTimerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void timeStart(){
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();
            }
        });
    }
    private void init() {
        //初始化CountTimer，设置倒计时为1分钟。
        countTimerView=new CountTimer(1*60000,1000,BaseDispatchTouchActivity.this);
    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                countTimerView.start();
                break;
            //否则其他动作计时取消
            default:countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }
    @Override
    protected void onResume() {

        super.onResume();
        timeStart();
    }

    @Override
    public void onNlpCompleted(NlpResult nlpResult) {
        switch (nlpResult.action){
            case "q1":
                break;
        }
    }
}
