package com.example.a20182.historyRecyclerAdapater;

/**
 * Created by Administrator on 2018/6/26.
 * 用于保存起始点路线搜索历史
 */

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.example.a20182.R;

import java.util.ArrayList;

public class RouteSelectHistoryAdapter extends RecyclerView.Adapter{
    private ArrayList<String[]> routeHistory = new ArrayList<>();
    private ArrayList<LatLonPoint[]> latLonPoints = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public RouteSelectHistoryAdapter(ArrayList<String[]> routeHistory, ArrayList<LatLonPoint[]> locationBeans){
        this.routeHistory = routeHistory;
        this.latLonPoints = locationBeans;

    }

    //用于获取item对应的viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_routeselecthistory, viewGroup, false);

        RecyclerView.ViewHolder viewHolder = new HistoryHolder(view);
        return viewHolder;
    }

    //填充item中各控件中内容
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        HistoryHolder historyHolder = (HistoryHolder) viewHolder;
        if(mItemClickListener != null){
            historyHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onClick(i);
                }
            });
        }
        historyHolder.tv_startHistrory.setText(routeHistory.get(i)[0]);
        historyHolder.tv_endHistory.setText(routeHistory.get(i)[1]);

        historyHolder.iv_historyIcon.setImageResource(R.drawable.routeselecthistory);
        historyHolder.iv_historyArrow.setImageResource(R.drawable.right);
    }

    @Override
    public int getItemCount() {
        return routeHistory.size();
    }



    class HistoryHolder extends RecyclerView.ViewHolder{
        public TextView tv_startHistrory, tv_endHistory;
        public ImageView iv_historyIcon,iv_historyArrow;
        /*
        *   初始化控件
        * */
        public HistoryHolder(View itemView) {
            super(itemView);
            tv_startHistrory = itemView.findViewById(R.id.tv_startHistrory);
            tv_endHistory = itemView.findViewById(R.id.tv_endHistory);
            iv_historyIcon = itemView.findViewById(R.id.iv_historyIcon);
            iv_historyArrow = itemView.findViewById(R.id.iv_historyArrow);
            itemView = itemView;
        }
    }


    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}

