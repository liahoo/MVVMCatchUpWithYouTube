package com.aguosoft.app.videocamp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aguosoft.app.videocamp.R;


public class BaseFragment extends Fragment {
    protected String TAG = getClass().getSimpleName();

    public View findViewById(int id) {
        View fragment_view = getView();
        if (fragment_view != null) {
            return fragment_view.findViewById(id);
        } else {
            logI("[findViewById] failed! getView() is null");
            return null;
        }
    }

    protected Intent registerReceiverSave(BroadcastReceiver receiver,
                                          IntentFilter filter) {
        return getActivity().getApplicationContext().registerReceiver(receiver,
                filter);
    }

    protected void unregisterReceiverSave(BroadcastReceiver receiver) {
        try {
            getActivity().getApplicationContext().unregisterReceiver(receiver);
        } catch (Exception e) {
            logW("Unable to unregister receiver: " + e.getMessage());
        }
    }

    public void setVisibility(int visibility) {
        View fragment_view = getView();
        if (fragment_view != null) {
            fragment_view.setVisibility(visibility);
        }
    }

    public void setVisibilityForChild(int vid, int visibility) {
        setVisibilityForChild(findViewById(vid), visibility);
    }

    public void setVisibilityForChild(View view, int visibility) {
        if (view != null)
            view.setVisibility(visibility);
    }

    private BroadcastReceiver mIntentReceiver = null;

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        onBroadcastFilterAddAction(intentFilter);
        if (intentFilter.countActions() > 0) {
            mIntentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onBroadcastReceived(context, intent);
                }
            };
            registerReceiverSave(mIntentReceiver, intentFilter);
        } else {
            mIntentReceiver = null;
        }
    }

    @Override
    public void onStop() {
        if (mIntentReceiver != null) {
            unregisterReceiverSave(mIntentReceiver);
            mIntentReceiver = null;
        }
        super.onStop();
    }

    protected void onBroadcastReceived(Context context, Intent intent) {
        logI("[onBroadcastReceived] action:" + intent.getAction());
    }

    protected void onBroadcastFilterAddAction(IntentFilter intentFilter) {

    }

    protected ActionBar getToolbar(){
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
    protected void setTitle(@StringRes int textRes) {
        ActionBar toolbar = getToolbar();
        if (toolbar != null)
            toolbar.setTitle(textRes);
    }
    protected void setTitle(CharSequence text) {
        ActionBar toolbar = getToolbar();
        if (toolbar != null)
            toolbar.setTitle(text);
    }

    protected void showArgsError() {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setMessage(R.string.error_args_error)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
    public void logI(String text){
        if(AppConfig.SHOW_LOG)
            Log.i(TAG, text);
    }
    public void logW(String text){
        if(AppConfig.SHOW_LOG)
            Log.w(TAG, text);
    }
    public void logD(String text){
        if(AppConfig.SHOW_LOG)
            Log.d(TAG, text);
    }
    public void logE(String text){
        if(AppConfig.SHOW_LOG)
            Log.e(TAG, text);
    }
}
