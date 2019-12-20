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
import com.szshuwei.x.collect.SWLocationClient;
import com.szshuwei.x.collect.entities.LocationData;

public class CycleLocationActivity extends AppCompatActivity implements CycleLocationListener,
        View.OnClickListener {

    private Button mBtnStartCycleLocate;
    private Button mBtnStopCycleLocate;
    private RecyclerView mLocateHistoryRv;

    private LinearLayoutManager mLinearLayoutManager;
    private LocateHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_location);

        mLocateHistoryRv = (RecyclerView) findViewById(R.id
                .rv_locate_history);
        mLocateHistoryRv.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(CycleLocationActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mLocateHistoryRv.setLayoutManager(mLinearLayoutManager);

        mAdapter = new LocateHistoryAdapter(CycleLocationActivity.this, null);
        mLocateHistoryRv.setAdapter(mAdapter);
        mBtnStartCycleLocate = (Button) findViewById(R.id.btn_start_cycle_locate);
        mBtnStopCycleLocate = (Button) findViewById(R.id.btn_stop_cycle_locate);

        mBtnStartCycleLocate.setOnClickListener(this);
        mBtnStopCycleLocate.setOnClickListener(this);

        // 注册周期定位回调监听
        SWLocationClient.getInstance().registerCycleLocationListener(this);

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
            Toast.makeText(this, "周期定位成功\n无精准位置信息", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_cycle_locate:
                boolean start = SWLocationClient.getInstance().startCycleSceneRecognizeUI();
                if (start) {
                    Toast.makeText(this, "发起周期定位成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "发起周期定位失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_stop_cycle_locate:
                boolean stop = SWLocationClient.getInstance().stopCycleSceneRecognizeUI();
                if (stop) {
                    Toast.makeText(this, "停止周期定位成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "停止周期定位失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
