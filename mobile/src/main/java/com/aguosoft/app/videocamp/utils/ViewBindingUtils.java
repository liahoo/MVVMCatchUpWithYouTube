package com.aguosoft.app.videocamp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by liang on 2016/09/02.
 */
public final class ViewBindingUtils {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl){
        Picasso.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }
//
//    @BindingAdapter({"playlistItems"})
//    public static void setPlaylistItems(LinearLayout parent, List<PlaylistItem> playlistItems) {
//        if (playlistItems != null && playlistItems.size() > 0) {
//            for (PlaylistItem playlistItem : playlistItems) {
//                PlaylistItemSnippet snippet = playlistItem.getSnippet();
//                if (snippet != null) {
//                    ItemPlaylistBinding itemPlaylistBinding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()));
//                    itemPlaylistBinding.setMPlaylistItem(new VMVideo().set(playlistItem.getSnippet()));
//                    parent.addView(itemPlaylistBinding.getRoot());
//                }
//            }
//        }
//    }
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
