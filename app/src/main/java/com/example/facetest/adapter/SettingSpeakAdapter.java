package com.example.facetest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetest.R;
import com.example.facetest.activity_setting.SettingSpeakDetailsActivity;
import com.example.facetest.bean.SpeakBean;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;

import java.util.List;

public class SettingSpeakAdapter extends RecyclerView.Adapter<SettingSpeakAdapter.ViewHolder> {

    Context context;
    List<SpeakBean> list;
    Robot robot=Robot.getInstance();
    public static String questions;//问题
    private ListDataSave save;

    public SettingSpeakAdapter(Context context, List<SpeakBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_exhibition,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        save=new ListDataSave(context,"speakData");
        holder.text_setting_exhbition.setText(list.get(i).getQuestion()+"");
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions=list.get(i).getQuestion();
                context.startActivity(new Intent(context, SettingSpeakDetailsActivity.class));
            }
        });
        holder.remove_exhbition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(i);
                save.setSpeakData("speak",list);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View thisView;
        TextView text_setting_exhbition;
        ImageView remove_exhbition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thisView=itemView;
            text_setting_exhbition=itemView.findViewById(R.id.text_setting_exhbition);
            remove_exhbition=itemView.findViewById(R.id.remove_exhbition);//删除语音
        }
    }
}
