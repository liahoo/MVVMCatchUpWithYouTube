package com.aguosoft.app.videocamp.adapter;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by liang on 2016/10/06.
 */

public class ChannelFragmentPagerAdatper extends SimpleFragmentPagerAdapter<SimpleFragmentPagerAdapter.Pagerable> {

    public ChannelFragmentPagerAdatper(FragmentManager fm, ArrayList<Pagerable> pagerable_list) {
        super(fm, pagerable_list);
    }
}
