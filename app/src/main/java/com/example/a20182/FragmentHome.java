package com.example.a20182;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteBusWalkItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.a20182.Navigation.BusRouteOverlay;
import com.example.a20182.Navigation.GeoCoderUtil;
import com.example.a20182.Navigation.InputTipTask;
import com.example.a20182.Navigation.LocationBean;
import com.example.a20182.Navigation.PoiSearchTask;
import com.example.a20182.Navigation.RouteSelectActivity;
import com.example.a20182.Navigation.SearchAdapter;
//import com.example.a20182.RecyclerUtils.RouteSelectHistory;
import com.example.a20182.historyRecyclerAdapater.RouteSelectHistoryAdapter;
import com.example.a20182.routeDisplayAdapter.RecyclerViewUtils.MyDecoration;
import com.example.a20182.routeDisplayAdapter.RecyclerViewUtils.MyLayoutManager;
import com.example.a20182.routeDisplayAdapter.routeDisaplayAdapter;
import com.example.a20182.SharedPreferencesUtils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.drm.DrmManagerClient.ERROR_NONE;

/**
 * A simple {@link Fragment} subclass.
 * app主页面，主要实现以下功能：
 * 1.地图显示以及定位
 * 2.地图显示所搜的地点，点击Marker跳转到路线规划方案界面
 * 3.起点终点搜索路线规划
 * 4.在搜索页面显示历史搜索
 */

