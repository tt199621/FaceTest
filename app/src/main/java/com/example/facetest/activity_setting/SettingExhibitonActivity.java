package com.example.facetest.activity_setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facetest.R;
import com.example.facetest.adapter.SettingExhibitonAdapter;
import com.example.facetest.util.BaseDispatchTouchActivity;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 设置展厅页面
 */
public class SettingExhibitonActivity extends BaseDispatchTouchActivity implements View.OnClickListener {

    private ImageView finish_setting_exhibition;
    private RecyclerView recycler_setting_exhibition;
    private LinearLayoutManager linearLayoutManager;
    private Robot robot;
    private List<String> locations,defaultLocation;
    private ItemTouchHelper touchHelper;
    private SettingExhibitonAdapter adapter;
    private ListDataSave save;
    private Boolean saveCode=false;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_exhibiton);
        initView();
        initData();
        swipRefresh();
    }


    public void initView(){
        robot=Robot.getInstance();
        locations=new ArrayList<>();
        defaultLocation=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this);
        finish_setting_exhibition=findViewById(R.id.finish_setting_exhibition);
        recycler_setting_exhibition=findViewById(R.id.recycler_setting_exhibition);
        finish_setting_exhibition.setOnClickListener(this);
        save=new ListDataSave(SettingExhibitonActivity.this,"location");
    }

    public void initData(){
        //如果位置还未排序，默认用系统位置顺序
        if (save.getLocation("location_order")==null){
            defaultLocation=robot.getLocations();
            defaultLocation.remove("home base");
            if (defaultLocation.size()!=0){
                save.setLocation("location_order",defaultLocation);
            }else {
                robot.speak(TtsRequest.create("您还未添加展位",true));
            }
        }else {
            locations.clear();
            //如果不点击添加按钮，只读取存储的展位
            if (saveCode==true){
                defaultLocation=robot.getLocations();
                defaultLocation.remove("home base");
                save.setLocation("location_order",defaultLocation);
                locations=save.getLocation("location_order");
            }else {
                locations=save.getLocation("location_order");
            }
        }
        adapter=new SettingExhibitonAdapter(this,locations);
        recycler_setting_exhibition.setLayoutManager(linearLayoutManager);
        recycler_setting_exhibition.setAdapter(adapter);
        //设置item拖拽效果
        touchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //也就是说返回值是组合式的
                //makeMovementFlags (int dragFlags, int swipeFlags)，看下面的解释说明
                int swipFlag=0;
                //如果也监控左右方向的话，swipFlag=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                int dragflag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //等价于：0001&0010;多点触控标记触屏手指的顺序和个数也是这样标记哦
                return  makeMovementFlags(dragflag,swipFlag);

                /**
                 * 备注：由getMovementFlags可以联想到setMovementFlags，不过文档么有这个方法，但是：
                 * 有 makeMovementFlags (int dragFlags, int swipeFlags)
                 * 如果你想让item上下拖动和左边滑动删除，应该这样用： makeMovementFlags(UP | DOWN, LEFT)
                 */
                //拓展一下：如果只想上下的话：makeMovementFlags（UP | DOWN, 0）,标记方向的最小值1
            }

            /**
             * 拖动某个item的回调，在return前要更新item位置，调用notifyItemMoved（draggedPosition，targetPosition）
             * viewHolde:正在拖动item
             * target：要拖到的目标
             * @return true 表示消费事件
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(locations, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(locations, i, i - 1);
                    }
                }
                //保存调整顺序后的位置
                save.setLocation("location_order",locations);
                adapter.notifyItemMoved(fromPosition, toPosition);
/*                adapter=new SettingExhibitonAdapter(SettingExhibitonActivity.this,locations);
                recycler_setting_exhibition.setLayoutManager(linearLayoutManager);
                recycler_setting_exhibition.setAdapter(adapter);*/
                return true;
            }

            /**
             * 主要是做左右拖动的回调
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //暂不处理
            }

            /**
             * 官方文档如下：返回true 当前tiem可以被拖动到目标位置后，直接”落“在target上，其他的上面的tiem跟着“落”，
             * 所以要重写这个方法，不然只是拖动的tiem在动，target tiem不动，静止的
             * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
             * @param recyclerView
             * @param current
             * @param target
             * @return
             */
            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                return true;
            }


            /**
             * 官方文档说明如下：
             * Returns whether ItemTouchHelper should start a drag and drop operation if an item is long pressed.
             * 是否开启长按 拖动
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                //return true后，可以实现长按拖动排序和拖动动画了
                return true;
            }
        });
        //绑定recyclerview
        touchHelper.attachToRecyclerView(recycler_setting_exhibition);
    }


    //下拉刷新
    public void swipRefresh(){
        //下拉刷新
        swipeRefreshLayout=findViewById(R.id.swip_setting_exh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        swipeRefreshLayout.setProgressViewEndTarget (false,200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                saveCode=true;
                                initData();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish_setting_exhibition:
                finish();
                break;
        }
    }
}
