package com.github.rtyley.android.screenshot.paparazzo;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.RawImage;
import com.github.rtyley.android.screenshot.paparazzo.processors.ScreenshotProcessor;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnDemandScreenshotService {

    private final IDevice device;
    private final List<ScreenshotProcessor> processors;
    private final ScreenshotRequestListener logListenerCommandShell;

    public OnDemandScreenshotService(IDevice device, ScreenshotProcessor... processors) {
        this.device = device;
        this.processors = asList(processors);
        this.logListenerCommandShell = new ScreenshotRequestListener();
    }

    /**
     * Start receiving and acting on paparazzo requests from the device.
     */
    public void start() {
        Thread thread = new Thread(new LogCatCommandExecutor());
        thread.setName(getClass().getSimpleName() + " logcat for " + device.getSerialNumber());
        thread.start();

        try {
            sleep(500); // ignore old output that logcat feeds us
        } catch (InterruptedException e) {
        }

        logListenerCommandShell.activate();
    }

    /**
     * Stop receiving and acting on paparazzo requests.
     * <p/>
     * This class is not re-usable after finish() is called.
     */
    public void finish() {
        if (logListenerCommandShell != null) {
            logListenerCommandShell.cancelled = true;
        }
        for (ScreenshotProcessor screenshotProcessor : processors) {
            screenshotProcessor.finish();
        }
    }

    private class LogCatCommandExecutor implements Runnable {
        /**
         * Reads only the 'main' log buffer, specifically to exclude the 'system' log - the system log can
         * be extremely slow to cat on some devices, and it doesn't even have the 'screenshot_request'
         * tag we're interested in.
         */
        private final static String LOGCAT_COMMAND = "logcat -b main screenshot_request:D *:S";

        @Override
        public void run() {
            try {
                device.executeShellCommand(LOGCAT_COMMAND, logListenerCommandShell, 0);
            } catch (Exception e) {
                // log.error("Exception launching screenshot-request-listening logcat.", e);
            }
        }
    }

    private class ScreenshotRequestListener extends MultiLineReceiver {
        private boolean activated = false, cancelled = false;

        public ScreenshotRequestListener() {
            setTrimLine(false);
        }

        public void activate() {
            activated = true;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void processNewLines(String[] lines) {
            if (!activated || cancelled) {
                // log.debug("Ignoring "+lines+" lines of log");
                return;
            }

            String mostRecentLogLine = lines[lines.length - 1];

            takeScreenshotFor(mostRecentLogLine); // whatever the earlier requests corresponded to, that stuff is gone
        }
    }

    private void takeScreenshotFor(String logLine) {
        RawImage rawImage;
        try {
            rawImage = device.getScreenshot();
        } catch (Exception e) {
            // log.warn("Exception getting raw image data for screenshot", e);
            return;
        }

        if (rawImage == null) {
            // log.warn("No image data returned for screenshot");
            return;
        }

        Map<String, String> keyValueMap = keyValueMapFor(logLine);
        BufferedImage image = bufferedImageFrom(rawImage);

        for (ScreenshotProcessor screenshotProcessor : processors) {
            screenshotProcessor.process(image, keyValueMap);
        }
    }

    private static final Pattern KEY_VALUE_HOLDER_PATTERN = Pattern.compile("\\{(.*)\\}$");

    /**
     * Parse the logline into key-value pairs. The logline format is comma-separated
     * key-value pairs surrounded by curly braces, ie:
     *
     * {foo=bar,name=ARandomName}
     *
     * The logline can also be prefixed with data like '02-28 11:37:49.629 D/screenshot_request(26711): '
     * depending on what you pass to the '-v' logcat option
     */
    static Map<String, String> keyValueMapFor(String logLine) {
        Map<String, String> keyValueMap = new HashMap<String, String>();
        Matcher matcher = KEY_VALUE_HOLDER_PATTERN.matcher(logLine);
        if (matcher.find()) {
            for (String keyValuePair : matcher.group(1).split(",")) {
                int separatorIndex = keyValuePair.indexOf("=");
                if (separatorIndex > 0) {
                    String key = keyValuePair.substring(0, separatorIndex);
                    String value = keyValuePair.substring(separatorIndex + 1);
                    keyValueMap.put(key, value);
                }
            }
        }
        return keyValueMap;
    }

    private static BufferedImage bufferedImageFrom(RawImage rawImage) {
        BufferedImage image = new BufferedImage(rawImage.width, rawImage.height, TYPE_INT_ARGB);

        int index = 0;
        int bytesPerPixel = rawImage.bpp >> 3;
        for (int y = 0; y < rawImage.height; y++) {
            for (int x = 0; x < rawImage.width; x++) {
                image.setRGB(x, y, rawImage.getARGB(index) | 0xff000000);
                index += bytesPerPixel;
            }
        }
        return image;
    }

}
