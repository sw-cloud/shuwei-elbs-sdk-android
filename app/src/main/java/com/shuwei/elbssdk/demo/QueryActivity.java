package com.shuwei.elbssdk.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwei.elbssdk.demo.adapter.CollectHistoryAdapter;
import com.szshuwei.x.collect.DeleteListener;
import com.szshuwei.x.collect.QueryListener;
import com.szshuwei.x.collect.SWLocationClient;
import com.szshuwei.x.collect.entities.CollectQueryData;
import com.szshuwei.x.collect.entities.DeleteData;
import com.szshuwei.x.collect.entities.DeleteResponseData;
import com.szshuwei.x.collect.entities.QueryData;
import com.szshuwei.x.collect.entities.QueryResponseData;

import java.util.ArrayList;
import java.util.List;

public class QueryActivity extends AppCompatActivity
        implements View.OnClickListener, QueryListener, DeleteListener {
    /**
     * 采集者id
     */
    private EditText mCollectorIdEt;
    /**
     * page 当前页数
     */
    private EditText mCurrentPageEt;
    /**
     * pageNumber 每页条数
     */
    private EditText mPageSizeEt;
    /**
     * 透传参数
     */
    private EditText mPassThroughEt;
    private Button mQueryBtn;
    private TextView mChooseAll;
    private TextView mDelete;
    private RecyclerView mRecyclerView;
    private CollectHistoryAdapter mAdapter;
    private int itemPosition;
    private boolean isChooseAll = false;
    private List<CollectQueryData> mShowData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_record);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new CollectHistoryAdapter(QueryActivity.this, null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCheckedListener(new CollectHistoryAdapter.OnItemCheckedListener() {

            @Override
            public void onItemChecked(CollectQueryData queryItem, int position, boolean isChecked) {
                if (isChecked) {
                    mShowData.add(queryItem);
                } else {
                    mShowData.remove(queryItem);
                }
            }
        });
    }

    private void initView() {
        mCollectorIdEt = findViewById(R.id.collector_id_et);
        mCurrentPageEt = findViewById(R.id.current_page_et);
        mPageSizeEt = findViewById(R.id.page_size_et);
        mPassThroughEt = findViewById(R.id.pass_through_et);
        mQueryBtn = findViewById(R.id.btn_query);
        mChooseAll = findViewById(R.id.tv_choose_all);
        mDelete = findViewById(R.id.tv_delete);

        mQueryBtn.setOnClickListener(this);
        mChooseAll.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        SWLocationClient.getInstance().registerQueryListener(this);
        SWLocationClient.getInstance().registerDeleteListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                query();
                break;
            case R.id.tv_choose_all:
                if (!isChooseAll) {
                    mAdapter.notifyChecked(true);
                    if (mShowData != null) {
                        mShowData.clear();
                        mShowData.addAll(mAdapter.getData());
                    }
                    isChooseAll = true;
                } else {
                    mAdapter.notifyChecked(false);
                    if (mShowData != null) {
                        mShowData.clear();
                    }
                    isChooseAll = false;
                }
                break;
            case R.id.tv_delete:
                if (mShowData != null && mShowData.size() > 0) {
                    List<String> list = new ArrayList<>();
                    for (CollectQueryData beans : mShowData) {
                        list.add(beans.getPoiId());
                    }
                    DeleteData deleteData = new DeleteData();
                    deleteData.setPoiIdList(list);
                    boolean b = SWLocationClient.getInstance().deleteCollectDataUI(deleteData);
                } else {
                    Toast.makeText(this, "未选择任何数据", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * 查询
     */
    private void query() {
        String coParam = mCollectorIdEt.getText().toString();
        String cpParam = mCurrentPageEt.getText().toString();
        String psParam = mPageSizeEt.getText().toString();
        String ptParam = mPassThroughEt.getText().toString();
        if (TextUtils.isEmpty(cpParam)) {
            Toast.makeText(this, "当前页为必填项", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(psParam)) {
            Toast.makeText(this, "每页条数为必填项", Toast.LENGTH_SHORT).show();
            return;
        }

        QueryData data = new QueryData();
        if (!TextUtils.isEmpty(coParam)) {
            data.setCollectorId(coParam);
        }
        data.setCurrentPage(Integer.parseInt(cpParam));
        data.setPageSize(Integer.parseInt(psParam));
        if (!TextUtils.isEmpty(ptParam)) {
            data.setPassThrough(ptParam);
        }
        boolean b = SWLocationClient.getInstance().queryCollectDataUI(data);
        Toast.makeText(this, "查询采集数据：" + b, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuerySuccess(int retCode, String msg, QueryResponseData data) {
        Log.i("tag", "onQuerySuccess: ------->>>>" + msg);
        Toast.makeText(this, "onQuerySuccess with code  = "
                + retCode + ", msg = " + msg, Toast.LENGTH_LONG).show();
        if (data != null) {
            List<CollectQueryData> collectList = data.getCollectList();
            if (collectList != null && !collectList.isEmpty()) {
                mAdapter.setNewData(collectList);
            }
        }
    }

    @Override
    public void onQueryError(int retCode, String msg) {
        Toast.makeText(this, "onQueryError with code  = "
                + retCode + ", msg = " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        mShowData.clear();
        mShowData = null;
        SWLocationClient.getInstance().unregisterQueryListener();
        SWLocationClient.getInstance().unregisterDeleteListener();
        super.onDestroy();
    }

    @Override
    public void onDeleteSuccess(int retCode, String msg, DeleteResponseData data) {
        Toast.makeText(this, "onDeleteSuccess with code  = "
                + retCode + ", msg = " + msg + ", data = " + data, Toast.LENGTH_LONG).show();
        if (isChooseAll) {
            mAdapter.getData().clear();
            mAdapter.setNewData(null);

        } else {
            for (CollectQueryData queryItem : mShowData) {
                mAdapter.deleteItem(queryItem);
            }
        }
        mShowData.clear();
    }

    @Override
    public void onDeleteError(int retCode, String msg) {
        Toast.makeText(this, "onDeleteSuccess with code  = "
                + retCode + ", msg = " + msg, Toast.LENGTH_LONG).show();
    }
}
