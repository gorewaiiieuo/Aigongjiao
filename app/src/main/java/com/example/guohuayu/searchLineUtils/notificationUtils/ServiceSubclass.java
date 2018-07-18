package com.example.guohuayu.searchLineUtils.notificationUtils;

/*
* 子服务类
* 用于后台弹窗的实现
 */

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;

import android.view.WindowManager;

public class ServiceSubclass extends Service {

    public static boolean stopShark = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    public void onCreate() {
        System.out.println("---> Service onCreate()");
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("---> Service onStart()");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("---> Service onStartCommand()");
        for (int i = 0; i < 10000; i++) {
            if (i==9527) {
                Builder builder=new Builder(this);
                builder.setTitle("提示");
                builder.setMessage("车已到站！");

                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MediaPlayUtil.stopRing();
                        stopShark = true;

                    }
                });
                Dialog dialog=builder.create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("---> Service onDestroy()");
    }

}