package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;

public class SettingActiveDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView finish;
    private String ax;
    private Intent intent=getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_active_details);
        initView();
        getData();
    }

    public void initView(){
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
    }

    //设置预览数据
    public void getData(){
        ax=intent.getStringExtra("ax");
        Log.d("ax",""+ax);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
        }
    }
}
