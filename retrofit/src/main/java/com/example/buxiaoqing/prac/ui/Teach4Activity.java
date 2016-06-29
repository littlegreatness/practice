package com.example.buxiaoqing.prac.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buxiaoqing.prac.R;
import com.example.buxiaoqing.prac.bean.IpBean;
import com.example.buxiaoqing.prac.presenter.Teach4Presenter;
import com.example.buxiaoqing.prac.presenter.impl.Teach4PresenterImpl;
import com.example.buxiaoqing.prac.view.ITeach4View;


/**
 * 教程一：实战篇：实现MVP模式进行开发</br>
 * 本教程演示使用MVP开发模式，结合RxJava进行实现数据获取到界面显示的过程
 *
 * @author ysbing
 */
public class Teach4Activity extends Activity implements View.OnClickListener, ITeach4View {

    private Teach4Presenter presenter;

    //IP地址
    private EditText mIpText;
    //请求的结果，IP信息
    private TextView mIpInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach4);
        presenter = new Teach4PresenterImpl(this);
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
                String ip = mIpText.getText().toString();
                if (ip.length() == 0) {
                    Toast.makeText(this, "请输入IP地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.queryIpInfo(ip);
                break;
        }
    }


    @Override
    public void showIpInfo(IpBean ipBean) {
        String ipInfo = String.format("这里是用MVP模式结合RxJava获取的数据，IP是：%s，我在%s%s%s", ipBean.ip, ipBean.country, ipBean.region, ipBean.city);
        mIpInfoText.setText(ipInfo);
    }

    @Override
    public void showError(Throwable e) {
        String err = "很遗憾失败了，错误是：" + e.toString();
        mIpInfoText.setText(err);
    }


}
