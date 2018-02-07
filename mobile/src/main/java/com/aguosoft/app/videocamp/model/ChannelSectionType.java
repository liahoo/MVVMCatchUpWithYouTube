package com.aguosoft.app.videocamp.model;

/**
 * Created by liang on 2016/10/05.
 */

public enum ChannelSectionType {
    None,
    SinglePlaylist,
    MultiplePlaylists,
    RecentUploads,
    MultipleChannels;
    public static ChannelSectionType parse(String rawType){
        switch(rawType) {
            case "multipleChannels":
                return MultipleChannels;
            case "recentUploads":
                return RecentUploads;
            case "singlePlaylist":
                return SinglePlaylist;
            case "multiplePlaylists":
                return MultiplePlaylists;
            default:
                return None;
        }
    }
}
