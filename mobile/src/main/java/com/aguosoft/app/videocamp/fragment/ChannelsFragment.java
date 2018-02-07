package com.aguosoft.app.videocamp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.activity.ChannelActivity;
import com.aguosoft.app.videocamp.adapter.BindingListAdapter;
import com.aguosoft.app.videocamp.adapter.ChannelGridAdapter;
import com.aguosoft.app.videocamp.model.VMChannelSnippet;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 2016/10/07.
 */

public class ChannelsFragment extends BaseFragment implements BindingListAdapter.OnItemClickListener {
    public static final String ARG_CHANNEL_IDS = "channel_ids";
    protected ArrayList<String> mChannelIds;
    protected RecyclerView recyclerView;
    protected AsyncTask<String, Void, ChannelListResponse> fetchingChannelsTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initArguments();
        initViews();
        initContent();
    }

    protected void initArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mChannelIds = args.getStringArrayList(ARG_CHANNEL_IDS);
        } else {
            showArgsError();
        }
    }

    protected void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
        ChannelGridAdapter adapter = new ChannelGridAdapter(recyclerView);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    protected void initContent() {
        if (fetchingChannelsTask == null) {
            fetchingChannelsTask = new AsyncTask<String, Void, ChannelListResponse>() {
                @Override
                protected void onPostExecute(ChannelListResponse channelListResponse) {
                    super.onPostExecute(channelListResponse);
                    onResponse(channelListResponse);
                    fetchingChannelsTask = null;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    fetchingChannelsTask = null;
                }

                @Override
                protected ChannelListResponse doInBackground(String... params) {
                    YouTube youtube = YouTubeHelper.createBuilder()
                            .setApplicationName(getString(R.string.app_name)).build();
                    try {
                        YouTube.Channels.List request = youtube.channels().list("snippet");
                        request.setKey(YouTubeHelper.API_KEY);
                        request.setId(params[0]);
                        request.setMaxResults(30L);
                        return request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            String channelIds = makeParamIds();
            fetchingChannelsTask.execute(channelIds);
        }
    }

    private void onResponse(ChannelListResponse channelListResponse) {
        logI("[onResponse]");
        List<Channel> channels = channelListResponse.getItems();
        if (channels != null && channels.size() > 0) {
            logI("[onResponse] Load "+channels.size() + " channels");
            ChannelGridAdapter adapter = (ChannelGridAdapter) recyclerView.getAdapter();
            for (Channel channel : channels) {
                VMChannelSnippet vmChannelSnippet = new VMChannelSnippet().set(channel);
                adapter.add(vmChannelSnippet);
            }
            adapter.notifyDataSetChanged();
        }else
            logW("[onResponse] nothing loaded");
    }

    private String makeParamIds() {
        if (mChannelIds != null && mChannelIds.size() > 0) {
            StringBuffer result = new StringBuffer();
            for (String id : mChannelIds) {
                result.append(id).append(',');
            }
            return result.substring(0, result.length() - 2);
        }
        return null;
    }

    @Override
    public void onItemClick(View view, int position) {
        ChannelGridAdapter adapter = (ChannelGridAdapter) recyclerView.getAdapter();
        VMChannelSnippet channel = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), ChannelActivity.class);
        intent.putExtra(ChannelActivity.EXTRA_CHANNEL_ID, channel.id.get());
        getActivity().startActivity(intent);
    }
}
