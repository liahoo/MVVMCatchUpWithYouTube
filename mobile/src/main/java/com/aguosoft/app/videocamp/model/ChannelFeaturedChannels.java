package com.aguosoft.app.videocamp.model;

import android.os.Bundle;

import com.aguosoft.app.videocamp.fragment.FeaturedChannelsFragment;

import java.util.ArrayList;

/**
 * Created by liang on 2016/10/07.
 */

public class ChannelFeaturedChannels extends BasePager {
    private final String mFeaturedChannelsTitle;
    private final ArrayList<String> mFeaturedChannelsIds;

    public ChannelFeaturedChannels(CharSequence title, String featuredChannelsTitle, ArrayList<String> featuredChannelsIds) {
        super(title);
        mFeaturedChannelsTitle = featuredChannelsTitle;
        mFeaturedChannelsIds = featuredChannelsIds;
    }

    @Override
    public Object getPageContent() {
        FeaturedChannelsFragment fragment = new FeaturedChannelsFragment();
        Bundle args = new Bundle();
        args.putString(FeaturedChannelsFragment.ARG_TITLE, mFeaturedChannelsTitle);
        args.putStringArrayList(FeaturedChannelsFragment.ARG_CHANNEL_IDS, mFeaturedChannelsIds);
        fragment.setArguments(args);
        return fragment;
    }
}
