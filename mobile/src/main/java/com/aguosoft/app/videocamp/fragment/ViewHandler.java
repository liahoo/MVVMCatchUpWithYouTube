package com.aguosoft.app.videocamp.fragment;

import android.util.Log;
import android.view.View;

import com.aguosoft.app.videocamp.BuildConfig;


/**
 * Created by liang on 2016/09/05.
 */
public class ViewHandler implements View.OnClickListener {

    private static final String TAG = ViewHandler.class.getSimpleName();

    public void onClickEvent(View view){
        if(BuildConfig.LOG_ENABLED)
            Log.i(TAG, "[onClickEvent] eventId: ");
    }

    public void onClickEvent(View view, int eventId){
        if(BuildConfig.LOG_ENABLED)
            Log.i(TAG, "[onClickEvent] eventId: "+eventId);
    }

    public void onClickEventLike(View view){
        if(BuildConfig.LOG_ENABLED)
            Log.i(TAG, "[onClickEventLike] eventId: ");

    }

    @Override
    public void onClick(View view) {
        if(BuildConfig.LOG_ENABLED)
            Log.i(TAG, "[onClick] eventId: ");

    }
}
