package com.tt.sample.function.devinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.tt.sample.MyApplication;
import com.tt.sample.R;


public class DevControlActivity extends AppCompatActivity {
    Button conLight, conVoi, conHotSpot;

    boolean isopen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devcontrol);
        conLight = findViewById(R.id.control_light);
        conVoi = findViewById(R.id.control_voi);
        conHotSpot = findViewById(R.id.control_hotspot);

        int currLight = DevControlUtils.getCurrentScreenBrightness(MyApplication.getInstance());
        conLight.setText("设置亮度(" + currLight + ")");

        isopen = DevControlUtils.isWifiApEnabled(MyApplication.getInstance());
        conHotSpot.setText("开关热点(" + isopen + ")");

        conLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
//                DevControlUtils.setCurrentBrightness(DevControlActivity.this, 50);
                DevControlUtils.setSystemLight(DevControlActivity.this, 50);
            }
        });
        conVoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevControlUtils.setVoi(MyApplication.getInstance(), 10);
            }
        });
        //
        conHotSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isopen) {
                    DevControlUtils.closeWifiAp(getApplicationContext());
                } else {
//                    DevControlUtils.startWifiAp(getApplicationContext(), "tom123",
//                            "12345678", true);
                    DevControlUtils.startTethering(getApplicationContext());
                }
                isopen = DevControlUtils.isWifiApEnabled(MyApplication.getInstance());
                conHotSpot.setText("开关热点(" + isopen + ")");

            }
        });
    }


}
