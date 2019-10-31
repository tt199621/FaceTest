package com.example.facetest.activity_setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facetest.R;
import com.example.facetest.adapter.SettingSpeakAdapter;
import com.example.facetest.bean.SpeakBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改回答内容
 */
public class SettingSpeakDetailsActivity extends BaseDispatchTouchActivity implements View.OnClickListener{

    private ImageView finish;
    private TextView speak_details_question;
    private EditText speak_details_anwser;
    private Button commit_speak_details;
    private ListDataSave save;
    private List<SpeakBean> speakBeans;
    private Robot robot=Robot.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_speak_details);
        initView();
        initData();
    }

    public void initView(){
        speakBeans=new ArrayList<>();
        save=new ListDataSave(this,"speakData");
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        speak_details_question=findViewById(R.id.speak_details_question);
        speak_details_anwser=findViewById(R.id.speak_details_anwser);
        commit_speak_details=findViewById(R.id.commit_speak_details);
        commit_speak_details.setOnClickListener(this);
    }

    public void initData(){
        speak_details_question.setText(SettingSpeakAdapter.questions);
        /**
         * 设置预览数据
         */
        speakBeans=save.getSpeakData("speak");
        for (int i = 0; i < speakBeans.size(); i++) {
            if (speakBeans.get(i).getQuestion().equals(SettingSpeakAdapter.questions)){
                speak_details_anwser.setText(speakBeans.get(i).getAnswer());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.commit_speak_details://提交修改
                if (!speak_details_anwser.getText().toString().equals("")){
                    for (int i = 0; i < speakBeans.size(); i++) {
                        if (speakBeans.get(i).getQuestion().equals(SettingSpeakAdapter.questions)){
                            speakBeans.remove(i);
                        }
                    }
                    SpeakBean speakBean=new SpeakBean();
                    speakBean.setQuestion(SettingSpeakAdapter.questions);
                    speakBean.setAnswer(speak_details_anwser.getText().toString());
                    speakBeans.add(speakBean);
                    save.setSpeakData("speak",speakBeans);
                    robot.speak(TtsRequest.create("保存成功",false));
                    finish();
                }else {
                    robot.speak(TtsRequest.create("内容不能为空",false));
                }
                break;
        }
    }
}
