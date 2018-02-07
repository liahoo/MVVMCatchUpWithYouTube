package com.aguosoft.app.videocamp.model;

import com.aguosoft.app.videocamp.adapter.SimpleFragmentPagerAdapter;

/**
 * Created by liang on 2016/10/07.
 */

public abstract class BasePager implements SimpleFragmentPagerAdapter.Pagerable {
    private CharSequence mTitle;

    public BasePager(CharSequence title) {
        setTitle(title);
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    public CharSequence getTitle() {
        return mTitle;
    }
}
