package com.example.facetest.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;
import com.example.facetest.adapter.SettingExhibitonAdapter;
import com.robotemi.sdk.Robot;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置展厅页面
 */
public class SettingExhibitonActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView finish_setting_exhibition;
    private RecyclerView recycler_setting_exhibition;
    private LinearLayoutManager linearLayoutManager;
    private Robot robot;
    private List<String> locations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_exhibiton);
        initView();
        initData();
    }


    public void initView(){
        robot=Robot.getInstance();
        locations=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this);
        finish_setting_exhibition=findViewById(R.id.finish_setting_exhibition);
        recycler_setting_exhibition=findViewById(R.id.recycler_setting_exhibition);
        finish_setting_exhibition.setOnClickListener(this);
    }

    public void initData(){
        locations.clear();
        locations=robot.getLocations();
        locations.remove("home base");//移除充电桩位置
        for (int i = 0; i < locations.size(); i++) {
            Log.d("tt",""+locations.get(i));
        }
        SettingExhibitonAdapter adapter=new SettingExhibitonAdapter(this,locations);
        recycler_setting_exhibition.setLayoutManager(linearLayoutManager);
        recycler_setting_exhibition.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish_setting_exhibition:
                finish();
                break;
        }
    }
}
