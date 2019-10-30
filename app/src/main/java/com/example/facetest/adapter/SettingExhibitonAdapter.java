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
import com.example.facetest.activity_exhibition.SettingExhDetailsActivity;
import com.example.facetest.util.ListDataSave;
import com.robotemi.sdk.Robot;

import java.util.List;

public class SettingExhibitonAdapter extends RecyclerView.Adapter<SettingExhibitonAdapter.ViewHolder> {

    Context context;
    List<String> locations;
    Robot robot=Robot.getInstance();
    public static String exhName="";//展位名称
    private ListDataSave save;

    public SettingExhibitonAdapter(Context context, List<String> locations) {
        this.context = context;
        this.locations = locations;
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
        save=new ListDataSave(context,"location");
        holder.text_setting_exhbition.setText(locations.get(i)+"");
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exhName=locations.get(i);
                context.startActivity(new Intent(context, SettingExhDetailsActivity.class));
            }
        });
        holder.remove_exhbition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        ImageView remove_exhbition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thisView=itemView;
            text_setting_exhbition=itemView.findViewById(R.id.text_setting_exhbition);
            remove_exhbition=itemView.findViewById(R.id.remove_exhbition);
        }
    }
}
