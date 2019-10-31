package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;
import com.example.facetest.adapter.SettingSpeakAdapter;
import com.example.facetest.bean.SpeakBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;

import java.util.ArrayList;
import java.util.List;

public class SettingSpeakActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish,add_speak;
    private RecyclerView recycler_setting_speak;
    private LinearLayoutManager linearLayoutManager;
    private ListDataSave save;
    private List<SpeakBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_speak);
        initView();
        initData();
    }

    public void initView(){
        list=new ArrayList<>();
        save=new ListDataSave(this,"speakData");
        linearLayoutManager=new LinearLayoutManager(this);
        recycler_setting_speak=findViewById(R.id.recycler_setting_speak);
        add_speak=findViewById(R.id.add_speak);
        finish=findViewById(R.id.finish);
        add_speak.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    public void initData(){
        list=save.getSpeakData("speak");
        SettingSpeakAdapter adapter=new SettingSpeakAdapter(this,list);
        recycler_setting_speak.setLayoutManager(linearLayoutManager);
        recycler_setting_speak.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.add_speak://添加说话
                startActivity(new Intent(this,AddSpeakActivity.class));
                finish();
                break;
        }
    }
}
