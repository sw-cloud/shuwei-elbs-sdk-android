package com.shuwei.elbssdk.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shuwei.elbssdk.demo.R;
import com.shuwei.elbssdk.demo.utils.StringUtils;
import com.shuwei.elbssdk.demo.utils.TimeUtils;
import com.shuwei.elbssdk.demo.utils.Utils;
import com.szshuwei.x.collect.entities.CollectQueryData;

import java.util.ArrayList;
import java.util.List;

public class CollectHistoryAdapter extends
        RecyclerView.Adapter<CollectHistoryAdapter.CollectHistoryViewHolder> {
    private boolean isChecked = false;

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<CollectQueryData> mData;

    private OnItemCheckedListener mOnItemCheckedListener;

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        mOnItemCheckedListener = onItemCheckedListener;
    }

    public CollectHistoryAdapter(Context context, List<CollectQueryData> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        if (null == list) {
            mData = new ArrayList<>();
        } else {
            mData = list;
        }
    }

    @NonNull
    @Override
    public CollectHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_collect_history, viewGroup, false);
        return new CollectHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectHistoryViewHolder collectHistoryViewHolder, int position) {
        CollectQueryData queryItem = mData.get(position);
        collectHistoryViewHolder.bind(queryItem, position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setNewData(@Nullable List<CollectQueryData> data) {
        this.mData = data == null ? new ArrayList<>() : data;
        notifyDataSetChanged();
    }

    public void addData(int position, CollectQueryData queryEntity) {
        mData.add(position, queryEntity);
        notifyDataSetChanged();
    }

    public List<CollectQueryData> getData() {
        return mData;
    }

    public class CollectHistoryViewHolder extends RecyclerView.ViewHolder {

        public CollectHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(final CollectQueryData queryItem, final int position) {
            TextView mPointNameTv = itemView.findViewById(R.id.point_name_tv);
            TextView mBuildingNameTv = itemView.findViewById(R.id.building_name_tv);
            TextView mAddressTv = itemView.findViewById(R.id.address_tv);
            TextView mDateTv = itemView.findViewById(R.id.date_tv);
            TextView mTimeTv = itemView.findViewById(R.id.time_tv);
            CheckBox mStateCb = itemView.findViewById(R.id.state_cb);

            String pointName = queryItem.getPoiName();
            pointName = String.format(Utils.getContext().getResources()
                    .getString(R.string.locate_format_point_name), String.valueOf(position + 1), pointName);
            mPointNameTv.setText(pointName);

            String buildingName;
            String areaName = queryItem.getArea();
            if (!TextUtils.isEmpty(areaName)) {
                buildingName = areaName;
            } else {
                buildingName = StringUtils.checkNotNull(queryItem.getBuilding());
            }
            if (TextUtils.isEmpty(queryItem.getFloorName())) {
                mBuildingNameTv.setText(buildingName);
            } else {
                mBuildingNameTv.setText(buildingName + String.format(Utils.getContext().getResources
                        ().getString(R.string.locate_format_floor), queryItem.getFloorName()));
            }

            mAddressTv.setText(StringUtils.checkNotNull(queryItem.getAddress()));
            String[] dateTime = TimeUtils.getLegalDateTime(queryItem.getTimestamp());
            mDateTv.setText(StringUtils.checkNotNull(dateTime[0]));
            mTimeTv.setText(StringUtils.checkNotNull(dateTime[1]));

            mStateCb.setChecked(isChecked);
            mStateCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mOnItemCheckedListener != null) {
                        mOnItemCheckedListener.onItemChecked(queryItem, position, isChecked);
                    }
                }
            });
        }
    }

    public interface OnItemCheckedListener {
        void onItemChecked(CollectQueryData queryEntity, int position, boolean isChecked);
    }

    public void notifyChecked(boolean isChecked) {
        this.isChecked = isChecked;
        notifyDataSetChanged();
    }

    public void deleteItem(CollectQueryData dataBean) {
        mData.remove(dataBean);
        setNewData(mData);
    }

}
