package com.example.facetest.activity_exhibition;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.facetest.R;
import com.example.facetest.bean.ExhibitionBean;
import com.example.facetest.bean.LocationBean;
import com.example.facetest.util.CountTimer;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements Robot.TtsListener, OnGoToLocationStatusChangedListener {

    private ImageView guide_image,finish;
    private TextView guide_introduce;
    private ListDataSave save;
    private List<String> locations;
    private List<ExhibitionBean> beans;
    private Robot robot;
    private int order=0;//导览顺序编号
    private Boolean code=false;
    private CountTimer timer;
    private LinearLayout linear_guide;
    private int stopCode=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
    }

    public void  initView(){
        robot=Robot.getInstance();
        locations=new ArrayList<>();
        beans=new ArrayList<>();
        save=new ListDataSave(this,"location");
        locations=save.getLocation("location_order");
        guide_image=findViewById(R.id.guide_image);
        guide_introduce=findViewById(R.id.guide_introduce);
        linear_guide=findViewById(R.id.linear_guide);
        timer=new CountTimer(60000,1000,this);//设置定时
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                timer.cancel();
            }
        });
        linear_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopCode%2==0){
                    // 取消监听
                    Toast.makeText(GuideActivity.this, "已暂停", Toast.LENGTH_SHORT).show();
                    robot.removeOnGoToLocationStatusChangedListener(GuideActivity.this);
                    robot.removeTtsListener(GuideActivity.this);
                    timer.cancel();
                    stopCode++;
                }else {
                    Toast.makeText(GuideActivity.this, "继续出发", Toast.LENGTH_SHORT).show();
                    robot.addOnGoToLocationStatusChangedListener(GuideActivity.this);
                    robot.addTtsListener(GuideActivity.this);
                    if (order<beans.size()){
                        robot.goTo(beans.get(order).getLocation());//前往下一个展位
                    }else {
                        Toast.makeText(GuideActivity.this, "没有更多展位了", Toast.LENGTH_SHORT).show();
                        linear_guide.setOnClickListener(null);
                    }
                    stopCode++;
                }
            }
        });
    }

    public void initData(){
        locations.remove("home base");
        beans.clear();
        for (int i = 0; i < locations.size(); i++) {
            List<LocationBean> data=save.getDataList(locations.get(i));
            if (data!=null){
                ExhibitionBean bean=new ExhibitionBean();
                bean.setLocation(locations.get(i));
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).getCode()==0){
                        bean.setText(data.get(j).getContent());//介绍文字
                    }
                    if (data.get(j).getCode()==1){
                        bean.setSpeak(data.get(j).getContent());//语音介绍
                    }
                    if (data.get(j).getCode()==2){
                        bean.setSrc(data.get(j).getContent());//图片路径
                    }
                }
                beans.add(bean);
            }
        }

        //第一个展位信息
        Glide.with(this).load(beans.get(order).getSrc()).placeholder(R.drawable.screensaver1).into(guide_image);//展位图片
        guide_introduce.setText(beans.get(order).getText());//文字信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    robot.speak(TtsRequest.create("请您跟我来",false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        robot.goTo(beans.get(order).getLocation());//前往第一个展位
    }

    //说话回调
    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        switch (ttsRequest.getStatus()){
            case COMPLETED:
                if (code==true){
                    if (order<beans.size()){
                        Glide.with(this).load(beans.get(order).getSrc()).placeholder(R.drawable.screensaver1).into(guide_image);//展位图片
                        guide_introduce.setText(beans.get(order).getText());//文字信息
                        robot.goTo(beans.get(order).getLocation());//前往下一个展位
                        code=false;
                    }else {
                        timer.start();
                    }
                }
                break;
        }
    }




    //前往回调
    @Override
    public void onGoToLocationStatusChanged(String s, String s1, int i, String s2) {
        switch (s1){
            case "complete":
                if (order<beans.size()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                                robot.speak(TtsRequest.create(beans.get(order).getSpeak(), false));//语音介绍
                                code=true;
                                order++;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            case "abort":
//                startActivity(new Intent(this,GuideActivity.class));
//                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 注册监听事件
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addTtsListener(this);
    }

    @Override
    protected void onPause() {
        // 取消监听
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeTtsListener(this);
        timer.cancel();
        super.onPause();
    }


}
