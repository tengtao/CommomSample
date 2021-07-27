package com.tt.sample.function.language;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * 多语言的Application配置
 */
public class LanguageSampleApplication extends Application {

    private static LanguageSampleApplication instance;

    //单例模式中获取唯一的MyApplication实例
    public static LanguageSampleApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 多语言
     */
    @Override
    public void attachBaseContext(Context base) {
        Locale locale = LanguageUtil.getLocale(base);
        if (locale == null) {
            locale = LanguageUtil.LOCALE_ENGLISH;
        }
        super.attachBaseContext(LanguageUtil.attachBaseContext(base, locale));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        languageWork();
    }

    private void languageWork() {
        //自己写的工具包（如下）
        Locale locale = LanguageUtil.getLocale(this);
        LanguageUtil.updateLocale(this, locale);
    }

}
