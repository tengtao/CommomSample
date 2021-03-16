package com.tt.sample.function.jetpack;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tt.sample.bean.UserBean;

import java.util.HashMap;
import java.util.List;

/**
 * 一个通用的法则是，你的 ViewModel 中没有导入像 android.*这样的包（像 android.arch.* 这样的除外)。
 * 这个经验也同样适用于 MVP 模式中的 Presenter 。
 * <p>
 * ❌ 避免在 ViewModel 里持有视图层的引用
 */
public class UserListViewModel extends ViewModel {
    public static final String TAG = "UserListViewModel";
    //用户信息
    private MutableLiveData<List<UserBean>> userListLiveData;
    //进条度的显示
    private MutableLiveData<Boolean> loadingLiveData;

    //
    private HashMap<String, MutableLiveData> hashMapLiveData = new HashMap<>();

    public <T> void createLiveData(String key, T t) {
        MutableLiveData<T> liveData = new MutableLiveData<T>();
        hashMapLiveData.put(key, liveData);
    }

    public UserListViewModel() {
        userListLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
    }

    /**
     * 获取用户列表信息
     * 假装网络请求 2s后 返回用户信息
     */
    public void getUserInfo() {
        log("=========getUserInfo");
        loadingLiveData.setValue(true);

        UserRepository.getUserRepository().getUsersFromServer(new UserRepository.Callback<List<UserBean>>() {
            @Override
            public void onSuccess(List<UserBean> users) {
                loadingLiveData.setValue(false);
                userListLiveData.setValue(users);
            }

            @Override
            public void onFailed(String msg) {
                loadingLiveData.setValue(false);
                userListLiveData.setValue(null);
            }
        });
    }

    //返回LiveData类型
    public LiveData<List<UserBean>> getUserListLiveData() {
        return userListLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        userListLiveData = null;
        loadingLiveData = null;
        //UserRepository清除callback
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}

