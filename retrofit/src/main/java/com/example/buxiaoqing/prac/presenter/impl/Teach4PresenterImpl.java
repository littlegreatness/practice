package com.example.buxiaoqing.prac.presenter.impl;


import com.example.buxiaoqing.prac.bean.ApiBean;
import com.example.buxiaoqing.prac.bean.IpBean;
import com.example.buxiaoqing.prac.model.Teach4Model;
import com.example.buxiaoqing.prac.model.impl.Teach4ModelImpl;
import com.example.buxiaoqing.prac.presenter.Teach4Presenter;
import com.example.buxiaoqing.prac.view.ITeach4View;

import rx.Subscriber;

/**
 * IP查询操作接口
 */
public class Teach4PresenterImpl implements Teach4Presenter {
    private ITeach4View teach4View;
    private Teach4Model teach4Model;

    public Teach4PresenterImpl(ITeach4View teach4View) {
        this.teach4View = teach4View;
        teach4Model = new Teach4ModelImpl();
    }

    @Override
    public void queryIpInfo(String ip) {
        Subscriber<ApiBean<IpBean>> mySubscriber = new Subscriber<ApiBean<IpBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                teach4View.showError(e);
            }

            @Override
            public void onNext(ApiBean<IpBean> apiBean) {
                teach4View.showIpInfo(apiBean.data);
            }
        };
        teach4Model.queryIpInfo(ip, mySubscriber);
    }
}
