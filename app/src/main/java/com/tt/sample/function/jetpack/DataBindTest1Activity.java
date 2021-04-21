package com.tt.sample.function.jetpack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.bean.UserBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DataBindTest1Activity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.testshow)
    TextView testshow;


    private UserListViewModel mUserListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewModel();
        observeLivaData();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                mUserListViewModel.getUserInfo();
                break;
            case R.id.btn2:
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
        }
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory());
        mUserListViewModel = viewModelProvider.get(UserListViewModel.class);
    }

    //观察ViewModel的数据，且此数据 是 View 直接需要的，不需要再做逻辑处理
    private void observeLivaData() {
        mUserListViewModel.getUserListLiveData().observe(this, new Observer<List<UserBean>>() {
            @Override
            public void onChanged(List<UserBean> users) {
                if (users == null) {
                    Toast.makeText(DataBindTest1Activity.this, "获取user失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //刷新列表
            }
        });

        mUserListViewModel.getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //显示/隐藏加载进度条
                progress.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });

//        //
        StockLiveData.get("symbol").observe(this, price -> {
            // Update the UI.
            Logger.d("=======11111==>" + price.toString());
            testshow.setText(price.toString());
        });
    }


}
