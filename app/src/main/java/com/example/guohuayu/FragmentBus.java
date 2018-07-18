package com.example.guohuayu;

/*
*主Frame的公交查询页面
* 提供查询路线和站点并跳转的功能
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import com.example.a20182.R;
import com.example.guohuayu.historyRecyclerAdapater.SearchHistoryAdapter;
import com.example.guohuayu.searchLineUtils.SearchLineActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBus extends Fragment implements View.OnClickListener{

    private EditText et_queryLine, et_queryStation;
    private Button btn_queryLine, btn_queryStation;
    private RecyclerView rv;
    public final static FragmentBus fragmentBus = new FragmentBus();
    public static FragmentBus getInstanse(){
        return fragmentBus;
    }
    public static List<List<String>> mlistHistory = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    public List<List<String>> getList(){
        return mlistHistory;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus, container, false);

        et_queryLine = view.findViewById(R.id.et_queryLine);
        et_queryStation = view.findViewById(R.id.et_queryStation);
        btn_queryLine = view.findViewById(R.id.btn_queryLine);
        btn_queryStation = view.findViewById(R.id.btn_queryStation);

        btn_queryLine.setOnClickListener(this);
        btn_queryStation.setOnClickListener(this);

        rv = view.findViewById(R.id.rv_searchHistory);
        initRecyclerView();

        return view;

    }

    //查看mlistHistory内容
    @Override
    public void onResume() {
        super.onResume();
        //System.out.println(mlistHistory);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2 ,GridLayoutManager.VERTICAL,false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new SearchHistoryAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_queryLine:
                if(!et_queryLine.getText().toString().replaceAll("[^0-9]","").equals("")) {
                    Intent intentLine = new Intent(getActivity().getApplicationContext(), SearchLineActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("lineName", et_queryLine.getText().toString());
                    list = new ArrayList<String>() {
                    };
                    list.add("line");
                    list.add(et_queryLine.getText().toString() + "路");
                    ifAdd();
                    et_queryLine.setText("");
                    intentLine.putExtras(bundle);
                    getActivity().startActivity(intentLine);
                }
                break;
            case R.id.btn_queryStation:
                if(!et_queryStation.getText().toString().equals("")) {
                    Intent intentStation = new Intent(getActivity().getApplicationContext(), SearchStationActivity.class);
                    Bundle bundleStation = new Bundle();
                    bundleStation.putString("stationName", et_queryStation.getText().toString());
                    list = new ArrayList<String>() {
                    };
                    list.add("station");
                    list.add(et_queryStation.getText().toString() + "站");
                    ifAdd();
                    et_queryStation.setText("");
                    intentStation.putExtras(bundleStation);
                    getActivity().startActivity(intentStation);
                }
                break;
        }
    }

    private void ifAdd(){
        boolean fl = true;
        for(List<String> strlist : mlistHistory){
            if(strlist.equals(list))fl = false;
        }
        if(fl) mlistHistory.add(list);
    }

}
