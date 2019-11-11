package com.example.facetest.activity_setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.facetest.R;
import com.example.facetest.util.BaseDispatchTouchActivity;

public class SettingDefaultModeActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private RadioGroup radio_group;
    private ImageView finish;
    private Button commit_mode;
    private String mode="工作模式";
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_default_mode);
        initView();
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio_btn_1:
                        mode="工作模式";
                        break;
                    case R.id.radio_btn_2:
                        mode="展厅模式";
                        break;
                    case R.id.radio_btn_3:
                        mode="";
                        break;
                }
            }
        });
    }

    public void initView(){
        editor=getSharedPreferences("modeDB",MODE_PRIVATE).edit();
        radio_group=findViewById(R.id.radio_group);
        finish=findViewById(R.id.finish);
        commit_mode=findViewById(R.id.commit_mode);
        finish.setOnClickListener(this);
        commit_mode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.commit_mode:
                editor.putString("mode",mode);
                editor.commit();
                Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

}
