package com.aguosoft.app.videocamp.model;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.databinding.SectionSinglePlaylistBinding;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelSection;
import com.google.api.services.youtube.model.ChannelSectionSnippet;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;

/**
 * Created by liang on 2016/10/05.
 */

public class VMSectionSinglePlaylist extends VMSection {
    public ObservableField<String> id = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableArrayList<PlaylistItem> playlistItems= new ObservableArrayList<>();

    @Override
    public View createView(Context context) {
        SectionSinglePlaylistBinding binding = SectionSinglePlaylistBinding.inflate(LayoutInflater.from(context));
        binding.setVmSection(this);
        fetchItems(context, id.get());
        return binding.getRoot();
    }

    public static VMSectionSinglePlaylist from(ChannelSection section){
        VMSectionSinglePlaylist ins = new VMSectionSinglePlaylist();
        ChannelSectionSnippet snippet = section.getSnippet();
        ins.title.set(snippet.getTitle());
        ins.id.set(section.getContentDetails().getPlaylists().get(0));
        return ins;
    }

    public void fetchItems(final Context context, String playlistId){
        AsyncTask<String, Void, PlaylistItemListResponse> fetchingTask = new AsyncTask<String, Void, PlaylistItemListResponse>() {
            @Override
            protected void onPostExecute(PlaylistItemListResponse playlistItemListResponse) {
                super.onPostExecute(playlistItemListResponse);
                playlistItems.clear();
                playlistItems.addAll(playlistItemListResponse.getItems());
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected PlaylistItemListResponse doInBackground(String... params) {
                YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(context.getString(R.string.app_name)).build();
                try {
                    YouTube.PlaylistItems.List request = youtube.playlistItems().list("snippet, contentDetails");
                    request.setKey(YouTubeHelper.API_KEY);
                    request.setPlaylistId(params[0]);
                    return request.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        fetchingTask.execute(playlistId);

    }
}
