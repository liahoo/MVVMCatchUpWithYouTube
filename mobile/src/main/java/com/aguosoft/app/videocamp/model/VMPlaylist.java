package com.aguosoft.app.videocamp.model;

import android.databinding.ObservableField;
import android.databinding.ObservableLong;

import com.google.api.services.youtube.model.Playlist;

/**
 * Created by hu on 2016/09/27.
 */

public class VMPlaylist {
    public ObservableField<String> id = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> imagePath = new ObservableField<>();
    public ObservableField<String> updatedAt = new ObservableField<>();
    public ObservableLong videoCount = new ObservableLong();

    public VMPlaylist(){

    }

    public VMPlaylist set(Playlist playlist){
        id.set(playlist.getId());
        name.set(playlist.getSnippet().getTitle());
        imagePath.set(playlist.getSnippet().getThumbnails().getMedium().getUrl());
        videoCount.set(playlist.getContentDetails().getItemCount());
        updatedAt.set(DateTimeUtils.format(playlist.getSnippet().getPublishedAt()));
        return this;
    }
}
