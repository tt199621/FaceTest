package com.example.facetest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.facetest.bean.LocationBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ListDataSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    //preferenceName=数据库名
    public ListDataSave(Context mContext, String preferenceName) {
        Log.d("exhName","DBname:"+ preferenceName);
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     * @param tag=key
     * @param datalist
     */
    public  void setDataList(String tag, List<LocationBean> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        Log.d("exhName","setKey:"+ tag);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取List
     * @param tag=key
     * @return
     */
    public  List<LocationBean> getDataList(String tag) {
        List<LocationBean> datalist=new ArrayList<>();
        Log.d("exhName","getKey:"+ tag);
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<LocationBean>>() {
        }.getType());
        return datalist;

    }
}

