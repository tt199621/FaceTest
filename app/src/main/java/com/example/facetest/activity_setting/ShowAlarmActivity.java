package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;
import com.example.facetest.receiver.AlarmReceiver;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

/**
 * 闹钟响起时展示框
 */
public class ShowAlarmActivity extends AppCompatActivity implements View.OnClickListener, Robot.TtsListener {

    private TextView tv_msg,downtime;
    private Button btn_cancle,btn_sure;
    private Robot robot=Robot.getInstance();
    private int speakNo=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);
        initView();
    }

    public void initView(){
        tv_msg=findViewById(R.id.tv_msg);
        btn_cancle=findViewById(R.id.btn_cancle);//查看
        btn_sure=findViewById(R.id.btn_sure);//我知道了
        downtime=findViewById(R.id.downtime);//倒计时
        tv_msg.setText("您有一条"+ AlarmReceiver.type+"提醒");
        btn_cancle.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        toMainActivity.start();
        robot.speak(TtsRequest.create("您好，小主人，您有一条"+AlarmReceiver.type+"提醒",false));
    }

    /**
     * 倒计时60秒，一次1秒
     *///倒计时控制
    CountDownTimer toMainActivity = new CountDownTimer(20 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            downtime.setText(l/1000+"");
        }

        @Override
        public void onFinish() {
            finish();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancle://查看日程
                startActivity(new Intent(this,LookAlarmActivity.class));
                finish();
                break;
            case R.id.btn_sure:
                toMainActivity.cancel();
                finish();
                break;
        }
    }

    //说话回调
    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        switch (ttsRequest.getStatus()){
            case COMPLETED:
                if (speakNo<2){
                    robot.speak(TtsRequest.create("您好，小主人，您有一条"+AlarmReceiver.type+"提醒",false));
                    speakNo++;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册监听事件
        robot.addTtsListener(this);
    }

    @Override
    protected void onPause() {
        // 取消监听
        robot.removeTtsListener(this);
        super.onPause();
    }

}
