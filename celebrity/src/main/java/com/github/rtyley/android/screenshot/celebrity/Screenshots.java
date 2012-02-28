package com.github.rtyley.android.screenshot.celebrity;

import android.util.Log;

import static java.lang.Thread.sleep;

public class Screenshots {

    /**
     * The paparazzo process filters logcat for this tag
     */
    public static final String TAG = "screenshot_request";

    public static void poseForScreenshot() {
        poseForScreenshotWithKeyValueString("");
    }

    public static void poseForScreenshotNamed(String name) {
        poseForScreenshotWithKeyValue("name", name);
    }



    private static void poseForScreenshotWithKeyValue(String key, String value) {
        poseForScreenshotWithKeyValueString(key+"="+value);
    }

    private static void poseForScreenshotWithKeyValueString(String keyValueString) {
        /* Note that the log message can not be blank, otherwise it won't
         * register with logcat.
         */
        Log.d(TAG, "{"+keyValueString+"}");

        /* Wait for the development machine to take the screenshot (can take about
         * 900ms)
         */
        try { sleep(1000L); } catch (InterruptedException e) {}
    }

}
