package com.example.facetest.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facetest.R;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;

public class ShowActActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView show_activity_img,finish;
    private TextView show_activity_text;
    private List<String> showData;
    private Robot robot=Robot.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_act);
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        show_activity_img=findViewById(R.id.show_activity_img);
        show_activity_text=findViewById(R.id.show_activity_text);
        showData=new ArrayList<>();
        showData=getIntent().getStringArrayListExtra("showData");
        Log.d("showData",""+showData.get(0)+" "+showData.get(1)+" "+showData.get(2));
        Glide.with(this).load(showData.get(2)).placeholder(R.drawable.screensaver1).into(show_activity_img);
        show_activity_text.setText(showData.get(0)+"");
        robot.speak(TtsRequest.create(showData.get(1),false));
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.finish){
            finish();
        }
    }
}
