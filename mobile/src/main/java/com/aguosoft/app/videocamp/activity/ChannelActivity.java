package com.aguosoft.app.videocamp.activity;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.adapter.ChannelFragmentPagerAdatper;
import com.aguosoft.app.videocamp.adapter.SimpleFragmentPagerAdapter;
import com.aguosoft.app.videocamp.databinding.ActivityChannelBinding;
import com.aguosoft.app.videocamp.model.ChannelAbout;
import com.aguosoft.app.videocamp.model.ChannelFeaturedChannels;
import com.aguosoft.app.videocamp.model.ChannelPlaylists;
import com.aguosoft.app.videocamp.model.ChannelUploads;
import com.aguosoft.app.videocamp.model.VMChannel;
import com.aguosoft.library.youtube.YouTubeHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelSectionListResponse;

import java.io.IOException;
import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {

    public static final String EXTRA_CHANNEL_ID = "channel_id";
    private VMChannel mVMChannel = new VMChannel();
    private ActivityChannelBinding binding;
    private AsyncTask<String, Void, ChannelSectionListResponse> fetchingDetailTask;
    private AsyncTask<String, Void, ChannelListResponse> fetchingNameTask;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        binding.setVmChannel(mVMChannel);
        mVMChannel.id.set(getIntent().getStringExtra(EXTRA_CHANNEL_ID));
        if (null == mVMChannel.id.get()) {
            Toast.makeText(this, R.string.error_no_channel_id, Toast.LENGTH_LONG).show();
            finish();
        }

        fetchChannelDetail(mVMChannel.id.get());

//        loadChannelDetail("UCxjXU89x6owat9dA8Z-bzdw");
        viewPager = (ViewPager) binding.contentLayout.getRoot().findViewById(R.id.view_pager);
        tabLayout = (TabLayout) binding.contentLayout.getRoot().findViewById(R.id.tab_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.channel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Call it after Channel's information has been loaded.
     */
    private void initViewPager() {
        ArrayList<SimpleFragmentPagerAdapter.Pagerable> pagerList = new ArrayList<>();
        pagerList.add(new ChannelUploads( getString(R.string.uploads), mVMChannel.uploadsPlaylistId.get()));
        pagerList.add(new ChannelPlaylists(getString(R.string.playlists), mVMChannel.id.get()));
        pagerList.add(new ChannelFeaturedChannels(getString(R.string.channels), mVMChannel.featuredChannelsTitle.get(), mVMChannel.featuredChannelsIds));
        pagerList.add(new ChannelAbout(getString(R.string.about), mVMChannel.description.get(), mVMChannel.subscriberCount.get(), mVMChannel.viewCount.get()));
        viewPager.setAdapter(new ChannelFragmentPagerAdatper(getSupportFragmentManager(), pagerList));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void fetchChannelDetail(String channelId) {
        if (fetchingNameTask == null) {
            fetchingNameTask = new AsyncTask<String, Void, ChannelListResponse>(){
                @Override
                protected void onPostExecute(ChannelListResponse channelListResponse) {
                    super.onPostExecute(channelListResponse);
                    try {
                        onResponse(channelListResponse.getItems().get(0));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    fetchingDetailTask = null;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    fetchingNameTask = null;
                }

                @Override
                protected ChannelListResponse doInBackground(String... params) {
                    YouTube youtube = YouTubeHelper.createBuilder().setApplicationName(getString(R.string.app_name)).build();
                    try {
                        YouTube.Channels.List request = youtube.channels().list("snippet, brandingSettings, contentDetails, statistics");
                        request.setKey(YouTubeHelper.API_KEY);
                        request.setFields("items(id, brandingSettings(channel(featuredChannelsTitle,featuredChannelsUrls), image(bannerMobileImageUrl)), snippet(title,description,thumbnails(default)), contentDetails(relatedPlaylists(uploads)),statistics)");
                        request.setId(params[0]);
                        return request.execute();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            fetchingNameTask.execute(channelId);
        }
    }

    private void onResponse(Channel channel) {
        mVMChannel.set(channel);
        initViewPager();
    }
}
