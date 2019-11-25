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

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.example.facetest.Arcface.common.Constants;
import com.example.facetest.R;
import com.example.facetest.activity.ContactsActivity;
import com.example.facetest.activity_exhibition.ExhibitionItemActivity;
import com.example.facetest.activity_exhibition.ExhibitionModeActivity;
import com.example.facetest.activity_exhibition.GuideActivity;
import com.example.facetest.activity_setting.SettingAlarmActivity;
import com.example.facetest.activity_setting.SettingExhibitonActivity;
import com.example.facetest.activity_work.WorkModelActivity;
import com.example.facetest.bean.LocationBean;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppApplication extends Application implements Robot.NlpListener {
    private static final String TAG = "FloatWindow";
    Robot robot;
    private List<String> locations;
    private ListDataSave save;
    private Intent intent;
    private FaceEngine faceEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        initFloat();
        robot=Robot.getInstance();
        robot.addNlpListener(this);
        intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activeEngine(null);
    }

    /**
     * 激活引擎
     *
     * @param view
     */
    public void activeEngine(final View view) {
        faceEngine = new FaceEngine();
        if (view != null) {
            view.setClickable(false);
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int activeCode = faceEngine.activeOnline(AppApplication.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            Toast.makeText(AppApplication.this, "引擎激活成功", Toast.LENGTH_SHORT).show();
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                        } else {
                            Toast.makeText(AppApplication.this, ""+R.string.active_failed, Toast.LENGTH_SHORT).show();
                        }

                        if (view != null) {
                            view.setClickable(true);
                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = faceEngine.getActiveFileInfo(AppApplication.this,activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        robot.removeNlpListener(this);
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

    @Override
    public void onNlpCompleted(NlpResult nlpResult) {
        switch (nlpResult.action){
            case "work"://工作模式
                intent.setClass(this,WorkModelActivity.class);
                startActivity(intent);
                break;
            case "exhibition"://展厅模式
                intent.setClass(this,ExhibitionModeActivity.class);
                startActivity(intent);
                break;
            case "booth"://展位介绍
                Boolean Code=true;
                locations=new ArrayList<>();
                save=new ListDataSave(this,"location");
                locations=save.getLocation("location_order");
                if (robot.getLocations().size()==1){
                    robot.speak(TtsRequest.create("请先添加位置",false));
                }else {
                    if(locations==null||locations.size()==0){
                        robot.speak(TtsRequest.create("位置未添加到展位中",false));
                    }else {
                        for (int i = 0; i < locations.size(); i++) {
                            List<LocationBean> data = save.getDataList(locations.get(i));
                            if (data.size() == 0) {
                                robot.speak(TtsRequest.create("请先给" + locations.get(i) + "设置展位信息", true));
                                Code=false;
                                intent.setClass(this,SettingExhibitonActivity.class);
                                startActivity(intent);
                            }
                        }
                        if (Code==true){
                            intent.setClass(this,ExhibitionItemActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case "tour"://导览介绍
                Boolean intentCode=true;
                locations=new ArrayList<>();
                save=new ListDataSave(this,"location");
                locations=save.getLocation("location_order");
                if (robot.getLocations().size()==1){
                    robot.speak(TtsRequest.create("请先添加位置",false));
                }else {
                    if(locations==null||locations.size()==0){
                        robot.speak(TtsRequest.create("位置未添加到展位中",false));
                    }else {
                        for (int i = 0; i < locations.size(); i++) {
                            List<LocationBean> data = save.getDataList(locations.get(i));
                            if (data.size() == 0) {
                                robot.speak(TtsRequest.create("请先给" + locations.get(i) + "设置展位信息", true));
                                intentCode=false;
                                intent.setClass(this,SettingExhibitonActivity.class);
                                startActivity(intent);
                            }
                        }
                        if (intentCode==true){
                            intent.setClass(this,GuideActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case "schedule"://日程安排
                intent.setClass(this,SettingAlarmActivity.class);
                startActivity(intent);
                break;
            case "video"://视频会议
                intent.setClass(this,ContactsActivity.class);
                startActivity(intent);
                break;
        }
    }

}
