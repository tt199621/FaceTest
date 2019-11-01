package com.example.facetest.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.activity.ShowActActivity;
import com.example.facetest.bean.SpeakBean;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;


public class BaseDispatchTouchActivity extends AppCompatActivity implements Robot.NlpListener {
    private CountTimer countTimerView;
    List<SpeakBean> datas;
    ListDataSave save,activity;
    List<String> actList;
    Robot robot=Robot.getInstance();
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
        robot.removeNlpListener(this);
        super.onPause();
        countTimerView.cancel();
    }
    @Override
    protected void onResume() {
        super.onResume();
        robot.addNlpListener(this);
        timeStart();
    }


    @Override
    public void onNlpCompleted(NlpResult nlpResult) {
        activity=new ListDataSave(this,"activityData");
        save=new ListDataSave(this,"speakData");
        datas=new ArrayList<>();
        actList=new ArrayList<>();

        //活动介绍回调ax
        if (nlpResult.action.substring(0,1).equals("a")){
            actList=activity.getLocation(nlpResult.action);
            if (actList.size()!=0){
                Intent intent=new Intent(this,ShowActActivity.class);
                intent.putStringArrayListExtra("showData", (ArrayList<String>) actList);
                startActivity(intent);
            }
        }

        //业务问答回调qx
        if (nlpResult.action.substring(0,1).equals("q")){
            datas=save.getSpeakData("speak");
            for (int i = 0; i < datas.size(); i++) {
                if (nlpResult.action.equals(datas.get(i).getQuestion())){
                    robot.speak(TtsRequest.create(datas.get(i).getAnswer(),false));
                }
            }
        }

    }


}
