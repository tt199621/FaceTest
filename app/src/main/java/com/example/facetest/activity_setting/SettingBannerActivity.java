package com.example.facetest.activity_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;
import com.example.facetest.adapter.BannerAdapter;
import com.example.facetest.bean.BannerBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置轮播图
 */
public class SettingBannerActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView add_banner,finish;
    private RecyclerView recycler_banner;
    private LinearLayoutManager linearLayoutManager;
    private ListDataSave save;
    private List<BannerBean> bannerBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_banner);
        initView();
        initData();
    }

    public void initView(){
        bannerBeans=new ArrayList<>();
        save=new ListDataSave(this,"bannerDB");
        finish=findViewById(R.id.finish);
        add_banner=findViewById(R.id.add_banner);
        recycler_banner=findViewById(R.id.recycler_banner);
        linearLayoutManager=new LinearLayoutManager(this);
        finish.setOnClickListener(this);
        add_banner.setOnClickListener(this);
    }

    public void initData(){
        bannerBeans.clear();
        bannerBeans=save.getBanners("banner");
        BannerAdapter adapter=new BannerAdapter(this,bannerBeans);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycler_banner.setAdapter(adapter);
        recycler_banner.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
            case R.id.add_banner://添加图片
                startActivity(new Intent(this,AddBannerActivity.class));
                break;
        }
    }
}
