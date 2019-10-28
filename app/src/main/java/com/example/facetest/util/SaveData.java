package com.example.facetest.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
import java.util.TreeSet;


public class SaveData {


    /**
     * Description: 设置密码
     * key 表名
     */
    public static void setGuideData(Context context,String key, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(key, data);
        editor.apply();
    }


    //取出密码
    public static String getGuideData(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }


    //添加展厅数据(多条)
    public static void setExhData(Context context,String key, Set<String> datas) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putStringSet(key,datas);
        editor.apply();
    }

    //取出展厅数据
    public static String[]  getExhData(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        Set<String> datas=new TreeSet<>();
        datas=sp.getStringSet(key,datas);
        String[] data = null;
        if (datas.size()>0){
            data = datas.toArray(new String[datas.size()]); //将SET转换为数组
        }
        return data;
    }

}
