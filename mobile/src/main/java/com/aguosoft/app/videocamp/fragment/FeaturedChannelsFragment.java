package com.aguosoft.app.videocamp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aguosoft.app.videocamp.R;

/**
 * Created by liang on 2016/10/07.
 */

public class FeaturedChannelsFragment extends ChannelsFragment {
    public static final String ARG_TITLE = "title";
    private String mTitle;
    private TextView channelTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.channel_featured_channels, container, false);
    }

    protected void initArguments() {
        super.initArguments();
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString(ARG_TITLE);
        } else {
            showArgsError();
        }
    }

    protected void initViews() {
        super.initViews();
        channelTitle= (TextView) findViewById(R.id.channels_title);
        channelTitle.setText(mTitle);
    }
}
