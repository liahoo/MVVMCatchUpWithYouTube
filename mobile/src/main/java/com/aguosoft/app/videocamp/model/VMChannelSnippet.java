package com.aguosoft.app.videocamp.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelSnippet;

/**
 * Created by hu on 2016/09/27.
 */

public class VMChannelSnippet extends BaseObservable{
    public ObservableField<String> id = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> thumbnailImageUrl = new ObservableField<>();

    public VMChannelSnippet set(Channel channel){
        id.set(channel.getId());
        ChannelSnippet snippet = channel.getSnippet();
        title.set(snippet.getTitle());
        thumbnailImageUrl.set(snippet.getThumbnails().getMedium().getUrl());
        return this;
    }
}
