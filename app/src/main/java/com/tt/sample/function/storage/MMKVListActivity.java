package com.tt.sample.function.storage;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
import com.tt.sample.function.storage.mmkv.MMKVSample;
import com.tt.sample.ui.adapter.DelAdapter;
import com.tt.sample.ui.view.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/***
 * 选择器
 * 侧滑删除
 */
public class MMKVListActivity extends BaseActivity {


    @BindView(R.id.del_list_rv)
    RecyclerView delListRv;
    @BindView(R.id.del_list_refresh)
    SwipeRefreshLayout delListRefresh;

    DelAdapter delAdapter;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_del_list;
    }

    public void inti() {
        intidelListRv();
    }

    private void intidelListRv() {
        LinearLayoutManager layoutManager = new CustomLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        delListRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        delListRv.setLayoutManager(layoutManager);
        delAdapter = new DelAdapter(this, null);
        delListRv.setAdapter(delAdapter);

        //

        List<String> stringList = new ArrayList<>();
        delAdapter.setNewInstance(stringList);
        delAdapter.addChildClickViewIds(R.id.item_del_menu_del);
        delAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.item_del_menu_del) {
                    showToast("删除" + delAdapter.getData().get(position));
                    delAdapter.removeAt(position);
                }

            }
        });

        delListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> stringList = MMKVSample.testGetAllData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                delAdapter.setNewInstance(stringList);
                                delListRefresh.setRefreshing(false);
                            }
                        });
                    }
                }).start();

            }
        });
    }
}