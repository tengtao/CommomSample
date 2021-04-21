package com.tt.sample.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.ui.RvViewPageActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RvTypeAdapter extends BaseMultiItemQuickAdapter<PageBean, BaseViewHolder> {
    RvViewPageActivity activity;

    public RvTypeAdapter(RvViewPageActivity activity, List data) {
        super(data);
        this.activity = activity;
        addItemType(1, R.layout.item_page1);
        addItemType(2, R.layout.item_page2);
    }

    public void changeState(int position) {
        notifyItemChanged(position, "changeState");
    }

    //艹fuck，,List<?>居然不能放object只能放问号，艹
    //只刷新单独的一个item
    //https://blog.csdn.net/a1064072510/article/details/82871034
    @Override
    protected void convert(@NotNull BaseViewHolder holder, PageBean item, @NotNull List<?> payloads) {
        super.convert(holder, item, payloads);
        //循环得到payloads里面的参数
        for (Object payload : payloads) {
            if ("changeState".equals(payload)) {
                Logger.d("========changeState");
                changeState(holder, item);
            }
        }
    }

    private void changeState(BaseViewHolder helper, PageBean lockBean) {
        int res = 0;
        if (lockBean.getType() == 1) {
            res = R.id.item_page_a;
        }
        //
        else if (lockBean.getType() == 2) {
            res = R.id.item_page_b;
        }

        helper.setText(res, lockBean.getName());
    }

    /**
     * 不同类型的锁对应不同的布局
     * 免得以后多种锁写在一个布局里面太乱
     * <p>
     * // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
     * adapter.addChildClickViewIds(R.id.btn, R.id.iv_num_add, R.id.item_click);
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, PageBean item) {
        Logger.d("=======???");
        switch (helper.getItemViewType()) {
            case 1:
                helper.setText(R.id.item_page_a, item.getName());
                helper.getView(R.id.item_page_a).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.showToast(item.getName());
                        notifyDataSetChanged();
                    }
                });
                break;
            case 2:
                helper.setText(R.id.item_page_b, item.getName());
                helper.getView(R.id.item_page_b).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.showToast(item.getName());
                    }
                });
                break;
        }
    }


}