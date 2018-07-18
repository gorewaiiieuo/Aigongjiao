package com.example.guohuayu;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a20182.R;
import com.example.guohuayu.searchLineUtils.BusLineInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.guohuayu.searchLineUtils.SearchLineActivity.markedBusline;

public class MainFrameActivity extends CheckPermissionsActivity implements View.OnClickListener {

    //用于控制mainactivity主界面显示内容
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    /**
     * 底部四个按钮
     */
    private LinearLayout mTabBtnHome;
    private LinearLayout mTabBtnQuery;
    private LinearLayout mTabBtnGame;
    private LinearLayout mTabBtnSettings;

    public static int posResult=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        initView();

        findViewById(R.id.iv_home).setOnClickListener(this);
        findViewById(R.id.iv_query).setOnClickListener(this);
        findViewById(R.id.iv_game).setOnClickListener(this);
        findViewById(R.id.iv_setting).setOnClickListener(this);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentIndex;

            @Override
            public void onPageSelected(int position) {
                resetTabBtn();
                //将所点击按钮图样设置为绿色被选图样 并获取所选按钮位置position
                switch (position) {
                    case 0:
                        ((ImageView) mTabBtnHome.findViewById(R.id.iv_home))
                                .setImageResource(R.drawable.home_pressed);
                        break;
                    case 1:
                        ((ImageView) mTabBtnQuery.findViewById(R.id.iv_query))
                                .setImageResource(R.drawable.plan_pressed);
                        break;
                    case 2:
                        ((ImageView) mTabBtnGame.findViewById(R.id.iv_game))
                                .setImageResource(R.drawable.plus_pressed);
                        break;
                    case 3:
                        ((ImageView) mTabBtnSettings.findViewById(R.id.iv_setting))
                                .setImageResource(R.drawable.setting_pressed);
                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        readData();

    }
    //将底部四个按钮图样初始化为未点击灰色图样
    protected void resetTabBtn() {

        ((ImageView) mTabBtnHome.findViewById(R.id.iv_home))
                .setImageResource(R.drawable.home_unpressed);
        ((ImageView) mTabBtnQuery.findViewById(R.id.iv_query))
                .setImageResource(R.drawable.plan_unpressed);
        ((ImageView) mTabBtnGame.findViewById(R.id.iv_game))
                .setImageResource(R.drawable.plus_unpressed);
        ((ImageView) mTabBtnSettings.findViewById(R.id.iv_setting))
                .setImageResource(R.drawable.setting_unpressed);
    }

    private void initView() {

        mTabBtnHome = (LinearLayout) findViewById(R.id.bottom_home);
        mTabBtnQuery = (LinearLayout) findViewById(R.id.bottom_query);
        mTabBtnGame = (LinearLayout) findViewById(R.id.bottom_game);
        mTabBtnSettings = (LinearLayout) findViewById(R.id.bottom_setting);

        //将四个fragment创建并添加到mFragments列表中开始监听

        FragmentHome fragmentHome = new FragmentHome();
        FragmentBus fragmentBus = new FragmentBus();/*******************************/
        FragmentGame fragmentPlan = new FragmentGame();
        FragmentSetting fragmentSetting = new FragmentSetting();
        mFragments.add(fragmentHome);
        mFragments.add(fragmentBus);
        mFragments.add(fragmentPlan);
        mFragments.add(fragmentSetting);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home:
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.iv_query:
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.iv_game:
                mViewPager.setCurrentItem(2, true);
                break;
            case R.id.iv_setting:
                mViewPager.setCurrentItem(3, true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                }
            }
        }

        if(resultCode == 1){
            posResult = data.getIntExtra("pos",-1);
            mViewPager.setCurrentItem(0);
        }
    }

    public void storageData(){
        FileOutputStream fos = null;
        try {
            fos=openFileOutput("busline.txt", MODE_PRIVATE);
            Gson gson = new Gson();
            String jsonstr = gson.toJson(markedBusline);
            fos.write(jsonstr.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readData(){
        FileInputStream fis=null;
        try {
            Gson gson = new Gson();
            fis=openFileInput("busline.txt");
            byte[] outByte=new byte[fis.available()];
            fis.read(outByte);
            if(!new String(outByte).equals("[]")&&!new String(outByte).equals("")){
                markedBusline = gson.fromJson(new String(outByte), new TypeToken<List<BusLineInfo>>(){}.getType());
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        storageData();
    }

}
