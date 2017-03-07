package com.prac.buxiaoqing.prac.gif;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.prac.buxiaoqing.prac.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okio.BufferedSink;

/**
 * author：buxiaoqing on 2017/3/3 14:18
 * Just do IT(没有梦想,何必远方)
 */
public class OkHttpAct extends Activity {
    private static String TAG = "buxq";
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
    }

    //        executor.schedule(new Runnable() {
//            @Override
//            public void run() {
//                //如果取消的时候正在进行读写操作，会报IO异常
//                call.cancel();
//            }
//        }, 30000, TimeUnit.MICROSECONDS);
//

    //异步GET请求
    public void getAsyncHttp(final View v) {
        OkHttpClient.Builder mClient = new OkHttpClient().newBuilder();
        mClient.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                return null;
            }
        });

        final Request request = new Request.Builder().url("http://www.baidu.com")
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();
        final Call call = mClient.build().newCall(request);
        //进入队列，就是包装进一个runnable放入队列
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: cache : " + "   ===" + response.networkResponse().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setBackgroundResource(R.color.colorPrimary);
                        Toast.makeText(getApplicationContext(), "异步GET请求 OK !", Toast.LENGTH_SHORT).show();
                    }
                });
                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    Log.d(TAG, "Main Thread");
                } else {
                    Log.d(TAG, "Not Main Thread");
                }
            }
        });
    }

    //同步GET请求
    public void getSyncHttp(View v) throws IOException {
        OkHttpClient mClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://www.baidu.com").build();
        final Call call = mClient.newCall(request);
        //同步请求需要在子线程中执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "getSyncHttp: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        throw new IOException("Unexpected code " + response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void postAsynHttp(View v) {
        OkHttpClient mClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("size", "10")
                .add("app", "weather")
                .build();

        Request request = new Request.Builder().url("http://api.1-blog.com/biz/bizserver/article/list.do")
                .post(requestBody).build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    public void postStream(View v) throws Exception {
        final OkHttpClient mClient = new OkHttpClient();
        RequestBody requestBody = new RequestBody() {
            @Override
            public okhttp3.MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }
        };
        final Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();

        final Call call = mClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private String factor(int n) {
        for (int i = 2; i < n; i++) {
            int x = n / i;
            if (x * i == n) return factor(x) + " × " + i;
        }
        return Integer.toString(n);
    }

    private void setCache(OkHttpClient.Builder mClient) {
        mClient.connectTimeout(20, TimeUnit.SECONDS);
        mClient.readTimeout(20, TimeUnit.SECONDS);
        mClient.writeTimeout(20, TimeUnit.SECONDS);
        File externalCacheDir = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        mClient.cache(new Cache(externalCacheDir.getAbsoluteFile(), cacheSize));
    }
}

