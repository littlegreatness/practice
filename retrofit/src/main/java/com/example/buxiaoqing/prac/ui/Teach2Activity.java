package com.example.buxiaoqing.prac.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


/**
 * 教程一：进阶篇：接口实体类封装</br>
 * 本教程演示如何封装实体类来获取统一的数据</br>
 * 这里是教程，所以布局的等等资源，会出现很多冗余，是为了让广大网友更加明白，教程产生的布局可能一样
 *
 * @author ysbing
 */
public class Teach2Activity extends Activity implements View.OnClickListener {

    private MyHandler handler;
    private Context mContext;

    //IP地址
    private EditText mIpText;
    //请求的结果，IP信息
    private TextView mIpInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach2);
        mContext = this;
        handler = new MyHandler();
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
                retrofitExecute();
                break;
        }
    }


    /**
     * retrofit使用Execute方法获取数据，要开启线程
     */
    private void retrofitExecute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = mIpText.getText().toString();
                if (ip.length() == 0) {
                    Toast.makeText(mContext, "请输入IP地址", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", apiBean);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                ApiBean<IpBean> apiBean = (ApiBean<IpBean>) bundle.getSerializable("data");
                String ipInfo = String.format("这里是阻塞的方式获取的数据，IP是：%s，我在%s%s%s", apiBean.data.ip, apiBean.data.country, apiBean.data.region, apiBean.data.city);
                mIpInfoText.setText(ipInfo);
            }
        }
    }


}
