package com.aguosoft.app.videocamp.adapter;

import android.support.v7.widget.RecyclerView;

import com.aguosoft.app.videocamp.BR;
import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.model.VMChannel;

import java.util.Collection;

/**
 * Created by liang on 2016/10/06.
 */

public class ChannelListAdapter extends BaseGridAdapter<VMChannel> {
    public ChannelListAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public ChannelListAdapter(RecyclerView recyclerView, Collection<VMChannel> list) {
        super(recyclerView, list);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.list_item_channel;
    }

    @Override
    protected int getVariableId() {
        return BR.channel;
    }
}
