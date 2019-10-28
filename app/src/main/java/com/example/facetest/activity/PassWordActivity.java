package com.example.facetest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facetest.R;
import com.example.facetest.util.SaveData;

public class PassWordActivity extends AppCompatActivity implements View.OnClickListener {

    TextView inputpwd;
    Button button0,button1,button2,button3,button4,button5,button6,button7,button8,button9,buttondel,enterbtn;
    String password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        initView();
        if (SaveData.getGuideData(this,"password")==""){
            SaveData.setGuideData(this,"password","123456");
        }
    }

    public void initView(){
        inputpwd=findViewById(R.id.inputpwd);//输入框
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        button7=findViewById(R.id.button7);
        button8=findViewById(R.id.button8);
        button9=findViewById(R.id.button9);
        buttondel=findViewById(R.id.buttondel);//删除
        enterbtn=findViewById(R.id.enterbtn);//确定
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttondel.setOnClickListener(this);
        enterbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button0:
                password=password+"0";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button1:
                password=password+"1";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button2:
                password=password+"2";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button3:
                password=password+"3";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button4:
                password=password+"4";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button5:
                password=password+"5";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button6:
                password=password+"6";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button7:
                password=password+"7";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button8:
                password=password+"8";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.button9:
                password=password+"9";
                inputpwd.setText(password.replaceAll("null",""));
                break;
            case R.id.buttondel:
                if (password!=null){
                    if (password.replaceAll("null","").length()!=0&&password.replaceAll("null","").length()<=11) {
                        password = password.substring(0, password.length() - 1);
                        inputpwd.setText(password.replaceAll("null", ""));
                    }else {
                        password=null;
                        inputpwd.setText("");
                    }
                }
                break;
            case R.id.enterbtn:
                if (password!=null){
                    if (password.replaceAll("null","").equals(SaveData.getGuideData(this,"password"))){
                        startActivity(new Intent(this,SettingActivity.class));
                        finish();
                    }else {
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
