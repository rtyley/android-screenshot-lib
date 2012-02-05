package com.github.rtyley.android.screenshot.celebrity;

import static java.lang.Thread.sleep;

import android.util.Log;

public class Screenshots {

    public static void poseForScreenshot() {
        Log.d("screenshot_request", "");
        try { sleep(1000L); } catch (InterruptedException e) {}
    }

}
