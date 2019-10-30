package com.example.facetest.activity_work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.facetest.R;
import com.example.facetest.activity_exhibition.MainActivity;
import com.example.facetest.util.BaseDispatchTouchActivity;

public class WorkModelActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_model);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {

    }
}
