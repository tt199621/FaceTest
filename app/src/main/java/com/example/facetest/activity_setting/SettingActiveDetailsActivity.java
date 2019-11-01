package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facetest.R;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;

public class SettingActiveDetailsActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish;
    private TextView activiety_name;
    private EditText activiety_text,activiety_speak,activiety_src;
    private Button commit_set_activiety;
    private String ax;
    private Intent intent;
    private ListDataSave save;
    private Robot robot=Robot.getInstance();
    private List<String> setList,getList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_active_details);
        initView();
        getData();
    }

    public void initView(){
        setList=new ArrayList<>();
        getList=new ArrayList<>();
        save=new ListDataSave(this,"activityData");
        intent=getIntent();
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        activiety_name=findViewById(R.id.activiety_name);
        activiety_text=findViewById(R.id.activiety_text);//文字
        activiety_speak=findViewById(R.id.activiety_speak);//语音
        activiety_src=findViewById(R.id.activiety_src);//图片路径
        commit_set_activiety=findViewById(R.id.commit_set_activiety);//确定
        commit_set_activiety.setOnClickListener(this);
    }

    //设置预览数据
    public void getData(){
        ax=intent.getStringExtra("ax");
        activiety_name.setText(ax);
        getList=save.getLocation(ax);
        if (getList.size()!=0){
            activiety_text.setText(getList.get(0));
            activiety_speak.setText(getList.get(1));
            activiety_src.setText(getList.get(2));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.commit_set_activiety:
                if (activiety_text.getText().toString().equals("")){
                    robot.speak(TtsRequest.create("文字介绍不能为空",false));
                }else if (activiety_speak.getText().toString().equals("")){
                    robot.speak(TtsRequest.create("语音介绍不能为空",false));
                }else if (activiety_src.getText().toString().equals("")){
                    robot.speak(TtsRequest.create("图片路径不能为空",false));
                }else {
                    setList.add(activiety_text.getText().toString());
                    setList.add(activiety_speak.getText().toString());
                    setList.add(activiety_src.getText().toString());
                    save.setLocation(ax,setList);
                    robot.speak(TtsRequest.create("保存成功",false));
                    finish();
                }
                break;
        }
    }
}
