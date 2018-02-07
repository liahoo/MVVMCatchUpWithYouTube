package com.aguosoft.app.videocamp.model;

import android.os.Bundle;

import com.aguosoft.app.videocamp.fragment.ChannelAboutFragment;

/**
 * Created by liang on 2016/10/07.
 */

public class ChannelAbout extends BasePager {

    private final long mSubscriberCount;
    private final long mViewCount;
    private final String mDescription;

    public ChannelAbout(CharSequence title, String description, long subscriberCount, long viewCount) {
        super(title);
        mDescription = description;
        mSubscriberCount = subscriberCount;
        mViewCount = viewCount;
    }

    @Override
    public Object getPageContent() {
        ChannelAboutFragment channelAboutFragment = new ChannelAboutFragment();
        Bundle args = new Bundle();
        args.putString(ChannelAboutFragment.ARG_DESCRIPTION, mDescription);
        args.putLong(ChannelAboutFragment.ARG_SUBSCRIBER_COUNT, mSubscriberCount);
        args.putLong(ChannelAboutFragment.ARG_VIEW_COUNT, mViewCount);
        channelAboutFragment.setArguments(args);
        return channelAboutFragment;
    }
}
