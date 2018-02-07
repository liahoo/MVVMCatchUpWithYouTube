package com.aguosoft.app.videocamp.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by liang on 2016/09/02.
 */
public class BindingHolder extends RecyclerView.ViewHolder {
    ViewDataBinding Binding;

    public ViewDataBinding getBinding() {
        return Binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.Binding = binding;
    }

    public BindingHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        setBinding(binding);
    }
}