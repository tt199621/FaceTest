package com.example.facetest.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 设置口令
 */
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

}
