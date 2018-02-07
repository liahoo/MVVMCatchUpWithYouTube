package com.aguosoft.app.videocamp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.databinding.GridItemChannelBinding;
import com.aguosoft.app.videocamp.model.VMChannelSnippet;

import java.util.Collection;

/**
 * Created by liang on 2016/09/28.
 */

public class ChannelGridAdapter extends BindingListAdapter<VMChannelSnippet> {
    public ChannelGridAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public ChannelGridAdapter(RecyclerView recyclerView, Collection<VMChannelSnippet> list) {
        super(recyclerView, list);
        setHasStableIds(true);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridItemChannelBinding binding = GridItemChannelBinding.inflate(getLayoutInflater(), parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        GridItemChannelBinding binding = (GridItemChannelBinding) holder.getBinding();
        binding.setChannelSnippet(getItem(position));
    }
}
