package com.prac.buxiaoqing.meitudemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


/**
 * 安卓系统相关的方法
 * Created by linfangxing on 2015/10/14.
 */
public class AndroidSysUtil {
    private static final String TAG = AndroidSysUtil.class.getSimpleName();


    /**
     * whether the mobile phone network is Connecting
     *
     * @param context
     * @return
     */
    public static boolean isConnectInternet(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }


    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure
                    .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getDiviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getSerialNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int getSDKVersionNumber() {
        int sdkVersion = 0;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    /**
     * collapseSoftInput
     *
     * @param context
     * @param view
     */
    public static void collapseSoftInputMethod(final Context context, final View view) {
        final InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void collapseSoftInputMethod(final Activity context, final View view, final long delay) {
        final InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                });
            }
        }).start();
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     * @param delay
     */
    public static void showSoftInputMethod(final Activity context, final View view, final long delay) {
        final InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                                InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
            }
        }).start();
    }


    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public static String getPakageName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            // int versionCode = info.versionCode;
            // String versionName = info.versionName;
            String packageName = info.packageName;
            return packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getDeviceWidth(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getDeviceHeight(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取手机的分辨率， 格式： 宽x高
     */
    public static String getResolution(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "x" + dm.heightPixels;
    }


    /**
     * 像素转换工具类.
     *
     * @author MarkMjw
     */
    public static class PixelUtil {


        /**
         * dp转 px.
         *
         * @param value the value
         * @return the int
         */
        public static int dp2px(Context context, float value) {
            final float scale = context.getResources().getDisplayMetrics().densityDpi;
            return (int) (value * (scale / 160) + 0.5f);
        }
        public static int dp2px(float value,Context context) {
            return dp2px(context, value);
        }
        /**
         * px转dp.
         *
         * @param value   the value
         * @param context the context
         * @return the int
         */
        public static int px2dp(Context context, float value) {
            final float scale = context.getResources().getDisplayMetrics().densityDpi;
            return (int) ((value * 160) / scale + 0.5f);
        }
        public static int px2dp(float value,Context context) {
            return px2dp(context, value);
        }
       /**
         * sp转px.
         *
         * @param value   the value
         * @param context the context
         * @return the int
         */
        public static int sp2px(Context context, float value) {
            Resources r;
            if (context == null) {
                r = Resources.getSystem();
            } else {
                r = context.getResources();
            }
            float spvalue = value * r.getDisplayMetrics().scaledDensity;
            return (int) (spvalue + 0.5f);
        }
        public static int sp2px(float value, Context context) {
            return sp2px(context, value);
        }

        /**
         * px转sp.
         *
         * @param value   the value
         * @param context the context
         * @return the int
         */
        public static int px2sp(Context context, float value) {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (value / scale + 0.5f);
        }
        public static int px2sp(float value, Context context) {
            return px2sp(context, value);
        }

    }
}
