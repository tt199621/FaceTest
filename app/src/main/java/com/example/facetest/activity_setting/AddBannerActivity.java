package com.example.facetest.activity_setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facetest.R;
import com.example.facetest.bean.BannerBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;

import java.util.ArrayList;
import java.util.List;

public class AddBannerActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish;
    private EditText input_src;
    private Button save_banner;
    private ListDataSave save;
    private List<BannerBean> bannerBeans;
    private int code=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banner);
        initView();
    }

    public void initView(){
        save=new ListDataSave(this,"bannerDB");
        bannerBeans=new ArrayList<>();
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        input_src=findViewById(R.id.input_src);
        save_banner=findViewById(R.id.save_banner);
        save_banner.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.save_banner:
                bannerBeans=save.getBanners("banner");
                for (int i = 0; i < bannerBeans.size(); i++) {
                    if (code==bannerBeans.get(i).getCode()){
                        code++;
                    }
                }
                BannerBean bannerBean=new BannerBean();
                bannerBean.setCode(code);
                bannerBean.setSrc(""+input_src.getText().toString());
                bannerBeans.add(bannerBean);
                save.setBanners("banner",bannerBeans);
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
