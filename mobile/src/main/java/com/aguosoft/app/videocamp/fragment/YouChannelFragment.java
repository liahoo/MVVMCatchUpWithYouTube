package com.aguosoft.app.videocamp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.databinding.FragmentYouChannelBinding;
import com.aguosoft.app.videocamp.model.ChannelSectionType;
import com.aguosoft.app.videocamp.model.VMChannel;
import com.aguosoft.app.videocamp.model.VMSectionSinglePlaylist;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelSection;
import com.google.api.services.youtube.model.ChannelSectionListResponse;
import com.google.api.services.youtube.model.ChannelSectionSnippet;

import java.io.IOException;
import java.util.List;

/**
 * Created by hu on 2016/09/27.
 */

public class YouChannelFragment extends BaseFragment {
    public static final java.lang.String ARG_CHANNEL_ID = "channel_id";
    public static final java.lang.String ARG_CHANNEL_NAME = "channel_name";
    private AsyncTask<String, Void, ChannelSectionListResponse> fetchingTask;
    private FragmentYouChannelBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYouChannelBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadChannelDetail();
    }

    private void loadChannelDetail() {
        Bundle args = getArguments();
        setTitle(args.getString(ARG_CHANNEL_NAME));
        callApi(args.getString(ARG_CHANNEL_ID));
    }

    private void callApi(String channelId) {
        if (fetchingTask == null) {
            fetchingTask = new AsyncTask<String, Void, ChannelSectionListResponse>() {
                @Override
                protected void onPostExecute(ChannelSectionListResponse channelSectionListResponse) {
                    super.onPostExecute(channelSectionListResponse);
                    handleResponse(channelSectionListResponse);
                    fetchingTask = null;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    fetchingTask = null;
                }

                @Override
                protected ChannelSectionListResponse doInBackground(String... params) {
                    YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(getString(R.string.app_name)).build();
                    try {
                        YouTube.ChannelSections.List request = youtube.channelSections().list("snippet, contentDetails");
                        request.setKey(YouTubeHelper.API_KEY);
                        request.setChannelId(params[0]);
                        return request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            fetchingTask.execute(channelId);
        }
    }
    private void handleResponse(ChannelSectionListResponse response) {
        List<ChannelSection> sectionList = response.getItems();

        for(ChannelSection section : sectionList){
            ChannelSectionSnippet snippet = section.getSnippet();
            ChannelSectionType sectionType = ChannelSectionType.parse(snippet.getType());
            switch(sectionType){
                case SinglePlaylist:
                    binding.sectionGroup.addView(VMSectionSinglePlaylist.from(section).createView(getActivity()));
                    break;
            }
            VMChannel vmChannel = new VMChannel();
        }
    }
}
