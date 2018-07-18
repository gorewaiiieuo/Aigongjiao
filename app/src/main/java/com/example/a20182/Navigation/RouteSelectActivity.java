package com.example.a20182.Navigation;
/*
* 此类用于显示路线规划的多种方案，
* 并支持点击返回显示路线图层
*/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.a20182.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteSelectActivity extends AppCompatActivity {

    private String start;
    private String end;
    private SimpleAdapter mAdapter;
    private ListView routeLv;
    private TextView startTv;
    private TextView endTv;

    private List<Map<String, String>> list_map = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_select);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        start = bundle.getString("start");
        end = bundle.getString("end");

        String[] oldRouteInfo = bundle.getStringArray("routeinfo");

        startTv = findViewById(R.id.startLine);
        endTv = findViewById(R.id.endLine);
        startTv.setText(start);
        endTv.setText(end);
        routeLv = findViewById(R.id.route_Lv);
        int length = oldRouteInfo.length;
        for(int i = 0; i < oldRouteInfo.length; i++){
            Map<String, String> item = new HashMap<>();
            item.put("route",oldRouteInfo[i].substring(0,oldRouteInfo[i].indexOf("\n")));
            item.put("routeinfo", oldRouteInfo[i].substring(oldRouteInfo[i].indexOf("\n") + 1));
            list_map.add(item);
        }
        mAdapter = new SimpleAdapter(this,
                list_map,
                R.layout.route_select_item,
                new String[]{"route","routeinfo"},
                new int[]{R.id.tv_route, R.id.tv_routeInfo});
        routeLv.setAdapter(mAdapter);
        routeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                //把要传递的数据封装至意图对象中
                data.putExtra("pos", position);
                //当前Activity销毁时，data这个意图就会传递给启动当前Activity的那个Activity
                setResult(1,data);
                data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
                data.addCategory(Intent.CATEGORY_HOME);
                //销毁当前Activity
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent data = new Intent();
        //把要传递的数据封装至意图对象中
        data.putExtra("pos", -1);
        //当前Activity销毁时，data这个意图就会传递给启动当前Activity的那个Activity
        setResult(1, data);
        data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
        data.addCategory(Intent.CATEGORY_HOME);
        finish();
    }
}
