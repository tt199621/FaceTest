package com.example.facetest.activity_exhibition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facetest.R;
import com.example.facetest.activity.ContactsActivity;
import com.example.facetest.activity.MainActivity;
import com.example.facetest.activity.PassWordActivity;
import com.example.facetest.activity_setting.SettingExhibitonActivity;
import com.example.facetest.activity_work.WorkModelActivity;
import com.example.facetest.bean.LocationBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;
import com.example.facetest.util.PackageName;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 展厅模式
 */
public class ExhibitionModeActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private Robot robot;
    String[] randomPlay={"我已经进入展厅模式啦，要来参观下展厅吗",
            "进入展厅状态，我要当个小导游啦",
            "进入展厅状态，我带你参观吧"};
    private List<String> locations;
    private ListDataSave save;

    private ImageView introduction,exhibition,returnhome,switch_btn,conf_btn,contacts,weatherbtn,news,imageView4,music_qq;
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
        exhibition=findViewById(R.id.exhibition);//导览介绍按钮
        returnhome=findViewById(R.id.returnhome);//返回屏保页
        switch_btn=findViewById(R.id.switch_btn);//切换模式
        conf_btn=findViewById(R.id.conf_btn);//设置
        contacts=findViewById(R.id.contacts);//联系人
        contacts.setOnClickListener(this);
        weatherbtn=findViewById(R.id.weatherbtn);//天气
        weatherbtn.setOnClickListener(this);
        news=findViewById(R.id.news);//新闻
        news.setOnClickListener(this);
        imageView4=findViewById(R.id.imageView4);//体育
        imageView4.setOnClickListener(this);
        music_qq=findViewById(R.id.music_qq);//音乐
        music_qq.setOnClickListener(this);
        conf_btn.setOnClickListener(this);
        switch_btn.setOnClickListener(this);
        returnhome.setOnClickListener(this);
        introduction.setOnClickListener(this);
        exhibition.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.introduction://展位介绍
                startActivity(new Intent(this, ExhibitionItemActivity.class));
                break;
            case R.id.exhibition://导览介绍
                Boolean intentCode=true;
                locations=new ArrayList<>();
                save=new ListDataSave(this,"location");
                locations=save.getLocation("location_order");
                if (robot.getLocations().size()==1){
                    robot.speak(TtsRequest.create("请先添加展位",false));
                }else {
                    if(locations==null||locations.size()==0){
                        Toast.makeText(this, "您还未添加展位", Toast.LENGTH_SHORT).show();
                    }else {
                        for (int i = 0; i < locations.size(); i++) {
                            List<LocationBean> data = save.getDataList(locations.get(i));
                            if (data.size() == 0) {
                                robot.speak(TtsRequest.create("请先给" + locations.get(i) + "设置展位信息", true));
                                intentCode=false;
                                startActivity(new Intent(this, SettingExhibitonActivity.class));
                            }
                        }
                        if (intentCode==true){
                            startActivity(new Intent(this,GuideActivity.class));
                        }
                    }
                }
                break;
            case R.id.conf_btn://设置
                startActivity(new Intent(this, PassWordActivity.class));
                break;
            case R.id.returnhome://回主页
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.switch_btn://切换模式
                startActivity(new Intent(this, WorkModelActivity.class));
                finish();
                break;
            case R.id.contacts://联系人
                startActivity(new Intent(this, ContactsActivity.class));
                break;
            case R.id.weatherbtn://天气
                openOtherApp(PackageName.weather);
                break;
            case R.id.news://新闻
                openOtherApp(PackageName.news);
                break;
            case R.id.imageView4://闲聊
                openOtherApp(PackageName.chat);
                break;
            case R.id.music_qq://音乐
                openOtherApp(PackageName.music);
                break;
        }
    }

    //打开外部APP
    private void openOtherApp(String packagename) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packagename);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
