package com.shuwei.elbssdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.szshuwei.x.collect.SWLocationClient;

/**
 * Created by anxy on 2019/6/10.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mServiceStartBtn;
    private Button mServiceStopBtn;
    private Button mCollectBtn;
    private Button mLocationBtn;
    private Button mCycleLocationBtn;
    private Button mCollectHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceStartBtn = findViewById(R.id.btn_start);
        mServiceStopBtn = findViewById(R.id.btn_stop);
        mCollectBtn = findViewById(R.id.btn_collect);
        mLocationBtn = findViewById(R.id.btn_location);
        mCycleLocationBtn = findViewById(R.id.btn_cycle_location);
        mCollectHistoryBtn = findViewById(R.id.btn_collect_history);

        mServiceStartBtn.setOnClickListener(this);
        mServiceStopBtn.setOnClickListener(this);
        mCollectBtn.setOnClickListener(this);
        mLocationBtn.setOnClickListener(this);
        mCycleLocationBtn.setOnClickListener(this);
        mCollectHistoryBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                SWLocationClient.getInstance().setOnClientStartListener(new SWLocationClient.OnClientStartListener() {
                    @Override
                    public void onStartSuccess() {
                        Toast.makeText(MainActivity.this, "启动服务成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStartFail() {
                        Toast.makeText(MainActivity.this, "启动服务失败", Toast.LENGTH_SHORT).show();
                    }
                });
                SWLocationClient.getInstance().start();
                Toast.makeText(this, "启动服务", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_stop:
                SWLocationClient.getInstance().stop();
                Toast.makeText(this, "停止服务", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_collect:
                Intent collectIntent = new Intent(MainActivity.this, CollectActivity.class);
                startActivity(collectIntent);
                break;
            case R.id.btn_location:
                Intent locationIntent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(locationIntent);
                break;
            case R.id.btn_cycle_location:
                Intent cycleLocationIntent = new Intent(MainActivity.this, CycleLocationActivity.class);
                startActivity(cycleLocationIntent);
                break;
            case R.id.btn_collect_history:
                Intent collectHistoryIntent = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(collectHistoryIntent);
                break;
        }
    }
}
