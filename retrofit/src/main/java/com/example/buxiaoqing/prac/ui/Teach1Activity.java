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


import com.example.buxiaoqing.prac.BuildConfig;
import com.example.buxiaoqing.prac.R;
import com.example.buxiaoqing.prac.api.APIService;
import com.example.buxiaoqing.prac.bean.DemoBean;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * 教程一：入门篇：hello, world</br>
 * 本教程演示最基本的使用方法，使用的淘宝的一个接口，在玩的时候，不要点太快，我发现点太快容易超时，可能是淘宝做了限制
 *
 * @author ysbing
 */
public class Teach1Activity extends Activity implements View.OnClickListener {

    private MyHandler handler;
    private Context mContext;

    //IP地址
    private EditText mIpText;
    //请求的结果，IP信息
    private TextView mIpInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach1);
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
        findViewById(R.id.button_query_call_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_query_execute:
                retrofitExecute();
                break;
            case R.id.button_query_call_back:
                retrofitCallBack();
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
                Call<DemoBean> call = apiService.getIpInfo(ip);
                try {
                    Response<DemoBean> response = call.execute();
                    DemoBean demoBean = response.body();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", demoBean);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        if (BuildConfig.DEBUG) {


        }


    }

    /**
     * 回调使用
     */
    private void retrofitCallBack() {
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
        //绑定接口
        APIService apiService = retrofit.create(APIService.class);
        //预获得掉用后的返回对象
        Call<DemoBean> call = apiService.getIpInfo(ip);
        //开始调用
        call.enqueue(new Callback<DemoBean>() {
            @Override
            public void onResponse(Response<DemoBean> response) {
                DemoBean demoBean = response.body();
                String ipInfo = String.format("这里是用回调的方式获取的数据，IP是：%s，我在%s%s%s", demoBean.data.ip, demoBean.data.country, demoBean.data.region, demoBean.data.city);
                mIpInfoText.setText(ipInfo);
            }

            @Override
            public void onFailure(Throwable t) {
                String err = "很遗憾失败了，错误是：" + t.toString();
                mIpInfoText.setText(err);
            }
        });
        //移除请求
        //call.cancel();


    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                DemoBean demoBean = (DemoBean) bundle.getSerializable("data");
                String ipInfo = String.format("这里是阻塞的方式获取的数据，IP是：%s，我在%s%s%s", demoBean.data.ip, demoBean.data.country, demoBean.data.region, demoBean.data.city);
                mIpInfoText.setText(ipInfo);
            }
        }
    }
}