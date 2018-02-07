package com.aguosoft.app.videocamp.adapter;

import android.support.v7.widget.RecyclerView;

import com.aguosoft.app.videocamp.model.VMVideo;

import java.util.Collection;

/**
 * Created by liang on 2016/09/28.
 */

public abstract class VideosAdapter extends BindingListAdapter<VMVideo> {
    public VideosAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public VideosAdapter(RecyclerView recyclerView, Collection<VMVideo> list) {
        super(recyclerView, list);
    }

    public void setCursorIndex(int index) {
        for (int i = 0; i < getItemCount(); i++) {
            if (i != index)
                getItem(i).isPlaying.set(false);
            else
                getItem(i).isPlaying.set(true);
        }
    }
}
