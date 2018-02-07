package com.aguosoft.app.videocamp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.activity.PlaylistPlayerActivity;
import com.aguosoft.app.videocamp.adapter.BindingListAdapter;
import com.aguosoft.app.videocamp.adapter.PlaylistGridAdapter;
import com.aguosoft.app.videocamp.adapter.PlaylistListAdapter;
import com.aguosoft.app.videocamp.model.VMPlaylist;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created by liang on 2016/10/07.
 */

public class PlaylistsFragment extends BaseFragment implements BindingListAdapter.OnItemClickListener {
    public static final String ARG_CHANNEL_ID = "channel_id";
    public static final String ARG_STYLE = "style";
    public static final int ARG_STYLE_GRID = 0;
    public static final int ARG_STYLE_LIST = 1;
    private String channelId;
    private RecyclerView recyclerView;
    private AsyncTask<String, Void, PlaylistListResponse> fetchingPlaylistsTask;
    private String nextPageToken;
    private int mStyle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_playlists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    protected void initRecyclerView() {
        recyclerView = (RecyclerView) getView();
        if(ARG_STYLE_GRID == mStyle) {
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
            PlaylistGridAdapter adapter = new PlaylistGridAdapter(recyclerView);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(new PlaylistListAdapter(recyclerView));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if(null == args){
            showArgsError();
        }
        mStyle = args.getInt(ARG_STYLE, ARG_STYLE_GRID);
        channelId = args.getString(ARG_CHANNEL_ID);
        fetchPlaylists(channelId);
    }

    protected void fetchPlaylists(String channelId) {
        if(null == fetchingPlaylistsTask){
            fetchingPlaylistsTask = new AsyncTask<String, Void, PlaylistListResponse>(){

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    fetchingPlaylistsTask = null;
                }

                @Override
                protected void onPostExecute(PlaylistListResponse playlistListResponse) {
                    super.onPostExecute(playlistListResponse);
                    onResponse(playlistListResponse);
                    fetchingPlaylistsTask = null;
                }

                @Override
                protected PlaylistListResponse doInBackground(String... params) {
                    YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(getString(R.string.app_name)).build();
                    try {
                        YouTube.Playlists.List request = youtube.playlists().list("snippet, contentDetails");
                        request.setKey(YouTubeHelper.API_KEY);
                        request.setPageToken(nextPageToken);
                        request.setMaxResults(30L);
                        request.setChannelId(params[0]);
                        return request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            fetchingPlaylistsTask.execute(channelId);
        }
    }

    private void onResponse(PlaylistListResponse playlistListResponse) {
        if(AppConfig.SHOW_LOG)
            Log.i(TAG, "[onResponse]");
        nextPageToken = playlistListResponse.getNextPageToken();
        List<Playlist> playlists = playlistListResponse.getItems();
        if(playlists!=null && playlists.size()>0) {
            PlaylistGridAdapter adapter = (PlaylistGridAdapter) recyclerView.getAdapter();
            for (Playlist playlist : playlists ) {
                VMPlaylist vmPlaylist = new VMPlaylist().set(playlist);
                adapter.add(vmPlaylist);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        PlaylistGridAdapter adapter = (PlaylistGridAdapter) recyclerView.getAdapter();
        playPlaylist(adapter.getItem(position).id.get());
    }

    private void playPlaylist(String playlistId) {
        Intent intent = new Intent(getActivity(), PlaylistPlayerActivity.class);
        intent.putExtra(PlaylistPlayerActivity.EXTRA_PLAYLIST_ID, playlistId);
        startActivity(intent);
    }
}
