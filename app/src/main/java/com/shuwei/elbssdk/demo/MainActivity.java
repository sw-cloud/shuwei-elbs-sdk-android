package com.shuwei.elbssdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by anxy on 2019/6/10.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mCollectBtn;
    private Button mLocationBtn;
    private Button mCollectHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCollectBtn = findViewById(R.id.btn_collect);
        mLocationBtn = findViewById(R.id.btn_location);
        mCollectHistoryBtn = findViewById(R.id.btn_collect_history);

        mCollectBtn.setOnClickListener(this);
        mLocationBtn.setOnClickListener(this);
        mCollectHistoryBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_collect:
                Intent collectIntent = new Intent(MainActivity.this, CollectActivity.class);
                startActivity(collectIntent);
                break;
            case R.id.btn_location:
                Intent locationIntent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(locationIntent);
                break;
            case R.id.btn_collect_history:
                Intent collectHistoryIntent = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(collectHistoryIntent);
                break;
        }
    }
}
