package com.tt.sample.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
import com.tt.sample.ui.adapter.DelAdapter;
import com.tt.sample.ui.adapter.PageBean;
import com.tt.sample.ui.adapter.RvTypeAdapter;
import com.tt.sample.ui.view.CustomLinearLayoutManager;
import com.tt.sample.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/***
 * 选择器
 * 侧滑删除
 */
public class DelListActivity extends BaseActivity {


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
        for (int i = 0; i < 10; i++) {
            stringList.add("name " + i);
        }
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
                List<String> stringList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    stringList.add("name " + i);
                }
                delAdapter.setNewInstance(stringList);
                delListRefresh.setRefreshing(false);
            }
        });
    }
}