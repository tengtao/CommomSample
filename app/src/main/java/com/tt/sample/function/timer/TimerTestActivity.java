package com.tt.sample.function.timer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.tt.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TimerTestActivity extends AppCompatActivity {


    @BindView(R.id.pb1)
    ProgressBar pb1;
    @BindView(R.id.timer_start_1)
    Button timerStart1;
    @BindView(R.id.timer_stop_1)
    Button timerStop1;
    @BindView(R.id.pb2)
    ProgressBar pb2;
    @BindView(R.id.timer_start_2)
    Button timerStart2;
    @BindView(R.id.timer_stop_2)
    Button timerStop2;
    @BindView(R.id.pb3)
    ProgressBar pb3;
    @BindView(R.id.timer_start_3)
    Button timerStart3;
    @BindView(R.id.timer_stop_3)
    Button timerStop3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_test);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.timer_start_1, R.id.timer_stop_1, R.id.timer_start_2, R.id.timer_stop_2, R.id.timer_start_3, R.id.timer_stop_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timer_start_1:
                pb1.setVisibility(View.VISIBLE);
                HandleTimer.startTimeOut("test1", 5000, new HandleTimer.TimeoutCallback() {
                    @Override
                    public void onTimeOut() {
                        pb1.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.timer_stop_1:
                HandleTimer.stopTimer("test1");
                break;
            case R.id.timer_start_2:
                pb2.setVisibility(View.VISIBLE);
                HandleTimer.startTimeOut("test2", 5000, new HandleTimer.TimeoutCallback() {
                    @Override
                    public void onTimeOut() {
                        pb2.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.timer_stop_2:
                HandleTimer.stopTimer("test2");
                break;
            case R.id.timer_start_3:
                pb3.setVisibility(View.VISIBLE);
                HandleTimer.startTimeOut("test3", 5000, new HandleTimer.TimeoutCallback() {
                    @Override
                    public void onTimeOut() {
                        pb3.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.timer_stop_3:
                HandleTimer.stopTimer("test3");
                break;
        }
    }
}
