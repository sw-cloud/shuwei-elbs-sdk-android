package com.shuwei.elbssdk.demo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.szshuwei.x.collect.CollectListener;
import com.szshuwei.x.collect.SWLocationClient;
import com.szshuwei.x.collect.entities.CollectData;
import com.szshuwei.x.collect.entities.CollectResponseData;

public class CollectActivity extends AppCompatActivity implements
        View.OnClickListener, CollectListener {

    private TextView mTv;
    private EditText mPoiNameEt;
    private EditText mFloorNameEt;
    private EditText mBuildNameEt;
    private EditText mAreaNameEt;
    private EditText mAddressEt;
    private EditText mStreetEt;
    private EditText mRegionEt;
    private EditText mCityEt;
    private EditText mProvinceEt;
    private EditText mExtParamEt;
    private EditText mPassThroughEt;
    private EditText mPoiIdEt;
    private EditText mCollectorIdEt;
    private Button mSubmitCollectData;

    private int mSuccessCount = 0;
    private int mFailCount = 0;

    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        mTv = (TextView) findViewById(R.id.tv);
        mPoiNameEt = (EditText) findViewById(R.id.poi_name_et);
        mFloorNameEt = (EditText) findViewById(R.id.floor_name_et);
        mBuildNameEt = (EditText) findViewById(R.id.build_name_et);
        mAreaNameEt = (EditText) findViewById(R.id.area_name_et);
        mAddressEt = (EditText) findViewById(R.id.address_name_et);
        mStreetEt = (EditText) findViewById(R.id.street_name_et);
        mRegionEt = (EditText) findViewById(R.id.region_name_et);
        mCityEt = (EditText) findViewById(R.id.city_name_et);
        mProvinceEt = (EditText) findViewById(R.id.province_name_et);
        mExtParamEt = (EditText) findViewById(R.id.ext_param_et);
        mPassThroughEt = (EditText) findViewById(R.id.pass_through_et);
        mPoiIdEt = (EditText) findViewById(R.id.poi_id_et);
        mCollectorIdEt = (EditText) findViewById(R.id.collector_id_et);
        mSubmitCollectData = (Button) findViewById(R.id.submit_collect_data);

        mSubmitCollectData.setOnClickListener(this);

        // 注册采集回调监听
        SWLocationClient.getInstance().registerCollectListener(this);

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

    private void showResult(int successCount, int failCount) {
        String format = String.format("采集耗时：+ " + (endTime - startTime) +
                "ms\n\n采集成功：%d\n采集失败：%d", successCount, failCount);
        mTv.setText(format);
    }

    @Override
    public void onCollectSuccess(int retCode, String msg, CollectResponseData data) {
        endTime = System.currentTimeMillis();
        Toast.makeText(this, "onCollectSuccess with code  = "
                + retCode + ", msg = " + msg + ", data = " + data, Toast.LENGTH_LONG).show();
        mSuccessCount++;
        showResult(mSuccessCount, mFailCount);
    }

    @Override
    public void onCollectError(int retCode, String msg) {
        endTime = System.currentTimeMillis();
        Toast.makeText(this, "onCollectError with code  = "
                + retCode + ", msg = " + msg, Toast.LENGTH_LONG).show();
        mFailCount++;
        showResult(mSuccessCount, mFailCount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_collect_data:
                startTime = System.currentTimeMillis();
                String poiName = mPoiNameEt.getText().toString().trim();
                String floorName = mFloorNameEt.getText().toString().trim();
                String buildName = mBuildNameEt.getText().toString().trim();
                String areaName = mAreaNameEt.getText().toString().trim();
                String address = mAddressEt.getText().toString().trim();
                String street = mStreetEt.getText().toString().trim();
                String region = mRegionEt.getText().toString().trim();
                String city = mCityEt.getText().toString().trim();
                String province = mProvinceEt.getText().toString().trim();
                String collectExt = mExtParamEt.getText().toString().trim();
                String passThrough = mPassThroughEt.getText().toString().trim();
                String poiId = mPoiIdEt.getText().toString().trim();
                String collectorId = mCollectorIdEt.getText().toString().trim();
                CollectData collectData = new CollectData();
                collectData.setName(poiName);
                collectData.setFloorName(floorName);
                collectData.setBuilding(buildName);
                collectData.setArea(areaName);
                collectData.setAddress(address);
                collectData.setStreet(street);
                collectData.setRegion(region);
                collectData.setCity(city);
                collectData.setProvince(province);
                collectData.setCollectExt(collectExt);
                collectData.setPassThrough(passThrough);
                collectData.setPoiId(poiId);
                collectData.setCollectorId(collectorId);
                boolean isCanSubmit = SWLocationClient.getInstance().submitCollectDataUI(collectData);
                if (isCanSubmit) {
                    // 如果可以提交采集数据
                    Toast.makeText(this, "可以提交采集数据", Toast.LENGTH_SHORT).show();
                } else {
                    // 如果不能提交采集数据
                    Toast.makeText(this, "不能提交采集数据", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
