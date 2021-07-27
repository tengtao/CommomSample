package com.tt.sample.function.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.google.gson.Gson;

import java.util.Locale;

public class LanguageUtil {

    // 简体中文
    public static final String CHINESE = "zh_CN";
    public static final String CHINESE2 = "zh";
    // 英文
    public static final String ENGLISH = "en_US";

    public static final String ENGLISH3 = "en";

    public static final String ENGLISH2 = "en_UK";
    // 繁体中文
    public static final String TRADITIONAL_CHINESE = "zh-hant";
    // 法语
    public static final String FRANCE = "fr";
    // 德语
    public static final String GERMAN = "de";
    // 印地语
    public static final String HINDI = "hi";
    // 意大利语
    public static final String ITALIAN = "it";
    //越南
    public static final String VIETNAM = "vn";
    public static final String VIETNAMS = "zbl";

    /**
     * 中文
     */
    public static final Locale LOCALE_CHINESE = Locale.CHINESE;
    /**
     * 英文
     */
    public static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
    /**
     * 越南
     */
    public static final Locale LOCALE_VN = new Locale(VIETNAMS, "VN");

    private static final String LOCALE_SP = "LOCALE_SP";
    private static final String LOCALE_SP_KEY = "LOCALE_SP_KEY";


    public static Locale getLocale(Context context) {
        SharedPreferences spLocale = context.getSharedPreferences(LOCALE_SP, Context.MODE_PRIVATE);
        String localeJson = spLocale.getString(LOCALE_SP_KEY, "");
        Gson gson = new Gson();
        return gson.fromJson(localeJson, Locale.class);
    }


    private static void setLocale(Context pContext, Locale pUserLocale) {
        SharedPreferences spLocal = pContext.getSharedPreferences(LOCALE_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spLocal.edit();
        String json = new Gson().toJson(pUserLocale);
        edit.putString(LOCALE_SP_KEY, json);
        edit.apply();
    }


    public static boolean updateLocale(Context context, Locale locale) {
        if (needUpdateLocale(context, locale)) {
            Configuration configuration = context.getResources().getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            } else {
                configuration.locale = locale;
            }
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            context.getResources().updateConfiguration(configuration, displayMetrics);
            setLocale(context, locale);
            return true;
        }
        return false;
    }

    public static boolean needUpdateLocale(Context pContext, Locale newUserLocale) {
        return newUserLocale != null && !getCurrentLocale(pContext).equals(newUserLocale);
    }

    public static Locale getCurrentLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }

    public static Context attachBaseContext(Context context, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context, locale);
        } else {
            applyLanguage(context, locale);
            return context;
        }
    }

    public static void applyLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocale(locale);
        } else {
            // updateConfiguration
            configuration.locale = locale;
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}