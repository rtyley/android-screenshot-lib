package com.github.rtyley.android.screenshot.celebrity;

import static java.lang.Thread.sleep;

import android.util.Log;

public class Screenshots {

    /**
     * The paparazzo process filters logcat for this tag
     */
    public static final String TAG = "screenshot_request";
    
    public static void poseForScreenshot() {
        /* Note that the log message can not be blank, otherwise it won't
         * register with logcat.
         */
        Log.d(TAG, "{}");

        /* Wait for the development machine to take the screenshot (usually takes about
         * half a second)
         */
        try { sleep(1000L); } catch (InterruptedException e) {}
    }

}
