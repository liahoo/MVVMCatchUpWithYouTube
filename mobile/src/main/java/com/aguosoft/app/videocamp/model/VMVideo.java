package com.aguosoft.app.videocamp.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.aguosoft.app.videocamp.utils.StringHelper;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;

import java.math.BigInteger;

/**
 * Created by hu on 2016/09/27.
 */

public class VMVideo {
    public ObservableField<String> id = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> publishedAt = new ObservableField<>();
    public ObservableField<String> imagePath = new ObservableField<>();
    public ObservableField<String> duration = new ObservableField<>();
    public ObservableField<String> viewCount = new ObservableField<>();
    public ObservableField<String> likeCount = new ObservableField<>();
    public ObservableField<String> commentCount = new ObservableField<>();
    public ObservableField<String> channelTitle = new ObservableField<>();
    public ObservableField<String> channelId = new ObservableField<>();
    public ObservableBoolean isPlaying = new ObservableBoolean(false);

    public VMVideo() {
    }

    public VMVideo set(PlaylistItem playlistItem) {
        PlaylistItemSnippet videoSnippet = playlistItem.getSnippet();
        ResourceId resId = videoSnippet.getResourceId();
        this.id.set(resId.getVideoId());
        this.title.set(videoSnippet.getTitle());
        this.publishedAt.set(DateTimeUtils.format(videoSnippet.getPublishedAt()));
        this.imagePath.set(parseImageUrl(videoSnippet.getThumbnails()));
        return this;
    }

    public VMVideo set(Video video){
        id.set(video.getId());
        title.set(video.getSnippet().getTitle());
        publishedAt.set(DateTimeUtils.format(video.getSnippet().getPublishedAt()));
        imagePath.set(parseImageUrl(video.getSnippet().getThumbnails()));
        duration.set(parseDuration(video.getContentDetails().getDuration()));
        viewCount.set(parseCount(video.getStatistics().getViewCount()));
        likeCount.set(parseCount(video.getStatistics().getLikeCount()));
        commentCount.set(parseCount(video.getStatistics().getCommentCount()));
        channelTitle.set(video.getSnippet().getChannelTitle());
        channelId.set(video.getSnippet().getChannelId());
        return this;
    }

    private String parseCount(BigInteger viewCount) {
        return viewCount.toString();
    }

    private String parseDuration(String duration) {
        return StringHelper.convertTimeStringPnYnMnDTnHnMnS(duration);
    }

    private String parseImageUrl(ThumbnailDetails thumbnailDetails){
        try {
            return thumbnailDetails.getMedium().getUrl();
        }catch (NullPointerException exception){
            return null;
        }
    }
}
