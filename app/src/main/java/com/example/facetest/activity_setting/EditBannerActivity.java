package com.example.facetest.activity_setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;
import com.example.facetest.adapter.BannerAdapter;
import com.example.facetest.bean.BannerBean;
import com.example.facetest.util.ListDataSave;

import java.util.ArrayList;
import java.util.List;

public class EditBannerActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView finish;
    private EditText edit_src;
    private Button save_banner_edit;
    private ListDataSave save;
    private List<BannerBean> bannerBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_banner);
        initView();
    }

    public void initView(){
        save=new ListDataSave(this,"bannerDB");
        bannerBeans=new ArrayList<>();
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        edit_src=findViewById(R.id.edit_src);
        save_banner_edit=findViewById(R.id.save_banner_edit);
        save_banner_edit.setOnClickListener(this);
        edit_src.setText(""+BannerAdapter.src);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.save_banner_edit:
                bannerBeans=save.getBanners("banner");
                for (int i = 0; i < bannerBeans.size(); i++) {
                    if (BannerAdapter.code ==bannerBeans.get(i).getCode()){
                        bannerBeans.get(i).setSrc(edit_src.getText().toString());
                        save.setBanners("banner",bannerBeans);
                    }
                }
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
