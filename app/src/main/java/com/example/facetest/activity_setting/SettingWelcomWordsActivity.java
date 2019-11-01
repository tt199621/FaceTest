package com.example.facetest.activity_setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.facetest.R;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.SaveData;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

/**
 * 设置迎宾词
 */
public class SettingWelcomWordsActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish;
    private EditText setting_welcom_words;
    private Button commit_set_words;
    private String words;
    private Robot robot=Robot.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_welcom_words);
        initView();
        getData();
    }

    public void initView(){
        setting_welcom_words=findViewById(R.id.setting_welcom_words);
        commit_set_words=findViewById(R.id.commit_set_words);
        finish=findViewById(R.id.finish);
        commit_set_words.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    //预览内容
    public void getData(){
       words=SaveData.getGuideData(this,"welcom");
       setting_welcom_words.setText(words+"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.commit_set_words:
                SaveData.setGuideData(this,"welcom",setting_welcom_words.getText().toString());
                robot.speak(TtsRequest.create("保存成功",false));
                finish();
                break;
        }
    }
}
