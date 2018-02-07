package com.aguosoft.app.videocamp.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collection;

/**
 * Created by liang on 2016/10/06.
 */

public abstract class BaseGridAdapter<T> extends BindingListAdapter<T> {
    public BaseGridAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public BaseGridAdapter(RecyclerView recyclerView, Collection<T> list) {
        super(recyclerView, list);
    }

    protected abstract int getItemLayout();
    protected abstract int getVariableId();

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayout(), parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(getVariableId(), getItem(position));

    }

}
