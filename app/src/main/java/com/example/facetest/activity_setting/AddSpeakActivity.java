package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.facetest.R;
import com.example.facetest.bean.SpeakBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加语音应答
 */
public class AddSpeakActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish;
    private EditText speak_code,speak_content;
    private Button commit_speak_add;
    private ListDataSave save;
    private List<SpeakBean> list;
    private Robot robot=Robot.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_speak);
        initView();
    }

    public void initView(){
        list=new ArrayList<>();
        finish=findViewById(R.id.finish);
        speak_code=findViewById(R.id.speak_code);
        speak_content=findViewById(R.id.speak_content);
        commit_speak_add=findViewById(R.id.commit_speak_add);
        commit_speak_add.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    public void saveData(){
        Boolean code=true;
        save=new ListDataSave(this,"speakData");
        list=save.getSpeakData("speak");
        //遍历查看是否有重复问题
        for (int i = 0; i < list.size(); i++) {
            if (!speak_code.getText().toString().equals("")){
                if (list.get(i).getQuestion().equals(speak_code.getText().toString())){
                    robot.speak(TtsRequest.create(speak_code.getText().toString()+"已经设置过",true));
                    code=false;
                }
            }else {
                code=false;
                robot.speak(TtsRequest.create("问题不能为空",false));
            }
        }
        if (code==true){
            SpeakBean speakBean=new SpeakBean();
            if (speak_code.getText().toString().substring(0,1).equals("q")){
                speakBean.setQuestion(speak_code.getText().toString());
                if (speak_content.getText().toString().equals("")){
                    speakBean.setAnswer("还未设置回答");
                }else {
                    speakBean.setAnswer(speak_content.getText().toString());
                }
                list.add(speakBean);
                save.setSpeakData("speak",list);
                robot.speak(TtsRequest.create("保存成功",false));
                startActivity(new Intent(this,SettingSpeakActivity.class));
                finish();
            }else {
                robot.speak(TtsRequest.create("问题格式输入错误",false));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.commit_speak_add:
                saveData();
                break;
        }
    }
}
