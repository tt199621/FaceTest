package com.example.facetest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.facetest.R;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.UserInfo;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Context context;
    Robot robot=Robot.getInstance();
    List<UserInfo> list=robot.getAllContact();

    public ContactsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.item_name.setText(""+list.get(i).getName());//名字
        Glide.with(context).load(list.get(i).getPicUrl()).placeholder(R.drawable.screensaver1).into(holder.item_image);//图片
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robot.startTelepresence(list.get(i).getName(),list.get(i).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View thisView;
        TextView item_name;
        ImageView item_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thisView=itemView;
            item_name=itemView.findViewById(R.id.item_name);
            item_image=itemView.findViewById(R.id.item_image);
        }
    }
}
