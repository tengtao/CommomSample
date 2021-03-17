package com.tt.sample.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;
import com.tt.sample.MyApplication;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
import com.tt.sample.ui.adapter.PageBean;
import com.tt.sample.ui.adapter.RvTypeAdapter;
import com.tt.sample.ui.view.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/***
 * weiview例子，还需要完善，
 */
public class RvViewPageActivity extends BaseActivity {

    //禁止滑动的
    CustomLinearLayoutManager layoutManager;
    @BindView(R.id.rv_page_rv)
    RecyclerView rvPageRv;

    RvTypeAdapter rvTypeAdapter;
    List<PageBean> pageBeans = new ArrayList<>();

    @Override
    public int getLayoutResID() {
        return R.layout.activity_rv_page;
    }

    public void inti() {
        intirvPageRv();
    }

    //
    int currIndex = 0;

    private void intirvPageRv() {
        rvPageRv.setHasFixedSize(true);
        layoutManager = new CustomLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPageRv.setLayoutManager(layoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                if (targetPos < 0) {
                    Logger.d("=========居然是负的");
                    return targetPos;
                }
                if (currIndex == targetPos) {
                    Logger.d("=========相同不操作=" + targetPos);
                    return targetPos;
                }
                //出现最后一个是可以滑的，，莫名其妙
                if (targetPos == pageBeans.size()) {
                    Logger.d("=========已经是最后一个=" + targetPos);
                    return targetPos;
                }
                Logger.d("=========targetPos " + targetPos);
                //当前index
                currIndex = targetPos;

                return targetPos;
            }
        };
        snapHelper.attachToRecyclerView(rvPageRv);
        rvTypeAdapter = new RvTypeAdapter(this, null);
        rvPageRv.setAdapter(rvTypeAdapter);


        //
        pageBeans.add(new PageBean("K1", 1));
        pageBeans.add(new PageBean("M1", 2));
        pageBeans.add(new PageBean("K2", 1));
        pageBeans.add(new PageBean("M2", 2));
        rvTypeAdapter.setNewInstance(pageBeans);

        //
        rvPageRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.d("==========修改");
                pageBeans.get(1).setName("M1XXXXX");
                rvTypeAdapter.changeState(1);
            }
        }, 2000);

    }


}