package com.shuwei.elbssdk.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuwei.elbssdk.demo.R;
import com.shuwei.elbssdk.demo.utils.StringUtils;
import com.shuwei.elbssdk.demo.utils.TimeUtils;
import com.shuwei.elbssdk.demo.utils.Utils;
import com.szshuwei.x.collect.entities.LocationData;

import java.util.ArrayList;
import java.util.List;

public class LocateHistoryAdapter extends
        RecyclerView.Adapter<LocateHistoryAdapter.LocateHistoryViewHolder> {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<LocationData> mData;

    public LocateHistoryAdapter(Context context, List<LocationData> list) {
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
    public LocateHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(com.shuwei.elbssdk.demo.R.layout.item_locate_history, viewGroup, false);
        return new LocateHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocateHistoryViewHolder locateHistoryViewHolder, int position) {
        LocationData locationData = mData.get(position);
        locateHistoryViewHolder.bind(locationData, position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setNewData(@Nullable List<LocationData> data) {
        this.mData = data == null ? new ArrayList<>() : data;
        notifyDataSetChanged();
    }

    public void addData(int position, LocationData locationData) {
        mData.add(position, locationData);
        notifyDataSetChanged();
    }

    public class LocateHistoryViewHolder extends RecyclerView.ViewHolder {


        public LocateHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(LocationData locationData, int position) {
            int size = mData.size();
            TextView mPointNameTv = itemView.findViewById(R.id.point_name_tv);
            TextView mBuildingNameTv = itemView.findViewById(R.id.building_name_tv);
            TextView mAddressTv = itemView.findViewById(R.id.address_tv);
            TextView mDateTv = itemView.findViewById(R.id.date_tv);
            TextView mLatLngTv = itemView.findViewById(R.id.lat_lng_tv);
            TextView mTimeTv = itemView.findViewById(R.id.time_tv);

            String pointName = locationData.getName();
            pointName = String.format(Utils.getContext().getResources()
                    .getString(R.string.locate_format_point_name), String.valueOf(size - position), pointName);
            mPointNameTv.setText(pointName);

            String buildingName = locationData.getBuilding();
            if (TextUtils.isEmpty(buildingName)) {
                buildingName = locationData.getArea();
            }
            if (TextUtils.isEmpty(locationData.getFloorName())) {
                mBuildingNameTv.setText(buildingName);
            } else {
                mBuildingNameTv.setText(buildingName + String.format(Utils.getContext().getResources
                        ().getString(R.string.locate_format_floor), locationData.getFloorName()));
            }

            mAddressTv.setText(StringUtils.join(StringUtils.checkNotNull(locationData.getCity()),
                    StringUtils.checkNotNull(locationData.getRegion())));

            mLatLngTv.setText(StringUtils.checkNotNull(locationData.getLatitude()
                    + " / " + locationData.getLongitude()));

            String[] dateTime = TimeUtils.getLegalDateTime(locationData.getTimestamp());
            mDateTv.setText(StringUtils.checkNotNull(dateTime[0]));
            mTimeTv.setText(StringUtils.checkNotNull(dateTime[1]));
        }
    }

}
