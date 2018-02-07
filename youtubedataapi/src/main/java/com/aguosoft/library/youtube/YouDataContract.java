package com.aguosoft.library.youtube;

import android.net.Uri;

/**
 * Created by liang on 2016/10/05.
 */

public final class YouDataContract {

    /**
     * The authority of the youtube data provider.
     */
    public static final String AUTHORITY =
        "com.aguosoft.data.youtube.authority";

    /**
     * The content URI for the top-level
     * lentitems authority.
     */
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);


}
