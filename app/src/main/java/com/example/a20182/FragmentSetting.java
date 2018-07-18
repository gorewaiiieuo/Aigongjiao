package com.example.a20182;

/*
*系统设置界面
* 设置提醒类型，搜索历史等信息
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.a20182.searchLineUtils.BusLineAdapter;
import com.example.a20182.searchLineUtils.BusLineInfo;
import com.example.a20182.searchLineUtils.notificationUtils.BusLineActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.a20182.searchLineUtils.BusLineAdapter.viewlist;
import static com.example.a20182.searchLineUtils.SearchLineActivity.markedBusline;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSetting extends Fragment {

    private View mView;

    private CheckBox clock_chb;
    private CheckBox shark_chb;
    private CheckBox window_chb;
    private CheckBox isEarlier_rb;
    private Button confirm_btn;


    public static boolean isClock ;
    public static boolean isShark;
    public static boolean isWindow = true;
    public static boolean isEarlier = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mView = inflater.inflate(R.layout.fragment_setting, container, false);
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView =  getActivity().getLayoutInflater().inflate(R.layout.fragment_setting, null);

        clock_chb = (CheckBox)mView.findViewById(R.id.chbClock);
        if(isClock)clock_chb.setChecked(true);
        else clock_chb.setChecked(false);
        clock_chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    isClock = true;
                }else{
                    isClock = false;
                }
            }
        });

        shark_chb = (CheckBox)mView.findViewById(R.id.chbShark);
        if(isShark)shark_chb.setChecked(true);
        else shark_chb.setChecked(false);
        shark_chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    isShark = true;
                }else{
                    isShark = false;
                }
            }
        });

        window_chb = (CheckBox)mView.findViewById(R.id.chbWindow);
        if(isWindow)window_chb.setChecked(true);
        else window_chb.setChecked(false);
        window_chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    isWindow = true;
                }else{
                    isWindow = false;
                }
            }
        });

        isEarlier_rb = mView.findViewById(R.id.isEarlier_rb);
        if(isEarlier)isEarlier_rb.setChecked(true);
        isEarlier_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isEarlier = true;
                }else{
                    isEarlier = false;
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart---------","here");
    }



}
