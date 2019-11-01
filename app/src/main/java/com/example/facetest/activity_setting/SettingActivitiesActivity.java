package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facetest.R;
import com.example.facetest.util.BaseDispatchTouchActivity;

/**
 * 设置活动介绍
 */
public class SettingActivitiesActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish;
    private TextView a1,a2,a3,a4,a5,a6,a7,a8,a9,a10;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_activities);
        initView();
    }

    public void initView(){
        intent=new Intent(this,SettingActiveDetailsActivity.class);
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        a1=findViewById(R.id.a1);
        a2=findViewById(R.id.a2);
        a3=findViewById(R.id.a3);
        a4=findViewById(R.id.a4);
        a5=findViewById(R.id.a5);
        a6=findViewById(R.id.a6);
        a7=findViewById(R.id.a7);
        a8=findViewById(R.id.a8);
        a9=findViewById(R.id.a9);
        a10=findViewById(R.id.a10);
        a1.setOnClickListener(this);
        a2.setOnClickListener(this);
        a3.setOnClickListener(this);
        a4.setOnClickListener(this);
        a5.setOnClickListener(this);
        a6.setOnClickListener(this);
        a7.setOnClickListener(this);
        a8.setOnClickListener(this);
        a9.setOnClickListener(this);
        a10.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.a1:
                intent.putExtra("ax","a1");
                startActivity(intent);
                break;
            case R.id.a2:
                intent.putExtra("ax","a2");
                startActivity(intent);
                break;
            case R.id.a3:
                intent.putExtra("ax","a3");
                startActivity(intent);
                break;
            case R.id.a4:
                intent.putExtra("ax","a4");
                startActivity(intent);
                break;
            case R.id.a5:
                intent.putExtra("ax","a5");
                startActivity(intent);
                break;
            case R.id.a6:
                intent.putExtra("ax","a6");
                startActivity(intent);
                break;
            case R.id.a7:
                intent.putExtra("ax","a7");
                startActivity(intent);
                break;
            case R.id.a8:
                intent.putExtra("ax","a8");
                startActivity(intent);
                break;
            case R.id.a9:
                intent.putExtra("ax","a9");
                startActivity(intent);
                break;
            case R.id.a10:
                intent.putExtra("ax","a10");
                startActivity(intent);
                break;

        }
    }
}
