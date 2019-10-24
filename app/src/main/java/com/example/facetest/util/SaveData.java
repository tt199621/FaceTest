package com.example.facetest.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shinelon-LJL on 2019/9/19
 */
public class SaveData {


    /**
     * Description: 设置数据
     * key 表名
     */
    public static void setGuideData(Context context,String key, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(key, data);
        editor.apply();
    }

    //取出数据
    public static String getGuideData(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

}
