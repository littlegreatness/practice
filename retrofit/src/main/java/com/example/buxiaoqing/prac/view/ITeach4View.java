package com.example.buxiaoqing.prac.view;


import com.example.buxiaoqing.prac.bean.IpBean;

/**
 * 世界接口
 */
public interface ITeach4View {
    /**
     * 显示IP信息
     *
     * @param ipBean IP实体类
     */
    public void showIpInfo(IpBean ipBean);

    /**
     * 显示错误消息
     *
     * @param e 错误
     */
    public void showError(Throwable e);
}

