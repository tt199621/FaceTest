package com.example.facetest.activity_exhibition;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;
import com.example.facetest.adapter.ExhibitonAdapter;
import com.example.facetest.bean.ExhibitionBean;
import com.example.facetest.bean.LocationBean;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 展位列表页
 */
public class ExhibitionItemActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish_exhibition_details;
    private RecyclerView recycler_exh_details;
    private LinearLayoutManager linearLayoutManager;
    private List<ExhibitionBean> beans;
    private Robot robot=Robot.getInstance();
    private ListDataSave save;
    private List<String> locations;
    String[] randomPlay={"各位帅哥美女，快来看看您感兴趣的展位吧",
            "亲，快来选择下参观哪个展位呀",
            "亲，您喜欢哪个展位呀"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        initView();
        randomPlay();
        initData();
    }

    //进入时随机播放
    public void randomPlay(){
        robot.speak(TtsRequest.create(randomPlay[new Random().nextInt(randomPlay.length)],false));
    }

    public void initView(){
        beans=new ArrayList<>();
        save=new ListDataSave(this,"location");
        recycler_exh_details=findViewById(R.id.recycler_exh_details);
        linearLayoutManager=new LinearLayoutManager(this);
        finish_exhibition_details=findViewById(R.id.finish);
        finish_exhibition_details.setOnClickListener(this);
    }

    public void initData(){
        locations=new ArrayList<>();
        locations=save.getLocation("location_order");
        beans.clear();
        for (int i = 0; i < locations.size(); i++) {
            List<LocationBean> data=save.getDataList(locations.get(i));
            if (data!=null){
                ExhibitionBean bean=new ExhibitionBean();
                bean.setLocation(locations.get(i));
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).getCode()==0){
                        bean.setText(data.get(j).getContent());//介绍文字
                    }
                    if (data.get(j).getCode()==1){
                        bean.setSpeak(data.get(j).getContent());//语音介绍
                    }
                    if (data.get(j).getCode()==2){
                        bean.setSrc(data.get(j).getContent());//图片路径
                    }
                }
                beans.add(bean);
            }
        }
        ExhibitonAdapter adapter=new ExhibitonAdapter(this,beans);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycler_exh_details.setLayoutManager(linearLayoutManager);
        recycler_exh_details.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
