package com.tt.sample.function.jetpack;


import android.os.AsyncTask;

import com.tt.sample.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例模式，
 * 负责获取数据
 */
public class UserRepository {

    private static UserRepository mUserRepository;

    static UserRepository getUserRepository() {
        if (mUserRepository == null) {
            mUserRepository = new UserRepository();
        }
        return mUserRepository;
    }

    //(假装)从服务端获取
    void getUsersFromServer(Callback<List<UserBean>> callback) {
        new AsyncTask<Void, Void, List<UserBean>>() {
            @Override
            protected void onPostExecute(List<UserBean> users) {
                callback.onSuccess(users);
                //存本地数据库
//                saveUsersToLocal(users);
            }

            @Override
            protected List<UserBean> doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //假装从服务端获取的
                List<UserBean> users = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    UserBean user = new UserBean("user" + i, i + "");
                    users.add(user);
                }
                return users;
            }
        }.execute();
    }


    public interface Callback<T> {
        void onSuccess(T t);

        void onFailed(String msg);
    }

}