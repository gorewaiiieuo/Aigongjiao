package com.example.guohuayu.historyRecyclerAdapater;

/**
 * Created by Administrator on 2018/7/9.
 */

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guohuayu.Navigation.LocationBean;
import com.example.a20182.R;


/**
 * Created by Administrator on 2018/6/26.
 */

public class RouteSelectHistory extends RecyclerView.Adapter implements View.OnClickListener {
    private String[][] routeHistory;
    private LocationBean[][] locationBeans;
    private OnItemClickListener mItemClickListener;

    public RouteSelectHistory(String[][] routeHistory, LocationBean[][] locationBeans){
        this.routeHistory = routeHistory;
        this.locationBeans = locationBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = View.inflate(viewGroup.getContext(), R.layout.list_historyitem, null);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_routeselecthistory, viewGroup, false);

        view.setOnClickListener(this);
        RecyclerView.ViewHolder viewHolder = new HistoryHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        HistoryHolder historyHolder = (HistoryHolder) viewHolder;
        ((HistoryHolder) viewHolder).itemView.setTag(i);
        historyHolder.tv_startHistrory.setText(routeHistory[i][0]);
        historyHolder.tv_endHistory.setText(routeHistory[i][1]);

        historyHolder.iv_historyIcon.setImageResource(R.drawable.routeselecthistory);
        historyHolder.iv_historyArrow.setImageResource(R.drawable.right);
    }

    @Override
    public int getItemCount() {
        return routeHistory.length;
    }

    @Override
    public void onClick(View v) {
        if(mItemClickListener != null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    class HistoryHolder extends RecyclerView.ViewHolder{
        public TextView tv_startHistrory, tv_endHistory;
        public ImageView iv_historyIcon,iv_historyArrow;
        public View itemView;
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
        void onItemClick(int position);
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}

