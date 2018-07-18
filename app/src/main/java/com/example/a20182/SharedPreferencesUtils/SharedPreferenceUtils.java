package com.example.a20182.SharedPreferencesUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/7/10.
 * 工具类,使用sharedpreference将routeHistory和locationBeans保存到本地
 * 使得应用重启后仍可读取起始点和终点搜索历史
 */

public class SharedPreferenceUtils {


    //用于加载RouteHistory
    public static boolean saveText(Context mContext, List<String[]> data) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("RouteHistoryList", MODE_PRIVATE).edit();
        editor.putInt("Status_size",data.size()); /*sKey is an array*/
        int size = data.size();
        System.out.println("*****在SharedPreferenceUtils中saveText获取来的size是 " + size);

        for(int i = 0; i < data.size(); i++) {
            editor.putString("starttext"+i, data.get(i)[0]);
            editor.putString("endtext"+i, data.get(i)[1]);
        }

        return editor.commit();
    }

    public static ArrayList<String[]> loadText(Context mContext) {
        SharedPreferences mSharedPreference1= mContext.getSharedPreferences("RouteHistoryList",MODE_PRIVATE);
        ArrayList<String[]> RouteHistory = new ArrayList<>();
        int size = mSharedPreference1.getInt("Status_size", 0);
        System.out.println("*****在SharedPreferenceUtils中loadText获取来的size是 " + size);

        for(int i = 0; i < size; i++) {
            String[] temp = {mSharedPreference1.getString("starttext"+i, null)
                    ,mSharedPreference1.getString("endtext"+i, null)};
            RouteHistory.add(temp);
        }
        return RouteHistory;
    }

    public static boolean savePos(Context mContext, List<String[]> data) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("latLonPoints", MODE_PRIVATE).edit();
        editor.putInt("Status_size2",data.size()); /*sKey is an array*/
        int size = data.size();

        System.out.println("*****在SharedPreferenceUtils中savePos获取来的size是 " + size);
        for(int i = 0; i < data.size(); i++) {
            editor.putString("start"+i, data.get(i)[0]);
            editor.putString("end"+i, data.get(i)[1]);
        }

        return editor.commit();
    }

    public static ArrayList<String[]> loadPos(Context mContext) {
        SharedPreferences mSharedPreference1= mContext.getSharedPreferences("latLonPoints",MODE_PRIVATE);
        ArrayList<String[]> RouteHistory = new ArrayList<>();
        int size = mSharedPreference1.getInt("Status_size2", 0);
        System.out.println("*****在SharedPreferenceUtils中loadPos获取来的size是 " + size);
        for(int i = 0; i < size; i++) {
            String[] temp = {mSharedPreference1.getString("start"+i, "0.0")
                    , mSharedPreference1.getString("end"+i, "0.0")};
            RouteHistory.add(temp);
        }
        return RouteHistory;
    }
}
