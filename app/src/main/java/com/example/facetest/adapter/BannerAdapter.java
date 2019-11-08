package com.example.facetest.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.facetest.R;
import com.example.facetest.activity_setting.EditBannerActivity;
import com.example.facetest.bean.BannerBean;
import com.example.facetest.util.ListDataSave;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    Context context;
    List<BannerBean> list;//路径集合
    ListDataSave save;
    public static int code=-1;
    public static String src="";

    public BannerAdapter(Context context,List<BannerBean> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        save=new ListDataSave(context,"bannerDB");
        holder.banner_src.setText(""+list.get(i).getSrc());//路径
        Glide.with(context).load(list.get(i).getSrc()).into(holder.banner_img);//图片
        //短按修改
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code=list.get(i).getCode();
                src=list.get(i).getSrc();
                context.startActivity(new Intent(context, EditBannerActivity.class));
            }
        });
        //长按删除
        holder.thisView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.waring);
                builder.setTitle("删除该图片？");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int x) {
                        list.remove(i);
                        save.setBanners("banner",list);
                        notifyDataSetChanged();
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View thisView;
        TextView banner_src;
        ImageView banner_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thisView=itemView;
            banner_src=itemView.findViewById(R.id.banner_src);
            banner_img=itemView.findViewById(R.id.banner_img);
        }
    }
}