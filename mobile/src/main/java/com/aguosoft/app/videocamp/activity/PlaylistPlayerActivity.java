package com.aguosoft.app.videocamp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.adapter.BindingListAdapter;
import com.aguosoft.app.videocamp.adapter.VideoListAdapter;
import com.aguosoft.app.videocamp.adapter.VideosAdapter;
import com.aguosoft.app.videocamp.databinding.ActivityPlayerBinding;
import com.aguosoft.app.videocamp.model.VMVideo;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 2016/10/26.
 */

public class PlaylistPlayerActivity extends PlayerActivity implements BindingListAdapter.OnItemClickListener {
    public static final String EXTRA_PLAYLIST_ID = "playlist_id";
    public static final String EXTRA_START_INDEX = "start_index";
    private String currPlaylistId;
    private AsyncTask<String, Void, PlaylistItemListResponse> fetchingTask;
    private String nextPageToken;
    private List<String> videoIds;
    private int nowPlayingIndex = 0;
    private AsyncTask<String, Void, VideoListResponse> fetchingRelatedVideosTask;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    protected void onPlayerCreated(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer) {
        super.onPlayerCreated(provider, youTubePlayer);
        play(0);
    }

    private void play(int index) {
        if (videoIds != null && youTubePlayer != null) {
            youTubePlayer.loadVideos(videoIds, nowPlayingIndex = index, 0);
            VideosAdapter adapter = (VideosAdapter) dataBinding.rvPlayingVideos.getAdapter();
            adapter.setCursorIndex(index);
        }
    }


    protected String getPlaylistId() {
        if (currPlaylistId == null)
            currPlaylistId = getIntent().getStringExtra(EXTRA_PLAYLIST_ID);
        return currPlaylistId;
    }

    @Override
    protected void init(ActivityPlayerBinding binding) {
        if (getPlaylistId() == null) {
            if (AppConfig.SHOW_LOG)
                Log.e(TAG, "EXTRA_PLAYLIST_ID is not set!");
        }else {
            binding.rvPlayingVideos.setLayoutManager(new LinearLayoutManager(this));
            VideoListAdapter adapter = new VideoListAdapter(dataBinding.rvPlayingVideos);
            binding.rvPlayingVideos.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            loadPlaylistItems(getPlaylistId());
        }
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
                    if (AppConfig.SHOW_LOG)
                        Log.d(TAG, "[onPostExecute]");
                    if(playlistItemListResponse!=null) {
                        List<PlaylistItem> items = playlistItemListResponse.getItems();
                        if (items != null && items.size() > 0) {
                            videoIds = new ArrayList<>();
                            for (int i = 0; i < items.size(); i++) {
                                videoIds.add(items.get(i).getSnippet().getResourceId().getVideoId());
                            }
                            fetchingRelatedVideosTask = loadVideoList(videoIds);
                        } else {
                            if (AppConfig.SHOW_LOG)
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
                    if (AppConfig.SHOW_LOG)
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
                        if (params.length > 1 && params[1]!=null)
                            request.setPageToken(params[1]);
                        request.setFields("items/snippet/resourceId/videoId");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchingTask != null) {
            fetchingTask.cancel(true);
            fetchingTask = null;
        }
        if (fetchingRelatedVideosTask != null) {
            fetchingRelatedVideosTask.cancel(true);
            fetchingRelatedVideosTask = null;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        play(position);
    }

    protected void onVideoListLoadSuccess(AsyncTask<String, Void, VideoListResponse> asyncTask, VideoListResponse videoListResponse) {
        if(asyncTask == fetchingRelatedVideosTask) {
            fetchingRelatedVideosTask=null;
            if (AppConfig.SHOW_LOG)
                Log.w(TAG, "[onVideoListLoadSuccess]");
            List<Video> items = videoListResponse.getItems();
            if (items != null && items.size() > 0) {
                if (AppConfig.SHOW_LOG)
                    Log.w(TAG, "[onVideoListLoadSuccess] Playlist has " + items.size() + " items");
                VideosAdapter adapter = (VideosAdapter) dataBinding.rvPlayingVideos.getAdapter();
                if (adapter == null) {
                    dataBinding.rvPlayingVideos.setAdapter(new VideoListAdapter(dataBinding.rvPlayingVideos));
                    adapter = (VideosAdapter) dataBinding.rvPlayingVideos.getAdapter();
                }
                for (Video video : items) {
                    adapter.add(new VMVideo().set(video));
                }
                adapter.notifyDataSetChanged();
            } else {
                if (AppConfig.SHOW_LOG)
                    Log.w(TAG, "[onVideoListLoadSuccess] videoListResponse has no items");
            }
            play(0);
        }else{
            if (AppConfig.SHOW_LOG)
                Log.w(TAG, "[onVideoListLoadSuccess] Task is not fetchingRelatedVideosTask");
            super.onVideoListLoadSuccess(asyncTask, videoListResponse);
        }
    }
    protected void onVideoListLoadFailed(AsyncTask<String, Void, VideoListResponse> asyncTask, boolean isCanceled) {
        if(asyncTask == fetchingRelatedVideosTask) {
            if (AppConfig.SHOW_LOG)
                Log.w(TAG, "[No videos loaded] isCanceled="+isCanceled);
            fetchingRelatedVideosTask=null;
        }else{
            super.onVideoListLoadFailed(asyncTask, isCanceled);
        }
    }
}
