package com.aguosoft.app.videocamp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aguosoft.app.videocamp.R;

import java.text.DecimalFormat;

/**
 * Created by liang on 2016/10/07.
 */

public class ChannelAboutFragment extends BaseFragment {

    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_SUBSCRIBER_COUNT = "subscriber_count";
    public static final String ARG_VIEW_COUNT = "view_count";
    private String mDescription;
    private TextView textViewDescription;
    private TextView textViewSubscribers;
    private TextView textViewViewCount;
    private long mSubscriberCount;
    private long mViewCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.channel_about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initArguments();
        initViews();
    }

    private void initArguments() {
        Bundle args = getArguments();
        if(args!=null) {
            mDescription = args.getString(ARG_DESCRIPTION);
            mSubscriberCount = args.getLong(ARG_SUBSCRIBER_COUNT);
            mViewCount = args.getLong(ARG_VIEW_COUNT);
        }
    }

    private void initViews() {
        textViewDescription = (TextView) findViewById(R.id.tv_description);
        textViewDescription.setText(mDescription);
        textViewSubscribers = (TextView) findViewById(R.id.tv_subscriber_count);
        textViewSubscribers.setText(formatNumber(mSubscriberCount));
        textViewViewCount = (TextView) findViewById(R.id.tv_view_count);
        textViewViewCount.setText(formatNumber(mViewCount));
    }

    protected String formatNumber(long count){
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        return decimalFormat.format(count);
    }
}
