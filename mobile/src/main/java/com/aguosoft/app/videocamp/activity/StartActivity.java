package com.aguosoft.app.videocamp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.aguosoft.app.videocamp.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private static final int SPLASH_TIME_OUT = 1;
    private final Handler mHideHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case SPLASH_TIME_OUT:
//                    startMainActivity();
                    gotoChannel("UC1pHFqCMAIHP8gr4lYGtNLA");
                    break;
            }
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        mHideHandler.sendEmptyMessageDelayed(SPLASH_TIME_OUT, 1000);
    }


    private void gotoChannel(String channelId){
        Intent intent = new Intent(this, ChannelActivity.class);
        intent.putExtra(ChannelActivity.EXTRA_CHANNEL_ID, channelId);
        startActivity(intent);
        finish();
    }

}
