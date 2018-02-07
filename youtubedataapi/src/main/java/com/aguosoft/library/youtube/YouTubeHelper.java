package com.aguosoft.library.youtube;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;

/**
 * Created by liang on 2016/09/28.
 */

public class YouTubeHelper {

    public static final String API_KEY = "AIzaSyBQzhU5v2R6gGX5FvT95A06L8WLGvjH5dY";

    public static YouTube.Builder createBuilder(){
        return createBuilder(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        });
    }

    public static YouTube.Builder createBuilder(HttpRequestInitializer httpRequestInitializer){
        return new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), httpRequestInitializer);
    }
}
