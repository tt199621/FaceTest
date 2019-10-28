package com.example.facetest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;
import com.example.facetest.util.SaveData;

public class ModifyPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText modify_password1,modify_password2;
    private Button modify_commit;
    private ImageView finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        modify_password1=findViewById(R.id.modify_password1);
        modify_password2=findViewById(R.id.modify_password2);
        modify_commit=findViewById(R.id.modify_commit);
        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);
        modify_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.modify_commit:
                if (!modify_password1.getText().toString().equals("")){
                    if (modify_password1.getText().toString().equals(modify_password2.getText().toString())){
                        SaveData.setGuideData(this,"password",modify_password2.getText().toString());
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(this, "两次输入的密码不同", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
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
