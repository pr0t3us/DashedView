package com.androidpositive.dashedview;

import android.content.Context;

/**
 * @author Serhio
 */
public class ScreenUtils {

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
