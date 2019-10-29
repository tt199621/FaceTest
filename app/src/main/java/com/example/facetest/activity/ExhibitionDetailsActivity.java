package com.example.facetest.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facetest.R;
import com.example.facetest.adapter.ExhibitonAdapter;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

public class ExhibitionDetailsActivity extends BaseDispatchTouchActivity implements View.OnClickListener, Robot.TtsListener , OnGoToLocationStatusChangedListener {

    private ImageView finish,image_details;
    private TextView exh_produce;
    Robot robot=Robot.getInstance();
    Boolean speakCode=true,goCode=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_details);
        initView();
    }

    public void initView(){
        finish=findViewById(R.id.finish);
        image_details=findViewById(R.id.image_details);
        exh_produce=findViewById(R.id.exh_produce);
        finish.setOnClickListener(this);
        Glide.with(this).load(ExhibitonAdapter.imgSrc).placeholder(R.mipmap.ic_launcher).into(image_details);//展位图片
        exh_produce.setText(ExhibitonAdapter.imgProduce);//文字介绍
        robot.speak(TtsRequest.create("请您跟我来",false));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
        }
    }

    //机器人说话回调
    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        switch (ttsRequest.getStatus()){
            case COMPLETED:
                if (speakCode==true){
                    robot.goTo(ExhibitonAdapter.location);
                    speakCode=false;
                }
                break;
        }
    }

    //机器人前往回调
    @Override
    public void onGoToLocationStatusChanged(String s, String s1) {
        switch (s1){
            case "start":
                break;
            case "complete":
                if (goCode==true){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                                Log.d("介绍：",""+ExhibitonAdapter.speak);
                                robot.speak(TtsRequest.create(ExhibitonAdapter.speak,false));
                                goCode=false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            case "abort"://中途打断
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册监听事件
        robot.addTtsListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
    }

    @Override
    protected void onPause() {
        // 取消监听
        robot.removeTtsListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        super.onPause();
    }


}
