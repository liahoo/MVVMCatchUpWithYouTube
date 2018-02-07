package com.aguosoft.app.videocamp.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by liang on 2016/10/14.
 */

public class VMChannelAbout {
    public ObservableField<String> description = new ObservableField<>();
    public ObservableInt subscribers = new ObservableInt();
    public ObservableInt viewCount = new ObservableInt();
}
