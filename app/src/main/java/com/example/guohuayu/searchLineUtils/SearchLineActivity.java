package com.example.guohuayu.searchLineUtils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.example.guohuayu.searchLineUtils.notificationUtils.BusLineActivity;
import com.example.a20182.R;
import com.example.guohuayu.FragmentHome;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SearchLineActivity extends AppCompatActivity implements BusLineSearch.OnBusLineSearchListener {
    private BusLineAdapter mAdapter;
    private ListView busLineLv;
    public static List<BusLineInfo> buslineInfo;
    public static List<BusLineInfo> markedBusline = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if(BusLineAdapter.viewlist!=null) BusLineAdapter.viewlist = new ArrayList<>();

        Intent intent = getIntent();
        String name = intent.getStringExtra("lineName").replace("路","");

        BusLineQuery busLineQuery = new BusLineQuery(name, BusLineQuery.SearchType.BY_LINE_NAME, FragmentHome.citycode);
        busLineQuery.setPageSize(15);
        busLineQuery.setPageNumber(0);
        BusLineSearch busLineSearch = new BusLineSearch(this, busLineQuery);
        busLineSearch.setOnBusLineSearchListener(this);
        busLineSearch.searchBusLineAsyn();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBusLineSearched(BusLineResult busLineResult, int i) {
        if(i==1000) {
            buslineInfo = new ArrayList<>();
            DateFormat bf = new SimpleDateFormat("HH:mm");
            List<BusLineItem> list = busLineResult.getBusLines();
            for (BusLineItem item : list) {
                if (item != null) {
                    String str = FragmentHome.delBrac(item.getBusLineName());
                    String str1 = item.getBusLineType();
                    String str2 = item.getOriginatingStation();
                    String str3 = item.getTerminalStation();
                    List<String> strlist = new ArrayList<String>();
                    List<BusStationItem> list2 = item.getBusStations();
                    int index = 0;
                    for(BusStationItem bsitem:list2){
                        if(bsitem.getBusStationName()!=null) {
                            strlist.add(bsitem.getBusStationName());
                            //Log.i("BusStations","stations3.add(new Station("+bsitem.getLatLonPoint().getLatitude()+","+bsitem.getLatLonPoint().getLongitude()+"));");
                            index++;
                        }
                    }
                    Date first = item.getFirstBusTime();
                    Date last = item.getLastBusTime();
                    String str4=first!=null?bf.format(first):"";
                    String str5=last!=null?bf.format(last):"";
                    String str6 = item.getBusLineId();

                    Log.i("BusLineId",str6);

                    BusLineInfo busline = new BusLineInfo(str,str1,str2,str3,strlist,str4,str5,str6,item);
                    if(buslineInfo.size()>0&&
                            busline.getBusLineName().equals(buslineInfo.get(buslineInfo.size()-1).getBusLineName())){
                        buslineInfo.get(buslineInfo.size()-1).setChild(busline);
                    }
                    else{
                        buslineInfo.add(busline);
                    }
                }

            }
            busLineLv = findViewById(R.id.bl_lv);
            mAdapter = new BusLineAdapter(this, buslineInfo);
            busLineLv.setAdapter(mAdapter);
            busLineLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(),BusLineActivity.class);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
            busLineLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean flag = false;
                    for(BusLineInfo item : markedBusline)
                    {
                        if(item.getBusLineId().equals(buslineInfo.get(position).getBusLineId())){
                            flag = true;
                        }
                    }
                    if(flag){
                        Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(markedBusline.size()>10)markedBusline.remove(0);
                        markedBusline.add(buslineInfo.get(position));
                        Toast.makeText(getApplicationContext(), "成功收藏" + buslineInfo.get(position).getBusLineName(), Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
            mAdapter.notifyDataSetChanged();

        }
    }
}
