package com.example.a20182.historyRecyclerAdapater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.a20182.FragmentBus;
import com.example.a20182.R;
import com.example.a20182.searchLineUtils.SearchLineActivity;
import com.example.a20182.SearchStationActivity;


/**
 * Created by Administrator on 2018/6/26.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter{
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = View.inflate(viewGroup.getContext(), R.layout.list_historyitem, null);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_historyitem, viewGroup, false);
        RecyclerView.ViewHolder viewHolder = new HistoryHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        HistoryHolder historyHolder = (HistoryHolder) viewHolder;

        FragmentBus fragmentBus = FragmentBus.getInstanse();

        final List<List<String>> mlistHistory = fragmentBus.getList();
        final List<String> list = mlistHistory.get(i);

        historyHolder.tv_searchHistoryItem.setText(list.get(1));
        historyHolder.tv_searchHistoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(0) == "line"){
                    Intent intentLine = new Intent(v.getContext(), SearchLineActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("lineName", list.get(1));
                    intentLine.putExtras(bundle);
                    v.getContext().startActivity(intentLine);
                }
                if(list.get(0) == "station"){
                    Intent intentStation = new Intent(v.getContext(), SearchStationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("stationName", list.get(1));
                    intentStation.putExtras(bundle);
                    v.getContext().startActivity(intentStation);
                }
            }
        });

        historyHolder.ibtn_cancelHistory.setImageResource(R.drawable.delete30);
        historyHolder.ibtn_cancelHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistHistory.size() == 1) {
                    Toast.makeText(v.getContext(),"此条目不能删除", Toast.LENGTH_SHORT).show();
                } else {
                    //删除自带默认动画
                    removeData(i);
                }
            }
        });

        historyHolder.imageView.setImageResource(R.drawable.icon_history);
    }

    @Override
    public int getItemCount() {
        return FragmentBus.getInstanse().getList().size();
    }

    public void removeData(int position) {
        FragmentBus fragmentBus = FragmentBus.getInstanse();
        List<List<String>> mlistHistory = fragmentBus.getList();

        mlistHistory.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    class HistoryHolder extends RecyclerView.ViewHolder{
        public ImageButton ibtn_cancelHistory;
        public TextView tv_searchHistoryItem;
        public ImageView imageView;
        /*
        *   初始化控件
        * */
        public HistoryHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            ibtn_cancelHistory = itemView.findViewById(R.id.ibtn_cancelHistory);
            tv_searchHistoryItem = itemView.findViewById(R.id.tv_searchHistoryItem);
        }
    }
}
