package com.aguosoft.app.videocamp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.api.services.youtube.model.VideoListResponse;

/**
 * Created by hu on 2016/10/26.
 */

public class SingleVideoPlayerActivity extends PlayerActivity {
    public static final String EXTRA_VIDEO_ID = "video_id";
    private String currVideoId;
    private AsyncTask<String, Void, VideoListResponse> fetchingVideoTask;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void onPlayerCreated(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer) {
        youTubePlayer.loadVideo(getVideoId());
    }

    protected String getVideoId() {
        if (currVideoId == null)
            currVideoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        return currVideoId;
    }

    protected void init() {
        currVideoId = getVideoId();
        if (currVideoId == null) {
            if (AppConfig.SHOW_LOG)
                Log.e(TAG, "EXTRA_VIDEO_ID is not set!");
        }else{
            fetchingVideoTask = loadVideoList(currVideoId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchingVideoTask != null) {
            fetchingVideoTask.cancel(true);
            fetchingVideoTask = null;
        }
    }
}
