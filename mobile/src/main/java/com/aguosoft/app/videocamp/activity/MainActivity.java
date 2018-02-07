package com.aguosoft.app.videocamp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.aguosoft.app.videocamp.R;
import com.aguosoft.app.videocamp.fragment.CategorySettingsFragment;
import com.aguosoft.app.videocamp.fragment.YouChannelFragment;
import com.aguosoft.app.videocamp.fragment.YouPlaylistItemsFragment;
import com.aguosoft.app.videocamp.view.TabMenu;

/**
 * Created by hu on 2016/09/22.
 */

public class MainActivity extends AppCompatActivity implements TabMenu.TabMenuListener {
    private boolean isShowingExitToast;
    private Toast mExitToast;
    private TabMenu tabMenu;
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initTabHost();
        initTabMenu();
    }

    private void gotoChannel(String channelId){
        Intent intent = new Intent(this, ChannelActivity.class);
        intent.putExtra(ChannelActivity.EXTRA_CHANNEL_ID, channelId);
        startActivity(intent);
    }
    private void initTabHost() {
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.content_layout);
        tabHost.getTabWidget().setVisibility(View.GONE);
        tabHost.addTab(tabHost.newTabSpec("watch").setIndicator(getString(R.string.tab_watch)), YouPlaylistItemsFragment.class, null);
        Bundle argsChannel = new Bundle();
        argsChannel.putString(YouChannelFragment.ARG_CHANNEL_NAME, "AKB48");
        argsChannel.putString(YouChannelFragment.ARG_CHANNEL_ID, "UCxjXU89x6owat9dA8Z-bzdw");
        tabHost.addTab(tabHost.newTabSpec("upload").setIndicator(getString(R.string.tab_upload)), YouChannelFragment.class, argsChannel);
        tabHost.addTab(tabHost.newTabSpec("settings").setIndicator(getString(R.string.tab_settings)), CategorySettingsFragment.class, null);
    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
    }

    protected void initTabMenu() {
        tabMenu = (TabMenu) findViewById(R.id.tab_menu);
        tabMenu.setCurrentTab(0, false);
        tabMenu.setTabListener(this);
    }

    protected boolean addOrReplaceFragment(Fragment fragment, int dest_container,
                                           String name, boolean addToBackStack) {
        if (fragment == null || isFinishing()) {
            return false;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(dest_container) == null)
            ft.add(dest_container, fragment);
        else
            ft.replace(dest_container, fragment);

        if (addToBackStack) {
            ft.addToBackStack(name);
        }
        ft.commitAllowingStateLoss();// Prevent bugs by calling after onSaveInstance.
        return true;
    }

    @Override
    public void onTabChanged(int pos, boolean byUser) {
            tabHost.setCurrentTab(pos);
    }

}