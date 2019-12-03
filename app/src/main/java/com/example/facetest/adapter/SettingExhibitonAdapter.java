package com.example.facetest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;
import com.example.facetest.activity_setting.SettingExhDetailsActivity;
import com.example.facetest.activity_setting.SettingExhibitonActivity;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingExhibitonAdapter extends RecyclerView.Adapter<SettingExhibitonAdapter.ViewHolder> {

    Context context;
    List<String> locations;
    Robot robot;
    public static String exhName="";//展位名称
    private ListDataSave save;
    List<Boolean> isClick;
    SettingExhibitonActivity activity;

    public SettingExhibitonAdapter(SettingExhibitonActivity activity,Context context, List<String> locations) {
        this.context = context;
        this.locations = locations;
        this.activity=activity;
        isClick=new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            isClick.add(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_exhibition,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        robot=Robot.getInstance();
        save=new ListDataSave(context,"location");
        holder.text_setting_exhbition.setText(locations.get(i)+"");
        if (isClick.get(i)==true){
            holder.text_setting_exhbition.setTextSize(40);
            holder.text_setting_exhbition.setTextColor(Color.RED);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        isClick.set(i,false);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            holder.text_setting_exhbition.setTextSize(30);
            holder.text_setting_exhbition.setTextColor(Color.WHITE);
        }
        holder.text_setting_exhbition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exhName=locations.get(i);
                context.startActivity(new Intent(context, SettingExhDetailsActivity.class));
            }
        });
        /*上移*/
        holder.exhbition_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i>0){
                    isClick.clear();
                    for (int i = 0; i < locations.size(); i++) {
                        isClick.add(false);
                    }
                    isClick.set(i-1,true);
                    Collections.swap(locations,i,i-1);
                    save.setLocation("location_order",locations);
                    notifyDataSetChanged();
                }else {
                    isClick.clear();
                    for (int i = 0; i < locations.size(); i++) {
                        isClick.add(false);
                    }
                    Toast.makeText(context, "已经到顶了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*下移*/
        holder.exhbition_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i<locations.size()-1){
                    isClick.clear();
                    for (int i = 0; i < locations.size(); i++) {
                        isClick.add(false);
                    }
                    isClick.set(i+1,true);
                    Collections.swap(locations,i,i+1);
                    save.setLocation("location_order",locations);
                    notifyDataSetChanged();
                }else {
                    isClick.clear();
                    for (int i = 0; i < locations.size(); i++) {
                        isClick.add(false);
                    }
                    Toast.makeText(context, "已经到底了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*删除*/
        holder.remove_exhbition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("locationSize",""+locations.get(i));
                robot.deleteLocation(locations.get(i));
                locations.remove(i);
                save.setLocation("location_order",locations);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View thisView;
        TextView text_setting_exhbition;
        ImageView remove_exhbition,exhbition_up,exhbition_down;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thisView=itemView;
            text_setting_exhbition=itemView.findViewById(R.id.text_setting_exhbition);
            remove_exhbition=itemView.findViewById(R.id.remove_exhbition);
            exhbition_up=itemView.findViewById(R.id.exhbition_up);
            exhbition_down=itemView.findViewById(R.id.exhbition_down);
        }
    }

}
