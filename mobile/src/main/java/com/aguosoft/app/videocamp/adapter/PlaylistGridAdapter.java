package com.aguosoft.app.videocamp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.databinding.GridItemPlaylistBinding;
import com.aguosoft.app.videocamp.model.VMPlaylist;

import java.util.Collection;

/**
 * Created by liang on 2016/09/28.
 */

public class PlaylistGridAdapter extends BindingListAdapter<VMPlaylist> {
    public PlaylistGridAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public PlaylistGridAdapter(RecyclerView recyclerView, Collection<VMPlaylist> list) {
        super(recyclerView, list);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridItemPlaylistBinding binding = GridItemPlaylistBinding.inflate(getLayoutInflater(), parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        GridItemPlaylistBinding binding = (GridItemPlaylistBinding) holder.getBinding();
        binding.setPlaylist(getItem(position));
    }
}
