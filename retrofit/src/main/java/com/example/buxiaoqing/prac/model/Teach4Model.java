package com.example.buxiaoqing.prac.model;


import com.example.buxiaoqing.prac.bean.ApiBean;
import com.example.buxiaoqing.prac.bean.IpBean;

import rx.Subscriber;

/**
 * IP查询操作接口
 */
public interface Teach4Model {
    /**
     * 查询IP信息
     *
     * @param ip ip地址
     */
    public void queryIpInfo(String ip, Subscriber<ApiBean<IpBean>> subscriber);
}
