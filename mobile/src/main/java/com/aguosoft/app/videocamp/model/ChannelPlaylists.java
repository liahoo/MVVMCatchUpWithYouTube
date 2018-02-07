package com.aguosoft.app.videocamp.model;

import android.os.Bundle;

import com.aguosoft.app.videocamp.fragment.PlaylistsFragment;

/**
 * Created by liang on 2016/10/07.
 */

public class ChannelPlaylists extends BasePager{
    private final String mChannelId;

    public ChannelPlaylists(CharSequence title, String channelId) {
        super(title);
        mChannelId = channelId;
    }

    @Override
    public Object getPageContent() {
        PlaylistsFragment fragment = new PlaylistsFragment();
        Bundle args = new Bundle();
        args.putString(PlaylistsFragment.ARG_CHANNEL_ID, mChannelId);
        fragment.setArguments(args);
        return fragment;
    }
}
