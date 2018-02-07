package com.aguosoft.app.videocamp.model;

import com.google.api.client.util.DateTime;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by liang on 2016/10/07.
 */

public class DateTimeUtils {
    public static String format(DateTime rawPublishedAt){
        Calendar calendar = Calendar.getInstance();
        if(rawPublishedAt!=null) {
            calendar.setTimeInMillis(rawPublishedAt.getValue());
        }
        return String.format(Locale.getDefault(), "%4d-%02d-%02d %02d:%02d",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

    }
}
