package com.prac.buxiaoqing.greendao;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.prac.buxiaoqing.greendao.bean.User;
import com.prac.buxiaoqing.greendao.gen.UserDao;

import java.util.List;

public class MainActivity extends Activity {


    private UserDao mUerDao;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUerDao = MyApplication.getInstances().getDaoSession().getUserDao();
    }


    public void create(View view) {
        mUser = new User((long) 10086, "prac");
        mUerDao.insert(mUser);
    }


    public void query(View view) {
        List<User> users = mUerDao.loadAll();
        String name = "";
        for (int i = 0; i < users.size(); i++) {
            name += users.get(i).getName() + ",";
        }
        print("MainActivity", "query:" + name);
    }

    public void remove(View view) {
        mUerDao.deleteAll();
    }

    public void update(View view) {
        mUser = new User((long) 10010, "prac");
        mUerDao.insert(mUser);
    }


    private void print(String tag, String msg) {
        Log.e(tag, msg);
    }
}
