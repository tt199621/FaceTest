package com.example.facetest.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facetest.R;
import com.example.facetest.activity_exhibition.ExhibitionModeActivity;
import com.example.facetest.activity_work.WorkModelActivity;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

public class AppApplication extends Application{
    private static final String TAG = "FloatWindow";

    @Override
    public void onCreate() {
        super.onCreate();
        initFloat();
    }

    private void initFloat() {
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.logo);
        FloatWindow//悬浮窗
                .with(getApplicationContext())
                .setView(imageView)
                .setWidth(Screen.width, 0.05f) //设置悬浮控件宽高
                .setHeight(Screen.width, 0.05f)
                .setX(Screen.width, 0.9f)
                .setY(Screen.height, 0.7f)
                .setMoveType(MoveType.slide,100,100)
                .setMoveStyle(500, new BounceInterpolator())
                .setFilter(true, ExhibitionModeActivity.class, WorkModelActivity.class)
                .setViewStateListener(mViewStateListener)
                .setPermissionListener(mPermissionListener)
                .setDesktopShow(true)
                .build();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //后台返回到前台
                SharedPreferences sp=getSharedPreferences("modeDB",MODE_PRIVATE);
                if(sp.getString("mode","").equals("")||sp.getString("mode","").equals("展厅模式")){
                    gototherApp("com.example.facetest","com.example.facetest.activity_exhibition.ExhibitionModeActivity");
                }else {
                    gototherApp("com.example.facetest","com.example.facetest.activity_work.WorkModelActivity");
                }
            }
        });
    }
    /**
     *
     * @param packages
     * @param activityPath
     */
    private void gototherApp(String packages,String activityPath) {
        Log.e("=====", "=====");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packages,
                activityPath);
        intent.setComponent(cn);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivityInfo(getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {//启动的intent存在
            startActivity(intent);
        } else {
            Toast.makeText(this, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }
    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
        }
    };

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
        }
    };

}
