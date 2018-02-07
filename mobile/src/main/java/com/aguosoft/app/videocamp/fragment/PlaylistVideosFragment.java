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
import com.aguosoft.app.videocamp.activity.*;
import com.aguosoft.app.videocamp.adapter.BindingListAdapter;
import com.aguosoft.app.videocamp.adapter.VideoGridAdapter;
import com.aguosoft.app.videocamp.adapter.VideoListAdapter;
import com.aguosoft.app.videocamp.adapter.VideosAdapter;
import com.aguosoft.app.videocamp.model.VMVideo;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.List;


/**
 * Created by liang on 2016/10/06.
 */

public class PlaylistVideosFragment extends BaseFragment implements BindingListAdapter.OnItemClickListener {

    public static final String ARG_PLAYLIST_ID = "playlist_id";
    public static final String ARG_PLAYLIST_NAME = "playlist_name";
    public static final String ARG_STYLE = "style";
    public static final int ARG_STYLE_GRID = 0;
    public static final int ARG_STYLE_LIST = 1;
    private String mPlaylistId;
    private RecyclerView recyclerView;
    private int mStyle;
    private AsyncTask<String, Void, PlaylistItemListResponse> fetchingTask;
    private AsyncTask<String, Void, VideoListResponse> fetchingVideoTask;
    private String nextPageToken = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.recyclerview, null, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!readArguments()) {
            return;
        }
        initRecyclerView();
        loadPlaylistItems(getPlaylistId());
    }

    private String getPlaylistId() {
        if(mPlaylistId==null){
            readArguments();
        }
        return mPlaylistId;
    }

    private boolean readArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mPlaylistId = args.getString(ARG_PLAYLIST_ID);
            mStyle = args.getInt(ARG_STYLE, ARG_STYLE_GRID);
            return mPlaylistId != null;
        }
        return false;
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        VideosAdapter videosAdapter;
        if (ARG_STYLE_LIST == mStyle) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            videosAdapter = new VideoListAdapter(recyclerView);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
            videosAdapter = new VideoGridAdapter(recyclerView);
        }
        videosAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(videosAdapter);
    }
    private void loadPlaylistItems(String playlistId) {
        if (playlistId == null) {
            return;
        }
        if (fetchingTask == null) {
            fetchingTask = new AsyncTask<String, Void, PlaylistItemListResponse>() {
                @Override
                protected void onPostExecute(PlaylistItemListResponse playlistItemListResponse) {
                    fetchingTask = null;
                    super.onPostExecute(playlistItemListResponse);
                    if(playlistItemListResponse!=null) {
                        List<PlaylistItem> items = playlistItemListResponse.getItems();
                        if (AppConfig.SHOW_LOG)
                            Log.d(TAG, "[onPostExecute] "+items.size() + " items loaded!");
                        if (items != null && items.size() > 0) {
                            String[] videoIds = new String[items.size()];
                            for (int i = 0; i < videoIds.length; i++) {
                                videoIds[i] = items.get(i).getSnippet().getResourceId().getVideoId();
                            }
                            loadVideos(videoIds);
                        } else {
                            if (com.aguosoft.app.videocamp.activity.AppConfig.SHOW_LOG)
                                Log.w(TAG, "[No items loaded]");
                        }
                        nextPageToken = playlistItemListResponse.getNextPageToken();
                    }else{
                        if (AppConfig.SHOW_LOG)
                            Log.w(TAG, "[onPostExecute]playlistItemListResponse is null");
                    }
                }

                @Override
                protected void onCancelled() {
                    fetchingTask = null;
                    super.onCancelled();
                    if (com.aguosoft.app.videocamp.activity.AppConfig.SHOW_LOG)
                        Log.d(TAG, "[onCancelled]");
                }

                @Override
                protected PlaylistItemListResponse doInBackground(String... params) {
                    YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(getString(R.string.app_name)).build();
                    try {
                        YouTube.PlaylistItems.List request = youtube.playlistItems().list("snippet");
                        request.setKey(YouTubeHelper.API_KEY);
                        request.setPlaylistId(params[0]);
                        request.setMaxResults(20L);
                        if (params.length > 1)
                            request.setPageToken(params[1]);
                        request.setFields("items/snippet/resourceId/videoId");
                        return request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            fetchingTask.execute(playlistId, nextPageToken);
        }
    }

    protected void loadVideos(String[] videoIdArray) {
        if(videoIdArray==null || videoIdArray.length<1){
            if (AppConfig.SHOW_LOG)
                Log.e(TAG, "[loadVideoList] videoIdArray is empty");
        }
        StringBuffer videoIds = new StringBuffer();
        videoIds.append(videoIdArray[0]);
        for(int i=1;i<videoIdArray.length;i++){
            videoIds.append(',').append(videoIdArray[i]);
        }
        loadVideos(videoIds.toString());
    }

    protected void loadVideos(String videoIds) {
        if(fetchingVideoTask==null) {
            if (AppConfig.SHOW_LOG)
                Log.e(TAG, "[loadVideoList] videoIds=" + videoIds);
            fetchingVideoTask = new AsyncTask<String, Void, VideoListResponse>() {
                @Override
                protected void onPostExecute(VideoListResponse videoListResponse) {
                    fetchingVideoTask = null;
                    super.onPostExecute(videoListResponse);
                    if (com.aguosoft.app.videocamp.activity.AppConfig.SHOW_LOG)
                        Log.d(TAG, "[onPostExecute]");
                    if (videoListResponse != null) {
                        onResponse(videoListResponse);
                    } else {
                        if (com.aguosoft.app.videocamp.activity.AppConfig.SHOW_LOG)
                            Log.w(TAG, "[No videos loaded]");
                    }
                }

                @Override
                protected void onCancelled() {
                    fetchingVideoTask = null;
                    super.onCancelled();
                    if (com.aguosoft.app.videocamp.activity.AppConfig.SHOW_LOG)
                        Log.d(TAG, "[onCancelled]");
                }

                @Override
                protected VideoListResponse doInBackground(String... params) {
                    YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(getString(R.string.app_name)).build();
                    try {
                        YouTube.Videos.List request = youtube.videos().list("id,snippet,contentDetails,statistics");
                        request.setKey(YouTubeHelper.API_KEY);
                        request.setMaxResults(20L);
                        request.setId(params[0]);
                        request.setFields("items(contentDetails/duration,id,snippet(channelId,channelTitle,publishedAt,thumbnails/medium,title),statistics(commentCount,likeCount,viewCount))");
                        return request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            fetchingVideoTask.execute(videoIds);
        }
    }

    private void onResponse(VideoListResponse videoListResponse) {
        List<Video> items = videoListResponse.getItems();
        if (items != null && items.size() > 0) {
            if (AppConfig.SHOW_LOG)
                Log.w(TAG, "[handleResopnse] Playlist has " + items.size() + " items");
            VideosAdapter adapter = (VideosAdapter) recyclerView.getAdapter();
            if (adapter == null)
                adapter = new VideoListAdapter(recyclerView);
            for (Video video : items) {
                adapter.add(new VMVideo().set(video));
            }
            adapter.notifyDataSetChanged();
        } else {
            if (AppConfig.SHOW_LOG)
                Log.w(TAG, "[onResponse] videoListResponse has no items");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchingTask != null) {
            fetchingTask.cancel(true);
            fetchingTask = null;
        }
        if (fetchingVideoTask != null) {
            fetchingVideoTask.cancel(true);
            fetchingVideoTask = null;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        playVideo(position);
    }

    protected void playVideo(int position) {
        Intent intent = new Intent(getActivity(), PlaylistPlayerActivity.class);
        intent.putExtra(PlaylistPlayerActivity.EXTRA_PLAYLIST_ID, mPlaylistId);
        intent.putExtra(PlaylistPlayerActivity.EXTRA_START_INDEX, position);
        startActivity(intent);
    }
}
