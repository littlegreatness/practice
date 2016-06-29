package com.example.buxiaoqing.prac.model.impl;




import com.example.buxiaoqing.prac.api.APIService;
import com.example.buxiaoqing.prac.bean.ApiBean;
import com.example.buxiaoqing.prac.bean.IpBean;
import com.example.buxiaoqing.prac.model.Teach4Model;

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
 * IP查询操作接口
 */
public class Teach4ModelImpl implements Teach4Model {

    @Override
    public void queryIpInfo(final String ip, Subscriber<ApiBean<IpBean>> subscriber) {
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
        myObservable.subscribe(subscriber);
    }
}
