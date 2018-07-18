package com.example.guohuayu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;
import com.amap.api.services.busline.BusStationSearch;
import com.amap.api.services.core.LatLonPoint;
import com.example.a20182.R;
import com.example.guohuayu.searchStationUtils.BusStationInfo;
import com.example.guohuayu.searchStationUtils.stationAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchStationActivity extends AppCompatActivity implements BusStationSearch.OnBusStationSearchListener {

    private TextView tv_station;;
    private String[] arrays;
    private stationAdapter mAdapter;
    private ListView busStationLv;
    private List<BusStationInfo> stationInfo;
    private boolean flag;
    private Button modebtn;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        flag = true;

        Intent intent = getIntent();
        name = intent.getStringExtra("stationName").replace("站","");

        tv_station = findViewById(R.id.tv_station);
        tv_station.setText(name);
        //System.out.println(name);
        setQuery(name);

        modebtn = findViewById(R.id.btn_modeset);
        modebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = !flag;
                if(flag)modebtn.setText(R.string.near);
                else modebtn.setText(R.string.far);
                setQuery(name);
            }
        });



    }

    @Override
    public void onBusStationSearched(BusStationResult busStationResult, int i) {
        if(i==1000){
            stationInfo = new ArrayList<>();
            List<BusStationItem> list = busStationResult.getBusStations();
            //Log.i("adcode",adcode);
            for(BusStationItem item:list)
            {
                if(item!=null&&!item.getBusStationName().contains("地铁站")) {
                    //Log.i("item-adcode",item.getAdCode());
                    if(flag||item.getAdCode().equals(FragmentHome.adcode)) {
                        List<String> strlist = new ArrayList<String>();
                        String str = item.getBusStationName();
                        LatLonPoint str1 = item.getLatLonPoint();
                        List<BusLineItem> list2 = item.getBusLineItems();
                        for (BusLineItem blitem : list2) {
                            strlist.add(FragmentHome.delBrac(blitem.getBusLineName()).replace("区间", ""));
                        }
                        BusStationInfo stationItem = new BusStationInfo(str, str1, removeDuplicate(strlist));
                        stationInfo.add(stationItem);
                    }
                }
            }
            busStationLv = findViewById(R.id.bs_lv);
            //arrays = strlist.toArray(new String[strlist.size()]);
            mAdapter = new stationAdapter(this, stationInfo);
            busStationLv.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
    }

    private void setQuery(String name){
        BusStationQuery busStationQuery = new BusStationQuery(name, FragmentHome.citycode);
        busStationQuery.setPageSize(15);
        busStationQuery.setPageNumber(0);
        BusStationSearch busStationSearch = new BusStationSearch(this, busStationQuery);
        busStationSearch.setOnBusStationSearchListener(this);// 设置查询结果的监听
        busStationSearch.searchBusStationAsyn();
    }

    public static List<String> removeDuplicate(List<String> li){
        List<String> list = new ArrayList<String>();//创建新的list
        for(int i=0; i<li.size(); i++){
            String str = li.get(i);  //获取传入集合对象的每一个元素
            if(!list.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list;  //返回集合
    }
}