package com.aguosoft.app.videocamp.model;

import android.os.Bundle;

import com.aguosoft.app.videocamp.fragment.PlaylistVideosFragment;

/**
 * Created by liang on 2016/10/07.
 */

public class ChannelUploads extends BasePager {

    private final String mPlaylistId ;

    public ChannelUploads(CharSequence title, String playlistId) {
        super(title);
        mPlaylistId = playlistId;
    }

    @Override
    public Object getPageContent() {
        PlaylistVideosFragment playlistVideosFragment = new PlaylistVideosFragment();
        Bundle args = new Bundle();
        args.putString(PlaylistVideosFragment.ARG_PLAYLIST_ID, mPlaylistId);
        args.putCharSequence(PlaylistVideosFragment.ARG_PLAYLIST_NAME, getTitle());
        playlistVideosFragment.setArguments(args);
        return playlistVideosFragment;
    }
}
