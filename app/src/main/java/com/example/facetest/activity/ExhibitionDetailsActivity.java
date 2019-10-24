package com.example.facetest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;

public class ExhibitionDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView finish_exhibition_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_details);
        initView();
    }

    public void initView(){
        finish_exhibition_details=findViewById(R.id.finish_exhibition_details);
        finish_exhibition_details.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish_exhibition_details:
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
