package com.tt.sample.function.jetpack;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

public class LiveDataCreateUtils {


    //
    private static HashMap<String, MutableLiveData> hashMapLiveData = new HashMap<>();

    public static <T> void createLiveData(String key) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        hashMapLiveData.put(key, liveData);
    }

    public static MutableLiveData getLiveData(String key) {
        return hashMapLiveData.get(key);
    }

}
