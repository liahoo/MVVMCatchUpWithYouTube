package com.aguosoft.app.videocamp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.databinding.GridItemVideoBinding;
import com.aguosoft.app.videocamp.model.VMVideo;

import java.util.Collection;

/**
 * Created by liang on 2016/09/28.
 */

public class VideoGridAdapter extends VideosAdapter {
    public VideoGridAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public VideoGridAdapter(RecyclerView recyclerView, Collection<VMVideo> list) {
        super(recyclerView, list);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridItemVideoBinding binding = GridItemVideoBinding.inflate(getLayoutInflater(), parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        GridItemVideoBinding binding = (GridItemVideoBinding) holder.getBinding();
        binding.setVideo(getItem(position));
    }
}
