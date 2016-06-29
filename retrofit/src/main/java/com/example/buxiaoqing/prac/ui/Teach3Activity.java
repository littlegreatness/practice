package com.example.buxiaoqing.prac.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.buxiaoqing.prac.R;
import com.example.buxiaoqing.prac.api.APIService;
import com.example.buxiaoqing.prac.bean.ApiBean;
import com.example.buxiaoqing.prac.bean.IpBean;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 教程一：扩展篇：Retrofit+RxJava</br>
 * 本教程演示将Retrofit结合RxJava进行数据请求，在Android开发中可使用RxAndroid
 *
 * @author ysbing
 */
public class Teach3Activity extends Activity implements View.OnClickListener {
    //IP地址
    private EditText mIpText;
    //请求的结果，IP信息
    private TextView mIpInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach3);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mIpText = (EditText) findViewById(R.id.text_ip);
        mIpInfoText = (TextView) findViewById(R.id.text_ip_info);
        findViewById(R.id.button_query_execute).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_query_execute:
                retrofitRxjava();
                break;
        }
    }

    /**
     * 使用RxJava获取数据，可以代替线程的开启，设置订阅者为主线程进行更新界面，如果对RxJava不熟悉的童鞋，建议先去学习再回来读本节教程
     */
    private void retrofitRxjava() {
        final String ip = mIpText.getText().toString();
        if (ip.length() == 0) {
            Toast.makeText(this, "请输入IP地址", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<ApiBean<IpBean>> myObservable = Observable.create(
                new Observable.OnSubscribe<ApiBean<IpBean>>() {
                    @Override
                    public void call(Subscriber<? super ApiBean<IpBean>> subscriber) {
                        String baseUrl = "http://ip.taobao.com";
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        APIService apiService = retrofit.create(APIService.class);
                        Call<ApiBean<IpBean>> call = apiService.getIpInfo2(ip);
                        try {
                            Response<ApiBean<IpBean>> response = call.execute();
                            ApiBean<IpBean> apiBean = response.body();
                            subscriber.onNext(apiBean);
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        } finally {
                            subscriber.onCompleted();
                        }
                    }
                }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()); // 指定 Subscriber 的回调发生在主线程
        myObservable.subscribe(new Subscriber<ApiBean<IpBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                String err = "很遗憾失败了，错误是：" + e.toString();
                mIpInfoText.setText(err);
            }

            @Override
            public void onNext(ApiBean<IpBean> apiBean) {
                String ipInfo = String.format("这里是结合RxJava获取的数据，IP是：%s，我在%s%s%s", apiBean.data.ip, apiBean.data.country, apiBean.data.region, apiBean.data.city);
                mIpInfoText.setText(ipInfo);
            }
        });
    }
}
