package com.prac.buxiaoqing.meitudemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.bumptech.glide.request.animation.GlideAnimation;

import java.util.ArrayList;

/**
 * author：buxiaoqing on 8/19/16 14:42
 * Just do IT(没有梦想,何必远方)
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    private ArrayList<Fragment> fragments;
    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
        //super.destroyItem(container, position, object);
    }
}
