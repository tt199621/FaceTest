package com.example.facetest.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.example.facetest.Arcface.camera.CameraHelper;
import com.example.facetest.Arcface.camera.CameraListener;
import com.example.facetest.Arcface.model.DrawInfo;
import com.example.facetest.Arcface.util.ConfigUtil;
import com.example.facetest.Arcface.util.DrawHelper;
import com.example.facetest.Arcface.widget.FaceRectView;
import com.example.facetest.R;
import com.example.facetest.activity_exhibition.ExhibitionModeActivity;
import com.example.facetest.activity_exhibition.GuideActivity;
import com.example.facetest.activity_setting.SettingExhibitonActivity;
import com.example.facetest.activity_work.WorkModelActivity;
import com.example.facetest.bean.BannerBean;
import com.example.facetest.bean.LocationBean;
import com.example.facetest.util.AlertDialogUtils;
import com.example.facetest.util.GlideImageLoader;
import com.example.facetest.util.ListDataSave;
import com.example.facetest.util.SaveData;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRobotReadyListener , Robot.NlpListener , ViewTreeObserver.OnGlobalLayoutListener , View.OnClickListener {

    private Robot robot;
    private Banner banner;
    List<String> path,locations;
    List<BannerBean> bannerBeans;
    private ListDataSave save,saveLocation;

    private Button work_btn,exhibition_btn;
    private ImageView imageView2;

    private static final String TAG = "PreviewActivity";
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    private Integer rgbCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private FaceEngine faceEngine;
    ;
    private int afCode = -1;
    private int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS;
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    private View previewView;
    private FaceRectView faceRectView;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    private boolean ifSpeak=true;
    AlertDialogUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        robot=Robot.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }
        // Activity启动后就锁定为启动时的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        previewView = findViewById(R.id.texture_preview);
        faceRectView = findViewById(R.id.face_rect_view);
        //在布局结束后才做初始化操作
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        work_btn=findViewById(R.id.work_btn);
        exhibition_btn=findViewById(R.id.exhibition_btn);
        work_btn.setOnClickListener(this);
        exhibition_btn.setOnClickListener(this);
        imageView2=findViewById(R.id.imageView2);//返回初始点
        imageView2.setOnClickListener(this);
        setBanner();
        checkPermissions(NEEDED_PERMISSIONS);
    }

    //设置轮播图
    public void setBanner(){
        banner=findViewById(R.id.banner);
        path=new ArrayList<>();
        bannerBeans=new ArrayList<>();
        save=new ListDataSave(this,"bannerDB");
        path.clear();
        bannerBeans.clear();
        bannerBeans=save.getBanners("banner");
        for (int i = 0; i < bannerBeans.size(); i++) {
            path.add(bannerBeans.get(i).getSrc());
        }
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(path);
        //banner设置方法全部调用完毕时最后调用
        banner.setDelayTime(4000);
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);//不显示指示器和标题
        banner.start();
    }



    //获取权限
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this.getApplicationContext(), neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                if (cameraHelper != null) {
                    cameraHelper.start();
                }
            } else {
                Toast.makeText(this.getApplicationContext(), "权限请求失败", Toast.LENGTH_SHORT).show();
            }
        }
    }





    //初始化引擎
    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, 20, FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine:  init: " + afCode + "  version:" + versionInfo);
        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, "引擎初始化失败:"+afCode, Toast.LENGTH_SHORT).show();
        }
    }



    /** 倒计时10秒，一次1秒 *///倒计时控制迎宾频率
    CountDownTimer mTimer = new CountDownTimer(10 * 1000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            ifSpeak = true;
            utils.closeDialog();
        }
    };

    //初始化相机
    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Log.i(TAG, "onCameraOpened: " + cameraId + "  " + displayOrientation + " " + isMirror);
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror,false,false);
            }


            @Override
            public void onPreview(byte[] nv21, Camera camera) {

                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                List<FaceInfo> faceInfoList = new ArrayList<>();
                int code = faceEngine.detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList);
                if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                    code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
                    if (code != ErrorInfo.MOK) {
                        return;
                    }
                }else {
                    return;
                }

                List<AgeInfo> ageInfoList = new ArrayList<>();
                List<GenderInfo> genderInfoList = new ArrayList<>();
                List<Face3DAngle> face3DAngleList = new ArrayList<>();
                List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
                int ageCode = faceEngine.getAge(ageInfoList);
                int genderCode = faceEngine.getGender(genderInfoList);
                int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
                int livenessCode = faceEngine.getLiveness(faceLivenessInfoList);

                //有其中一个的错误码不为0，return
                if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
                    return;
                }
                if (faceRectView != null && drawHelper != null) {
                    List<DrawInfo> drawInfoList = new ArrayList<>();
                    for (int i = 0; i < faceInfoList.size(); i++) {
                        drawInfoList.add(new DrawInfo(drawHelper.adjustRect(faceInfoList.get(i).getRect()), genderInfoList.get(i).getGender(), ageInfoList.get(i).getAge(), faceLivenessInfoList.get(i).getLiveness(), null));
                    }
                    drawHelper.draw(faceRectView, drawInfoList);
                    /**
                     * 检测到活体
                     */
                    if (drawInfoList.size()>0&&ifSpeak){
                        SharedPreferences sp=getSharedPreferences("modeDB",MODE_PRIVATE);
                        String welcomWords=SaveData.getGuideData(MainActivity.this,"welcom");//迎宾词
                        ifSpeak=false;
                        /**
                         * 迎宾模式
                         */
                        if (sp.getString("mode","").equals("")){
                            mTimer.start();
                            Robot robotW=Robot.getInstance();
                            utils = AlertDialogUtils.getInstance();
                            if (welcomWords.equals("")){
                                robotW.speak(TtsRequest.create("您好，有什么可以帮您的？",false));
                                utils.showConfirmDialog(MainActivity.this, "您好，有什么可以帮您的？");
                            }else {
                                robotW.speak(TtsRequest.create(welcomWords,false));
                                utils.showConfirmDialog(MainActivity.this, welcomWords);
                            }
                            //按钮点击监听
                            utils.setOnButtonClickListener(new AlertDialogUtils.OnButtonClickListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog dialog) {
                                    startActivity(new Intent(MainActivity.this, ExhibitionModeActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog dialog) {
                                    startActivity(new Intent(MainActivity.this, WorkModelActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            });
                        }
                        /**
                         * 工作模式
                         */
                        if (sp.getString("mode","").equals("工作模式")){
                            startActivity(new Intent(MainActivity.this,WorkModelActivity.class));
                            finish();
                        }
                        /**
                         * 展厅模式
                         */
                        if (sp.getString("mode","").equals("展厅模式")){
                            mTimer.start();
                            Robot robotW=Robot.getInstance();
                            utils = AlertDialogUtils.getInstance();
                            if (welcomWords.equals("")){
                                robotW.speak(TtsRequest.create("您好，有什么可以帮您的？",false));
                                utils.showDialog(MainActivity.this,"您好，有什么可以帮您的？","展厅模式","展厅导览");
                            }else {
                                robotW.speak(TtsRequest.create(welcomWords,false));
                                utils.showDialog(MainActivity.this,welcomWords,"展厅模式","展厅导览");
                            }
                            //按钮点击监听
                            utils.setOnButtonClickListener(new AlertDialogUtils.OnButtonClickListener() {
                                @Override
                                public void onNegativeButtonClick(AlertDialog dialog) {
                                    startActivity(new Intent(MainActivity.this, ExhibitionModeActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onPositiveButtonClick(AlertDialog dialog) {
                                    Boolean intentCode=true;
                                    locations=new ArrayList<>();
                                    saveLocation=new ListDataSave(MainActivity.this,"location");
                                    locations=saveLocation.getLocation("location_order");
                                    if (robot.getLocations().size()==1){
                                        robot.speak(TtsRequest.create("请先添加位置",false));
                                    }else {
                                        if(locations==null||locations.size()==0){
                                            robot.speak(TtsRequest.create("位置未添加到展位中",false));
                                        }else {
                                            for (int i = 0; i < locations.size(); i++) {
                                                List<LocationBean> data = saveLocation.getDataList(locations.get(i));
                                                if (data.size() == 0) {
                                                    robot.speak(TtsRequest.create("请先给" + locations.get(i) + "设置展位信息", true));
                                                    intentCode=false;
                                                    startActivity(new Intent(MainActivity.this, SettingExhibitonActivity.class));
                                                }
                                            }
                                            if (intentCode==true){
                                                startActivity(new Intent(MainActivity.this, GuideActivity.class));
                                            }
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            });
                        }

                    }
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };
        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(),previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraId != null ? rgbCameraId : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
    }


    /**
     * 在{@link #previewView}第一次布局完成后，去除该监听，并且进行引擎和相机的初始化
     */
    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initCamera();
        }
    }

    private void unInitEngine() {

        if (afCode == 0) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }

    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        unInitEngine();
        super.onDestroy();
    }


    @Override
    public void onRobotReady(boolean b) {
        if (b){
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                Robot.getInstance().onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 注册监听事件
        robot.addOnRobotReadyListener(this);
        robot.addNlpListener(this);
    }

    @Override
    protected void onPause() {
        // 取消监听
        robot.removeOnRobotReadyListener(this);
        robot.removeNlpListener(this);
        super.onPause();
    }

    @Override
    public void onNlpCompleted(NlpResult nlpResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.work_btn:
                startActivity(new Intent(MainActivity.this,WorkModelActivity.class));
                finish();
                break;
            case R.id.exhibition_btn:
                startActivity(new Intent(MainActivity.this,ExhibitionModeActivity.class));
                finish();
                break;
            case R.id.imageView2:
                ListDataSave save=new ListDataSave(this,"location");
                //默认回到导览的初始点
                if (save.getLocation("location_order").size()!=0){
                    robot.goTo(save.getLocation("location_order").get(0));
                }else {
                    robot.goTo("home base");
                }
                break;
        }
    }

}
