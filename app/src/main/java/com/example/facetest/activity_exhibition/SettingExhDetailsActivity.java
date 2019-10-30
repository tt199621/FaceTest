package com.example.facetest.activity_exhibition;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetest.R;
import com.example.facetest.adapter.SettingExhibitonAdapter;
import com.example.facetest.bean.LocationBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;

import java.util.ArrayList;
import java.util.List;

public class SettingExhDetailsActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private TextView exh_name;
    private EditText setting_text,setting_speak,setting_src;
    private Button commit_set_exh;
    private ImageView finish;
    private List<LocationBean> datas;
    private ListDataSave save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_exh_details);
        initView();
        preView();
    }

    public void initView(){
        save=new ListDataSave(this,"location");
        datas=new ArrayList<>();
        exh_name=findViewById(R.id.exh_name);
        setting_text=findViewById(R.id.setting_text);
        setting_speak=findViewById(R.id.setting_speak);
        setting_src=findViewById(R.id.setting_src);
        commit_set_exh=findViewById(R.id.commit_set_exh);
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        commit_set_exh.setOnClickListener(this);
    }

    public void  initData(){
        datas.clear();
        if (setting_text.getText().toString().equals("")){
            LocationBean locationBean=new LocationBean();
            locationBean.setCode(0);
            locationBean.setContent("");
            datas.add(locationBean);
        }else {
            LocationBean locationBean=new LocationBean();
            locationBean.setCode(0);
            locationBean.setContent(setting_text.getText().toString());
            datas.add(locationBean);
        }

        if (setting_speak.getText().toString().equals("")){
            LocationBean locationBean=new LocationBean();
            locationBean.setCode(1);
            locationBean.setContent("");
            datas.add(locationBean);
        }else {
            LocationBean locationBean=new LocationBean();
            locationBean.setCode(1);
            locationBean.setContent(setting_speak.getText().toString());
            datas.add(locationBean);
        }

        if (setting_src.getText().toString().equals("")){
            LocationBean locationBean=new LocationBean();
            locationBean.setCode(2);
            locationBean.setContent("");
            datas.add(locationBean);
        }else {
            LocationBean locationBean=new LocationBean();
            locationBean.setCode(2);
            locationBean.setContent(setting_src.getText().toString());
            datas.add(locationBean);
        }

        save.setDataList(SettingExhibitonAdapter.exhName,datas);
    }

    //预览数据
    public void preView(){
        exh_name.setText(""+SettingExhibitonAdapter.exhName);
        List<LocationBean> data=save.getDataList(SettingExhibitonAdapter.exhName);
        if (data!=null){
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getCode()==0){
                    setting_text.setText(""+data.get(i).getContent());
                }
                if (data.get(i).getCode()==1){
                    setting_speak.setText(""+data.get(i).getContent());
                }
                if (data.get(i).getCode()==2){
                    setting_src.setText(""+data.get(i).getContent());
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.commit_set_exh:
                //保存
                initData();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
