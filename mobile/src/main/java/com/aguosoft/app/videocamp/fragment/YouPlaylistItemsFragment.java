package com.aguosoft.app.videocamp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.adapter.BindingListAdapter;
import com.aguosoft.app.videocamp.adapter.VideoListAdapter;
import com.aguosoft.app.videocamp.model.VMVideo;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;
import java.util.List;


/**
 * Created by hu on 2016/09/27.
 */

public class YouPlaylistItemsFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private BindingListAdapter.OnItemClickListener mOnItemClickListener =
            new BindingListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
            };
    private AsyncTask<String, Void, PlaylistItemListResponse> fetchingTask;
    private String nextPageToken;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (AppConfig.SHOW_LOG)
            Log.d(TAG, "[onActivityCreated]");
        initPlaylistItems("PL55713C70BA91BD6E");
        initRecyclerView();
    }

    private void initPlaylistItems(String playlistId) {
        if (fetchingTask == null) {
            fetchingTask = new AsyncTask<String, Void, PlaylistItemListResponse>() {
                @Override
                protected void onPostExecute(PlaylistItemListResponse playlistItemListResponse) {
                    super.onPostExecute(playlistItemListResponse);
                    if (AppConfig.SHOW_LOG)
                        Log.d(TAG, "[onPostExecute]");

                    fetchingTask = null;
                    handleResopnse(playlistItemListResponse);
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    if (AppConfig.SHOW_LOG)
                        Log.d(TAG, "[onCancelled]");
                    fetchingTask = null;
                }

                @Override
                protected PlaylistItemListResponse doInBackground(String... params) {
                    YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(getString(R.string.app_name)).build();
                    try {
                        YouTube.PlaylistItems.List request = youtube.playlistItems().list("snippet");
                        request.setKey(YouTubeHelper.API_KEY);
                        request.setPlaylistId(params[0]);
                        request.setMaxResults(10L);
                        if (params.length > 1)
                            request.setPageToken(params[1]);
                        return request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            fetchingTask.execute(playlistId, nextPageToken);
        }
    }

    private void handleResopnse(PlaylistItemListResponse playlistItems) {
        nextPageToken = playlistItems.getNextPageToken();
        VideoListAdapter adapter = (VideoListAdapter) mRecyclerView.getAdapter();
        List<PlaylistItem> items = playlistItems != null ? playlistItems.getItems() : null;
        if (items != null && items.size() > 0) {
            if (AppConfig.SHOW_LOG)
                Log.w(TAG, "[handleResopnse] Playlist has " + items.size() + " items");
            for (PlaylistItem item : items) {
                adapter.add(new VMVideo().set(item));
            }
            adapter.notifyDataSetChanged();
        } else {
            if (AppConfig.SHOW_LOG)
                Log.w(TAG, "[handleResopnse] Playlist has no items");
        }
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(createAdapter());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private VideoListAdapter createAdapter() {
        VideoListAdapter adapter = new VideoListAdapter(mRecyclerView);
        adapter.setOnItemClickListener(mOnItemClickListener);
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_videos, null, false);
        setHasOptionsMenu(true);
        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (menu != null)
//            menu.clear();
//        inflater.inflate(R.menu.me, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int item_id = item.getItemId();
//        if(R.id.action_search == item_id){
//            getActivity().onSearchRequested();
//        }else if(R.id.action_date == item_id){
//            gotoPickupDate();
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchingTask != null) {
            fetchingTask.cancel(true);
            fetchingTask = null;
        }
    }
}
