package com.tt.sample.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
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
 * https://github.com/Bigkoo/Android-PickerView
 */
public class PickActivity extends BaseActivity {


    @BindView(R.id.pick_time)
    Button pickTime;
    @BindView(R.id.pick_str)
    Button pickStr;


    TimePickerView pvTime;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_pick;
    }

    public void inti() {
        selTime();
    }


    @OnClick({R.id.pick_time, R.id.pick_str})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pick_time:
                pvTime.show();
                break;
            case R.id.pick_str:
                selData();
                break;
        }
    }


    void selTime() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

//        注意：当我们进行设置时间的启始位置时，需要特别注意月份的设定
//        原因：Calendar组件内部的月份，是从0开始的，即0-11代表1-12月份
//        错误使用案例： startDate.set(2013,1,1);  endDate.set(2020,12,1);
//        正确使用案例： startDate.set(2013,0,1);  endDate.set(2020,11,1);
        startDate.set(2013, 0, 1);
        endDate.set(2020, 11, 31);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                pickTime.setText(TimeUtils.getTimes(date));
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Sure")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("Title")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        //设置在底部填满横向
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);
        params.leftMargin = 0;
        params.rightMargin = 0;
        pvTime.getDialogContainerLayout().setLayoutParams(params);
    }


    void selData() {
        List<String> optList = new ArrayList<>();
        optList.add("星期一");
        optList.add("星期二");
        optList.add("星期三");
        optList.add("星期四");
        optList.add("星期五");
        optList.add("星期六");
        optList.add("星期日");
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = optList.get(options1);
                pickStr.setText(tx);
            }
        }).build();
        pvOptions.setPicker(optList);
        pvOptions.show();
    }


}