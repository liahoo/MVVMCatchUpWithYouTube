package com.aguosoft.app.videocamp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.databinding.ListItemVideoBinding;
import com.aguosoft.app.videocamp.model.VMVideo;

import java.util.Collection;

/**
 * Created by liang on 2016/09/28.
 */

public class VideoListAdapter extends VideosAdapter {
    public VideoListAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public VideoListAdapter(RecyclerView recyclerView, Collection<VMVideo> list) {
        super(recyclerView, list);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemVideoBinding binding = ListItemVideoBinding.inflate(getLayoutInflater(), parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ListItemVideoBinding binding = (ListItemVideoBinding) holder.getBinding();
        binding.setVideo(getItem(position));
    }
}