public class FragmentHome extends Fragment
        implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener, AMap.OnMarkerClickListener{//, RouteSelectHistory.OnItemClickListener
    private View mView;

    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption option;

    private AutoCompleteTextView autotext;
    private ImageView buttonDelete;
    private ListView poi_lv;
    private RecyclerView selected_Lv2;
    private SearchAdapter adapter;
    private Marker searchMarker;
    private AutoCompleteTextView startTv;
    private AutoCompleteTextView endTv;
    private String starttext;
    private String endtext;
    private LatLonPoint start;
    private LatLonPoint end;
    private LatLonPoint myLoc;
    private List<BusPath> busPathList;
    private ArrayList<String[]> routeContent;
    private String[] RouteInfo;

    private View pop;
    private PopupWindow popupWindow;
    private boolean mIsShowing = false;
    private ImageView dropDown;

    private View pop2;
    private PopupWindow popupWindow2;
    private boolean isRotate = false;
    private ImageView dropDown2;
    private int pos;
    private BusRouteOverlay busRouteOverlay;
    private RecyclerView rv_searchHistory;

    private List<Marker> markerList = new ArrayList<Marker>();
    private Map<Marker,LocationBean> markerMap = new HashMap<>();

    private String oldText;

    public static String citycode;
    public static String adcode;

    private List<String> isSamestr = new ArrayList<>();

    private RouteSelectHistoryAdapter routeSelectAdapter;
    private ArrayList<String[]> RouteHistory = new ArrayList<>();
    private ArrayList<com.amap.api.services.core.LatLonPoint[]> latLonPoints = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_home, null);

        mapView = mView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        mView.findViewById(R.id.route_bus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow2!=null) popupWindow2.dismiss();
                popupSearch();
                HideKeyboard(view);
            }
        });

        mView.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow2!=null) popupWindow2.dismiss();
                toRouteList();
            }
        });

        autotext = (AutoCompleteTextView)mView.findViewById(R.id.search_edit_text);
        autotext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence!=null){
                    buttonDelete.setVisibility(View.VISIBLE);
                    InputTipTask.getInstance(mView.getContext()).setAdapter(autotext).searchTips(charSequence.toString().trim(), "");
                }else {
                    buttonDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

        });
        autotext.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                LocationBean loc = InputTipTask.getInstance(mView.getContext()).getBean().get(position);
                LatLng Position = new LatLng(loc.getLat(),loc.getLon());

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Position, 15));
                addMarker(aMap, loc);
            }
        });
        autotext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow2!=null) popupWindow2.dismiss();
            }
        });

        buttonDelete = mView.findViewById(R.id.search_edit_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autotext.setText("");
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mView;
    }

    @Override
    public void onStart(){
        super.onStart();

        init();
        if(busRouteOverlay!=null)busRouteOverlay.removeFromMap();

        pos = MainFrameActivity.posResult;
        if(pos!=-1)
        {
            isRotate = false;
            Log.i("--output2",""+pos);
            if(pos>=0)popupRouteRun();
            MainFrameActivity.posResult = -1;
        }
/*
        if(list!=null) {
            BusLineOverlay busLineOverlay = new BusLineOverlay(this, aMap, list);
            busLineOverlay.removeFromMap();
            busLineOverlay.addToMap();
            busLineOverlay.zoomToSpan();
        }
*/
    }



    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        //添加地图marker点击事件
        aMap.setOnMarkerClickListener(this);
    }

    //定位回调接口
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == ERROR_NONE) {
            mListener.onLocationChanged(aMapLocation);
            citycode = aMapLocation.getCityCode();
            adcode = aMapLocation.getAdCode();
            myLoc = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        }
    }

    //激活定位
    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

        if(locationClient ==null)
        {
            locationClient = new AMapLocationClient(mView.getContext());
        }
        if(option == null)
        {
            option = new AMapLocationClientOption();
            //设置GPS定位优先，即使设置高精度定位模式，它也会优先GPS在室内定位很差，最好不要设置，就默认的也就是false;
            option.setGpsFirst(false);//+++++
            //高精度定位模式
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        }
        //设置定位，onLocationChanged就是这个接口的方法
        locationClient.setLocationListener(this);
        locationClient.setLocationOption(option);
        //开始定位
        locationClient.startLocation();
    }

    @Override
    public void deactivate() {
        if(locationClient.isStarted())
            locationClient.stopLocation();
        locationClient = null;
        mListener=null;
    }

    private ProgressDialog dialog;

    private void startBusRoute() {
        isSamestr = new ArrayList<>();
        isSameScale(start);
        isSameScale(end);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        dialog.dismiss();
        if (i == 1000) {
            busPathList = busRouteResult.getPaths();
            int l = busPathList.size();
            RouteInfo = new String[l];
            routeContent = new ArrayList<String[]>();
            String busLine="";

            for (int n=0;n<l;n++) {
                busLine="";
                BusPath busPath = busPathList.get(n);
                List<BusStep> busSteps = busPath.getSteps();
                String[] routeInfo = new String[20];
                // 分别获取公交线路距离，步行距离，整个线路距离
                int index = 0;
                for (BusStep busStep : busSteps) {

                    if (busStep.getWalk() != null) {
                        RouteBusWalkItem walkPath = busStep.getWalk();
                        if(index == 0){
                            routeInfo[index] = "起点" + "\n" +
                                    "需要步行大约"
                                    + Math.round(walkPath.getDuration() / 60)
                                    + "分钟，步行" + walkPath.getDistance() + "米\n";
//                                    + walkPath.getDestination() + "\n";
                            index++;
                        }else {
                            routeInfo[index] = walkPath.getOrigin() + "\n" +
                                    Math.round(walkPath.getDuration() / 60)
                                    + "分钟，步行约" + walkPath.getDistance() + "米\n"
                                    + walkPath.getDestination() + "\n";

                            index++;
                        }
                    }
                    if (busStep.getBusLine() != null) {
                        if(busLine.equals(""))busLine=delBrac(busStep.getBusLine().getBusLineName());
                        else busLine = busLine + " → "+delBrac(busStep.getBusLine().getBusLineName());

                        RouteBusLineItem buslineItemForTest = busStep.getBusLine();
                        RouteBusLineItem busLineItem = busStep.getBusLine();
                        //获取除起始点和终点外经过的中间站
                        String pastStation = "";
                        for(int m = 0; m < busLineItem.getPassStationNum(); m++){
                            pastStation += busLineItem.getPassStations().get(m).getBusStationName().toString();
                            if(m != busLineItem.getPassStationNum() - 1){
                                pastStation += " ";
                            }
                        }
                        routeInfo[index] = busLineItem.getDepartureBusStation().getBusStationName() + "\n"
                                + busLineItem.getBusLineName() + ", 经过" + busLineItem.getPassStationNum() + "站\n"
                                + pastStation + "\n"
                                + busLineItem.getArrivalBusStation().getBusStationName() + "\n";
                        index++;
                    }

                }

                //String firstLine = busPath.getSteps().get(0).getBusLine().getBusLineName();
                int time = Math.round(busPath.getDuration()/60);
                String upStation = busPath.getSteps().get(0).getBusLine().getDepartureBusStation().getBusStationName();
                float distance =  Math.round(busPath.getDistance()/1000);
                float walkDistance = busPath.getWalkDistance();
                float cost = busPath.getCost();

                RouteInfo[n] = busLine + "\n" +
                        time+"分钟·"+distance+"公里·步行"+walkDistance+"米·"+cost+"元";
                if(routeInfo!=null) routeContent.add(routeInfo);
            }

            toRouteList();

        } else {
            //Log.e(TAG, "onBusRouteSearched: 路线规划失败");
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
    }

    /**
     * 公交规划线路
     */
    private void setBusRoute(BusPath busPath, LatLonPoint start, LatLonPoint end) {

        if(busRouteOverlay!=null)busRouteOverlay.removeFromMap();//去掉DriveLineOverlay上的线段和标记。
        busRouteOverlay = new BusRouteOverlay(mView.getContext(), aMap, busPath, start, end);
        busRouteOverlay.addToMap(); //添加驾车路线添加到地图上显示。
        busRouteOverlay.zoomToSpan();//移动镜头到当前的视角。
        busRouteOverlay.setNodeIconVisibility(true);//是否显示路段节点图标
    }

    public void addMarker(AMap aMap, LocationBean loc) {
        if(searchMarker!=null)
        {
            searchMarker.remove();
            markerMap.clear();
        }
        if (aMap != null) {
            LatLng latlng = new LatLng(loc.getLat(),loc.getLon());
            searchMarker = aMap.addMarker((new MarkerOptions())
                    .position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluemarker))
                    .title("要去这里吗?"));
            markerMap.put(searchMarker, loc);
            searchMarker.setVisible(true);
        }
    }

    public void dismiss(){
        if(popupWindow != null &&mIsShowing){
            popupWindow.dismiss();
            mIsShowing = false;
        }
    }

    public void popupSearch(){
        if(popupWindow == null){
            initPopupSearch();
        }
        if(!mIsShowing){
            popupWindow.showAtLocation(mView.findViewById(R.id.layout_main), Gravity.BOTTOM,0,0);
            mIsShowing = true;
            //poi_lv.setAdapter(null);
        }
    }

    private void initPopupSearch() {
        pop = View.inflate(mView.getContext(), R.layout.search_popup, null);
        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        mIsShowing = false;
        start = myLoc;
        starttext = "我的位置";
        end = null;
        endtext = "";

        dropDown = pop.findViewById(R.id.dismissIv);
        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        poi_lv = pop.findViewById(R.id.poi_lv);
        poi_lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if(startTv.isFocused()) {
                    LocationBean startloc = PoiSearchTask.getInstance(pop.getContext()).getLocBuffer().get(position);
                    start = new LatLonPoint(startloc.getLat(),startloc.getLon());
                    //Log.i("ok?",start.toString());
                    //startTv.clearFocus();
                    starttext = startloc.getTitle();
                    startTv.setText(starttext);
                }
                if(endTv.isFocused()) {
                    LocationBean endloc = PoiSearchTask.getInstance(pop.getContext()).getLocBuffer().get(position);
                    end = new LatLonPoint(endloc.getLat(),endloc.getLon());
                    //endTv.clearFocus();
                    endtext = endloc.getTitle();
                    endTv.setText(endtext);
                }
                if(!startTv.getText().toString().equals("")&&start!=null&&
                        !endTv.getText().toString().equals("")&&end!=null){
                    startBusRoute();
                }

            }
        });

        startTv = pop.findViewById(R.id.start_Tv);
        startTv.setText("我的位置");
        startTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = startTv.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(startTv.isFocused()) {
                    //task = PoiSearchTask.getInstance(pop.getContext()).setData(adapter, poi_lv, myLoc);
                    PoiSearchTask.getInstance(pop.getContext()).setData(adapter, poi_lv, myLoc).onSearch(startTv.getText().toString().trim(), citycode);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(PoiSearchTask.getInstance(pop.getContext()).setData(adapter, poi_lv, myLoc).getLocBuffer()!=null
                        &&!oldText.contains(startTv.getText().toString())){
                    KeyBoard(startTv,"close");
                }
            }
        });

        endTv = pop.findViewById(R.id.end_Tv);
        endTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = endTv.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(endTv.isFocused()) {
                    PoiSearchTask.getInstance(pop.getContext()).setData(adapter, poi_lv).onSearch(endTv.getText().toString().trim(), citycode);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(PoiSearchTask.getInstance(pop.getContext()).setData(adapter, poi_lv, myLoc).getLocBuffer()!=null
                        &&!oldText.contains(endTv.getText().toString())){
                    KeyBoard(endTv,"close");
                }
            }
        });

        pop.findViewById(R.id.transStartToEnd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start!=null&&end!=null&&!starttext.equals("")&&!endtext.equals("")){
                    LatLonPoint tempLatLon = start;
                    start = end;
                    end = tempLatLon;

                    String text = starttext;
                    starttext = endtext;
                    endtext = text;

                    startTv.setText(starttext);
                    endTv.setText(endtext);

                    startBusRoute();
                }
            }
        });


        RouteHistory = SharedPreferenceUtils.loadText(getContext());
        int length = RouteHistory.size();
        if(length > 0){
            starttext = RouteHistory.get(length - 1)[0];
            endtext = RouteHistory.get(length - 1)[1];
            latLonPoints.clear();
            ArrayList<String[]> convert = SharedPreferenceUtils.loadPos(getContext());
            for(int i = 0; i < length; i++){
                com.amap.api.services.core.LatLonPoint start =
                        new com.amap.api.services.core.LatLonPoint(Double.parseDouble(convert.get(i)[0].substring(0, convert.get(i)[0].indexOf(","))),
                                Double.parseDouble(convert.get(i)[0].substring(convert.get(i)[0].indexOf(",") + 1)));
                com.amap.api.services.core.LatLonPoint end =
                        new com.amap.api.services.core.LatLonPoint(Double.parseDouble(convert.get(i)[1].substring(0, convert.get(i)[1].indexOf(","))),
                                Double.parseDouble(convert.get(i)[1].substring(convert.get(i)[1].indexOf(",") + 1)));
                com.amap.api.services.core.LatLonPoint[] latLonPoint = {start, end};
                latLonPoints.add(latLonPoint);
            }
        }
        rv_searchHistory = pop.findViewById(R.id.rv_searchHistory1);
        RouteSelectHistoryAdapter adapter = new RouteSelectHistoryAdapter(RouteHistory, latLonPoints);/****************/
        MyLayoutManager manager = new MyLayoutManager(getContext());
        manager.setOrientation(LinearLayout.VERTICAL);
        rv_searchHistory.setLayoutManager(manager);
        rv_searchHistory.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration decoration = new MyDecoration(getContext());
        rv_searchHistory.addItemDecoration(decoration);
        adapter.setItemClickListener(new RouteSelectHistoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                start = latLonPoints.get(position)[0];
                end = latLonPoints.get(position)[1];

                startBusRoute();
            }
        });

        rv_searchHistory.setAdapter(adapter);
    }

    private void popupRouteRun(){

        BusPath busPath = busPathList.get(pos);
        setBusRoute(busPath, start, end);

        pop2 = View.inflate(mView.getContext(), R.layout.route_popup, null);

        completeType();

        dropDown2 = pop2.findViewById(R.id.dismissRv);
        dropDown2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dropDown2.setPivotX(dropDown2.getWidth() / 2);
                dropDown2.setPivotY(dropDown2.getHeight() / 2);//支点在图片中心

                isRotate=!isRotate;
                if(isRotate){
                    simplyType();
                }else{
                    dropDown2.setRotation(0);
                    completeType();
                }

            }
        });
    }

    private void simplyType(){
        if(popupWindow2!=null) popupWindow2.dismiss();

        pop2.setPadding(0,0,0,0);
        popupWindow2 = new PopupWindow(pop2, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow2.setTouchable(true);
        popupWindow2.setFocusable(false);
        popupWindow2.setOutsideTouchable(false);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow2.setAnimationStyle(R.style.anim_menu_bottombar);

        popupWindow2.showAtLocation(mView.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);

        String[] simpleInfo = {RouteInfo[pos]};
        selected_Lv2 = pop2.findViewById(R.id.selected_Lv2);
        routeDisaplayAdapter adapter = new routeDisaplayAdapter(simpleInfo);
        MyLayoutManager manager = new MyLayoutManager(getContext());
        manager.setOrientation(LinearLayout.VERTICAL);
        selected_Lv2.setLayoutManager(manager);
        selected_Lv2.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration decoration = new MyDecoration(getContext());
        selected_Lv2.addItemDecoration(decoration);

        selected_Lv2.setAdapter(adapter);

        dropDown2.setRotation(180);
    }

    private void completeType(){
        if(popupWindow2!=null) popupWindow2.dismiss();

        pop2.setPadding(0,dp2px(pop2.getContext(),240),0,0);
        popupWindow2 = new PopupWindow(pop2, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow2.setTouchable(true);
        popupWindow2.setFocusable(false);
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow2.setAnimationStyle(R.style.anim_menu_bottombar);

        popupWindow2.showAtLocation(mView.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);

        String[] route = routeContent.get(pos);

        //排除null否则报错----------------------------
        int index = 0;
        while(route[index]!=null)index++;
        String[] realRoute = new String[index];
        index = 0;
        for(String str:route){
            if(str!=null) {
                //Log.i("route", str);
                realRoute[index] = str;
                index++;
            }
        }
        //---------------------------------------------

        selected_Lv2 = pop2.findViewById(R.id.selected_Lv2);
        final routeDisaplayAdapter adapter = new routeDisaplayAdapter(realRoute);
        adapter.setHideOrShowCallBack(new routeDisaplayAdapter.HideOrShowCallBack() {
            @Override
            public void hide() {
                adapter.setHideList();
            }

            @Override
            public void open() {
                adapter.setOpenList();
            }
        });
        selected_Lv2.setLayoutManager(new LinearLayoutManager(getContext()));
        selected_Lv2.setAdapter(adapter);
    }

    private void toRouteList(){
        if(RouteInfo!=null) {
            Intent intent = new Intent(getActivity(), RouteSelectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("start", starttext);
            bundle.putString("end", endtext);

            bundle.putStringArray("routeinfo", RouteInfo);
            intent.putExtras(bundle);
            startActivityForResult(intent, 10);
        }
    }

    private void setPoint(int pos)
    {
        LocationBean loc = InputTipTask.getInstance(mView.getContext()).getBean().get(pos);
        if(loc!=null) {
            LatLng posLatLng = new LatLng(loc.getLat(), loc.getLon());

            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posLatLng, 15));
            addMarker(aMap, loc);
        }
    }

    public static void HideKeyboard(View v){
        InputMethodManager imm = ( InputMethodManager) v.getContext( ).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken() , 0 );
        }
    }

    //强制显示或者关闭系统键盘
    private void KeyBoard(final EditText txtSearchKey, final String status)
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status.equals("open")) {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, 300);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static String delBrac(String str)//(())
    {
        return str.replace(str.substring(str.indexOf("("),str.lastIndexOf(")")+1),"");
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if(aMap != null){
            LocationBean loc = markerMap.get(marker);
            start = myLoc;
            end = new LatLonPoint(loc.getLat(),loc.getLon());
            starttext = "我的位置";
            endtext = autotext.getText().toString();
            startBusRoute();
        }
        return false;
    }


    private void startQuery(){
        if(isSamestr.size()==2)
        {
            if(!isSamestr.get(0).equals(citycode)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("起始点不在市内！")
                        .setCancelable(false)
                        .setPositiveButton("确定",null);
                AlertDialog alert = builder.create();
                alert.show();
            }
            if(!isSamestr.get(1).equals(citycode)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("目的地不在市内！")
                        .setCancelable(false)
                        .setPositiveButton("确定", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
            if(isSamestr.get(0).equals(citycode) && isSamestr.get(1).equals(citycode)){
                dismiss();
                if (aMap == null) {
                    aMap = mapView.getMap();
                    //setUpMap();
                }
                //清除防止重复显示
                aMap.clear();
                if (dialog == null) {
                    dialog = new ProgressDialog(mView.getContext());
                }
                dialog.setMessage("正在规划路线，请稍后...");
                dialog.show();

                RouteSearch routeSearch = new RouteSearch(mView.getContext());

                //起始点与目的经纬度
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start, end);

                //添加信息到RouteHistory中并防止添加重复内容
                boolean flag = true;
                String[] strings = {startTv.getText().toString(), endTv.getText().toString()};
                for (int i = 0; i < RouteHistory.size(); i++) {
                    if ((RouteHistory.get(i)[0].equals(strings[0])) && (RouteHistory.get(i)[1].equals(strings[1]))) {
                        flag = false;
                        break;
                    }
                }
                //if(flag && !endTv.getText().toString().trim().equals("")) {//防止重复
                if(flag && !endTv.getText().toString().trim().equals("")) {
                    //将公交规划起始点搜索添加到记录
                    RouteHistory.add(strings);
                    latLonPoints.add(new com.amap.api.services.core.LatLonPoint[]{start, end});
                }

                //公交：fromAndTo包含路径规划的起点和终点，RouteSearch.BusLeaseWalk表示公交查询模式
                //第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算,1表示计算
                RouteSearch.BusRouteQuery query1 = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BUS_LEASE_WALK, citycode, 1);//本地区号
                routeSearch.calculateBusRouteAsyn(query1);
                routeSearch.setRouteSearchListener(FragmentHome.this);
            }
        }
    }

    private void isSameScale(LatLonPoint point){

        GeoCoderUtil.getInstance(this.getContext()).geoAddress(point, new GeoCoderUtil.GeoCoderAddressListener() {
            @Override
            public void onAddressResult(String result) {
                isSamestr.add(result.replaceAll("[^0-9]",""));
                startQuery();
            }
        });
    }

    //将RouteHistory和latLonPoints存储 以备应用重启重新导入
    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtils.saveText(getContext(),RouteHistory);

        List<String[]> convert = new ArrayList<>();
        int length = latLonPoints.size();
        for(int i = 0; i < length; i++){
            String[] temp = {latLonPoints.get(i)[0].toString(),latLonPoints.get(i)[1].toString()};
            convert.add(temp);
        }
        SharedPreferenceUtils.savePos(getContext(),convert);
    }
}
