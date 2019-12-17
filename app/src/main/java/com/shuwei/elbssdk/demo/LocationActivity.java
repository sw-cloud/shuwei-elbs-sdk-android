package com.shuwei.elbssdk.demo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shuwei.elbssdk.demo.adapter.LocateHistoryAdapter;
import com.szshuwei.x.collect.CycleLocationListener;
import com.szshuwei.x.collect.LocationListener;
import com.szshuwei.x.collect.SWLocationClient;
import com.szshuwei.x.collect.entities.LocationData;

public class LocationActivity extends AppCompatActivity implements CycleLocationListener,
        View.OnClickListener, LocationListener {

    private Button mBtnPassive;
    private RecyclerView mLocateHistoryRv;

    private LinearLayoutManager mLinearLayoutManager;
    private LocateHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mLocateHistoryRv = (RecyclerView) findViewById(R.id
                .rv_locate_history);
        mLocateHistoryRv.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(LocationActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mLocateHistoryRv.setLayoutManager(mLinearLayoutManager);

        mAdapter = new LocateHistoryAdapter(LocationActivity.this, null);
        mLocateHistoryRv.setAdapter(mAdapter);
        mBtnPassive = (Button) findViewById(R.id.btn_passive);

        mBtnPassive.setOnClickListener(this);

//        SWLocationClient.getInstance().registerCycleLocationListener(this);
        SWLocationClient.getInstance().registerLocationListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }
    }

    @Override
    public void onCycleLocationSuccess(int retCode, String msg, LocationData locationData) {
        if (null == locationData) {
            Toast.makeText(this, "被动定位成功\n无精准位置信息", Toast.LENGTH_LONG).show();
            return;
        }
        mAdapter.addData(0, locationData);
    }

    @Override
    public void onCycleError(int code, String msg) {
        Toast.makeText(this, "周期定位失败：code="
                + code + ",msg=" + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationSuccess(int code, String msg, LocationData locationData) {
        if (null == locationData) {
            Toast.makeText(this, "主动定位成功\n无精准位置信息", Toast.LENGTH_LONG).show();
            return;
        }
        mAdapter.addData(0, locationData);
    }

    @Override
    public void onError(int i, String s) {
        Toast.makeText(this, "主动定位失败：code="
                + i + ",msg=" + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_passive:
                boolean isCanReqLocation = SWLocationClient.getInstance().sceneRecognizeUI();
                if (isCanReqLocation) {
                    // 可以发起主动定位
                    Toast.makeText(this, "可以发起主动定位", Toast.LENGTH_SHORT).show();
                } else {
                    // 不能发起主动定位
                    Toast.makeText(this, "不能发起主动定位", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
