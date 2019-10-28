package com.example.facetest.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.facetest.R;
import com.example.facetest.activity.ExhibitionDetailsActivity;
import com.example.facetest.bean.ExhibitionBean;
import com.robotemi.sdk.Robot;

import java.util.List;

public class ExhibitonAdapter extends RecyclerView.Adapter<ExhibitonAdapter.ViewHolder> {

    Context context;
    List<ExhibitionBean> list;
    Robot robot=Robot.getInstance();
    public static String imgSrc,imgProduce,speak,location;

    public ExhibitonAdapter(Context context, List<ExhibitionBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exhibition_details,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.location_exh_details.setText(list.get(i).getLocation()+"");//展位名称
        Log.d("exhSrc",""+list.get(i).getSrc());
        Glide.with(context).load(list.get(i).getSrc()).placeholder(R.mipmap.ic_launcher).into(holder.image_item_exh);//展位图片
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("exhSrc","speak: "+list.get(i).getSpeak());
                /*if (list.get(i).getSpeak()!=null||!list.get(i).getSpeak().equals("")){
                    robot.speak(TtsRequest.create(list.get(i).getSpeak(),false));//说介绍词
                }else {
                    robot.speak(TtsRequest.create("还未设置介绍词",false));
                }*/
                if (list.get(i).getSrc() != null) {
                    imgSrc=list.get(i).getSrc();//图片路径
                }else {
                    imgSrc="";//图片路径
                }

                if (list.get(i).getText() != null) {
                    imgProduce=list.get(i).getText();//文字介绍
                }else {
                    imgProduce="未设置介绍内容";//文字介绍
                }

                if (list.get(i).getText() != null) {
                    speak=list.get(i).getSpeak();//语音介绍
                }else {
                    speak="";//语音介绍
                }

                location=list.get(i).getLocation();

                /*点击列表跳转到详情页*/
                context.startActivity(new Intent(context, ExhibitionDetailsActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View thisView;
        TextView location_exh_details;//展位名称
        ImageView image_item_exh;//展位图片
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thisView=itemView;
            location_exh_details=itemView.findViewById(R.id.location_exh_details);
            image_item_exh=itemView.findViewById(R.id.image_item_exh);
        }
    }
}
