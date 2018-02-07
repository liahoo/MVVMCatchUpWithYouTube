package com.aguosoft.app.videocamp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.databinding.ListItemPlaylistBinding;
import com.aguosoft.app.videocamp.model.VMPlaylist;

import java.util.Collection;

/**
 * Created by liang on 2016/10/06.
 */
public class PlaylistListAdapter extends BindingListAdapter<VMPlaylist> {
    public PlaylistListAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public PlaylistListAdapter(RecyclerView recyclerView, Collection<VMPlaylist> list) {
        super(recyclerView, list);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemPlaylistBinding binding = ListItemPlaylistBinding.inflate(getLayoutInflater());
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ListItemPlaylistBinding binding = (ListItemPlaylistBinding) holder.getBinding();
        binding.setPlaylist(getItem(position));
    }
}
