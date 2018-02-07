package com.aguosoft.app.videocamp.activity;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.adapter.VideoListAdapter;
import com.aguosoft.app.videocamp.databinding.ActivityPlayerBinding;
import com.aguosoft.app.videocamp.model.VMVideo;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hu on 2016/10/26.
 */

public abstract class PlayerActivity extends AppCompatActivity implements YouTubePlayer.PlayerStateChangeListener {
    protected final String TAG = getClass().getSimpleName();
    protected ActivityPlayerBinding dataBinding;
    protected YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        init(dataBinding);
        initPlayerFragment();
    }

    protected void init(ActivityPlayerBinding dataBinding) {

    }

    protected void onVideoChanged(Video newVideo){
        dataBinding.setVideo(new VMVideo().set(newVideo));
    }
    protected void initRelatedVideosList(RecyclerView rvPlayingVideos) {
        rvPlayingVideos.setAdapter(new VideoListAdapter(dataBinding.rvPlayingVideos));
    }

    protected void initPlayerFragment() {
        YouTubePlayerSupportFragment playerSupportFragment = new YouTubePlayerSupportFragment();
        playerSupportFragment.initialize(YouTubeHelper.API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                onPlayerCreated(provider,youTubePlayer);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if(AppConfig.SHOW_LOG)
                    Log.e(TAG, youTubeInitializationResult.name());
                youTubeInitializationResult.getErrorDialog(PlayerActivity.this, 1).show();
            }
        });
        addOrReplaceFragment(playerSupportFragment, R.id.layout_player, "player", false);
    }

    protected void onPlayerCreated(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer){
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayer.setPlayerStateChangeListener(this);
    }

    protected void play(List<String> videoIds){
        this.youTubePlayer.loadVideos(videoIds);
    }
    protected void play(String videoId){
        this.youTubePlayer.loadVideo(videoId);
    }
    protected boolean addOrReplaceFragment(Fragment fragment, int dest_container,
                                           String name, boolean addToBackStack) {
        if (fragment == null || isFinishing()) {
            return false;
        }
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(dest_container) == null)
            ft.add(dest_container, fragment);
        else
            ft.replace(dest_container, fragment);

        if (addToBackStack) {
            ft.addToBackStack(name);
        }
        ft.commitAllowingStateLoss();// Prevent bugs by calling after onSaveInstance.
        return true;
    }

    protected AsyncTask<String, Void, VideoListResponse> loadVideoList(Collection<String> videoIdArray) {
        if (videoIdArray == null || videoIdArray.size() < 1) {
            if (AppConfig.SHOW_LOG)
                Log.e(TAG, "[loadVideoList] videoIdArray is empty");
            return null;
        }
        StringBuilder videoIds = new StringBuilder();
        Iterator<String> it = videoIdArray.iterator();
        while (it.hasNext()) {
            videoIds.append(it.next()).append(',');
        }
        return loadVideoList(videoIds.substring(0, videoIds.length() - 2));
    }

    protected AsyncTask<String, Void, VideoListResponse> loadVideoList(String videoIds) {
        AsyncTask<String, Void, VideoListResponse> task = new AsyncTask<String, Void, VideoListResponse>() {
            @Override
            protected void onPostExecute(VideoListResponse videoListResponse) {
                super.onPostExecute(videoListResponse);
                if (AppConfig.SHOW_LOG)
                    Log.d(TAG, "[onPostExecute]");
                if (videoListResponse != null) {
                    onVideoListLoadSuccess(this, videoListResponse);
                } else {
                    onVideoListLoadFailed(this, false);
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if (AppConfig.SHOW_LOG)
                    Log.d(TAG, "[onCancelled]");
                onVideoListLoadFailed(this, true);
            }

            @Override
            protected VideoListResponse doInBackground(String... params) {
                YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(getString(R.string.app_name)).build();
                try {
                    YouTube.Videos.List request = youtube.videos().list("snippet,contentDetails,statistics");
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
        task.execute(videoIds);
        return task;
    }


    protected void onVideoListLoadSuccess(AsyncTask<String, Void, VideoListResponse> asyncTask, VideoListResponse videoListResponse) {
        if (AppConfig.SHOW_LOG)
            Log.w(TAG, "[onVideoListLoadSuccess]");
    }

    protected void onVideoListLoadFailed(AsyncTask<String, Void, VideoListResponse> asyncTask, boolean isCanceled) {
        if (AppConfig.SHOW_LOG)
            Log.w(TAG, "[No videos loaded] isCanceled=" + isCanceled);
    }

    @Override
    public void onLoading() {
        if(AppConfig.SHOW_LOG)
            Log.i(TAG,"[onLoading]");
    }

    @Override
    public void onLoaded(String videoId) {
        if (AppConfig.SHOW_LOG)
            Log.i(TAG, "[onLoaded] " + videoId);

    }

    @Override
    public void onAdStarted() {
        if(AppConfig.SHOW_LOG)
            Log.i(TAG,"[onAdStarted]");

    }

    @Override
    public void onVideoStarted() {
        if(AppConfig.SHOW_LOG)
            Log.i(TAG,"[onVideoStarted]");

    }

    @Override
    public void onVideoEnded() {
        if(AppConfig.SHOW_LOG)
            Log.i(TAG,"[onVideoEnded]");
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        if (AppConfig.SHOW_LOG)
            Log.i(TAG, "[onError] " + errorReason);

    }
}
