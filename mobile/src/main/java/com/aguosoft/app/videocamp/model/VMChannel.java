package com.aguosoft.app.videocamp.model;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelBrandingSettings;
import com.google.api.services.youtube.model.ChannelContentDetails;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;

/**
 * Created by hu on 2016/09/27.
 */

public class VMChannel {
    public ObservableField<String> id = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> thumbnailImageUrl = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<String> bannerImageUrl = new ObservableField<>();
    public ObservableArrayList<String> featuredChannelsIds = new ObservableArrayList<>();
    public ObservableField<String> featuredChannelsTitle = new ObservableField<>();
    public ObservableField<String> uploadsPlaylistId = new ObservableField<>();
    public ObservableLong subscriberCount = new ObservableLong();
    public ObservableLong viewCount = new ObservableLong();
    public ObservableLong videoCount = new ObservableLong();

    public VMChannel set(Channel channel){
        id.set(channel.getId());
        setSnippet(channel.getSnippet());
        setBrandingSettings(channel.getBrandingSettings());
        setContentDetails(channel.getContentDetails());
        setStatistics(channel.getStatistics());
        return this;
    }

    public void setStatistics(ChannelStatistics statistics) {
        if(statistics!=null) {
            subscriberCount.set(statistics.getSubscriberCount().longValue());
            viewCount.set(statistics.getViewCount().longValue());
            videoCount.set(statistics.getVideoCount().longValue());
        }
    }

    public void setContentDetails(ChannelContentDetails contentDetails) {
        if(contentDetails!=null) {
            uploadsPlaylistId.set(contentDetails.getRelatedPlaylists().getUploads());
        }
    }

    public void setBrandingSettings(ChannelBrandingSettings brandingSettings) {
        if(brandingSettings!=null) {
            bannerImageUrl.set(brandingSettings.getImage().getBannerMobileImageUrl());
            featuredChannelsTitle.set(brandingSettings.getChannel().getFeaturedChannelsTitle());
            featuredChannelsIds.addAll(brandingSettings.getChannel().getFeaturedChannelsUrls());
        }
    }

    public void setSnippet(ChannelSnippet snippet) {
        if(snippet!=null) {
            title.set(snippet.getTitle());
            description.set(snippet.getDescription());
            thumbnailImageUrl.set(snippet.getThumbnails().getDefault().getUrl());
        }
    }
}
