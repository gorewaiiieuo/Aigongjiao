package com.example.guohuayu.Navigation;

/*
*高德地图POI搜索封装类
* 采用单例模式
* 对输入字符串进行兴趣点搜索返回地理数据
 */

import android.content.Context;
import android.widget.ListView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

public class PoiSearchTask implements PoiSearch.OnPoiSearchListener {

    private static PoiSearchTask mInstance;
    private SearchAdapter mAdapter;
    private ListView mListView;
    private PoiSearch mSearch;
    private Context mContext;
    private ArrayList<LocationBean> locdatas;
    private LatLonPoint mMyLoc;


    private PoiSearchTask(Context context){
        this.mContext = context;
    }

    public static PoiSearchTask getInstance(Context context){
        if(mInstance == null){
            synchronized (PoiSearchTask.class) {
                if(mInstance == null){
                    mInstance = new PoiSearchTask(context);
                }
            }
        }
        return mInstance;
    }

    public PoiSearchTask setData(SearchAdapter adapter,ListView listview){
        this.mAdapter = adapter;
        this.mListView = listview;
        this.mMyLoc = null;
        return this;
    }

    public PoiSearchTask setData(SearchAdapter adapter,ListView listview,LatLonPoint myLoc){
        this.mAdapter = adapter;
        this.mListView = listview;
        this.mMyLoc = myLoc;
        return this;
    }

    public ArrayList<LocationBean> getLocBuffer(){
        return this.locdatas;
    }


    public void onSearch(String key, String city){
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        PoiSearch.Query query = new PoiSearch.Query(key, "", city);
        query.setPageSize(20);// 设置每页最多返回多少条poiItem
        query.setPageNum(0);
        mSearch = new PoiSearch(mContext, query);
        //mSearch.setBound(new SearchBound(new LatLonPoint(lat, lng), 2000));//设置周边搜索的中心点以及半径
        //设置异步监听
        mSearch.setOnPoiSearchListener(this);
        //查询POI异步接口
        mSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if(rCode == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {
                locdatas = new ArrayList<>();
                if(mMyLoc!=null) {
                    locdatas.add(new LocationBean(mMyLoc.getLongitude(), mMyLoc.getLatitude(), "我的位置", ""));
                }
                ArrayList<PoiItem> items = poiResult.getPois();
                for (PoiItem item : items) {
                    String title = item.getTitle();
                    LatLonPoint llp = item.getLatLonPoint();
                    double lon = llp.getLongitude();
                    double lat = llp.getLatitude();
                    String text = item.getSnippet();
                    locdatas.add(new LocationBean(lon, lat, title, text));
                }
                mAdapter= new SearchAdapter(mContext,locdatas);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
