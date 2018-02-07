package com.aguosoft.app.videocamp.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.model.VMChannel;

/**
 * Created by liang on 2016/09/09.
 */
public class ChannelPagerAdapter extends PagerAdapter {
    private VMChannel vmChannel;
    public ChannelPagerAdapter(VMChannel channel) {
        vmChannel = channel;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View pager = null;
        switch (position) {
            case 0:
                pager = createVideosPager(container);
                break;
            case 1:
                pager = createPlaylistsPager(container);
                break;
            case 2:
                pager = createChannelsPager(container);
                break;
            case 3:
                pager = createAboutPager(container);
        }
        if (pager != null)
            container.addView(pager);
        return pager;
    }

    private View createAboutPager(ViewGroup container) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        return null;
    }

    private View createVideosPager(ViewGroup container) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.recyclerview, null, false);
        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));
//        recyclerView.setAdapter(new VideoGridAdapter(recyclerView, vmChannel.uploads));
        return recyclerView;
    }

    private View createPlaylistsPager(ViewGroup container) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.recyclerview, null, false);
        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));
//        recyclerView.setAdapter(new PlaylistGridAdapter(recyclerView, vmChannel.playlistList));
        return recyclerView;
    }

    private View createChannelsPager(ViewGroup container) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.recyclerview, null, false);
        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 3));
        recyclerView.setAdapter(new ChannelGridAdapter(recyclerView));
        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
}
