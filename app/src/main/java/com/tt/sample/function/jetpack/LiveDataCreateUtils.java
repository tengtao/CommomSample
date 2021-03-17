package com.tt.sample.function.jetpack;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;

public class LiveDataCreateUtils {


    //
    private static HashMap<String, MutableLiveData> hashMapLiveData = new HashMap<>();

    public static <T> void createLiveData(String key, T t) {
        MutableLiveData<T> liveData = new MutableLiveData<T>();
        hashMapLiveData.put(key, liveData);
    }

    public static <T> MutableLiveData<T> getLiveData(String key) {
        return (MutableLiveData<T>) hashMapLiveData.get(key);
    }

}
