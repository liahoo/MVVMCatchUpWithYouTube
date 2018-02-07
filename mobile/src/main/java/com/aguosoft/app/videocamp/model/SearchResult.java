package com.aguosoft.app.videocamp.model;

/**
 * Created by liang on 2016/10/06.
 */

public enum SearchResult {
    None,
    Playlist,
    Video,
    Channel;

    public static SearchResult parse(String rawResult){
        switch(rawResult){
            case "youtube#playlist":
                return Playlist;
            case "youtube#video":
                return Video;
            case "youtube#channel":
                return Channel;
            default:
                return None;
        }
    }
}
