package com.github.rtyley.android.screenshot.paparazzo.processors;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface ScreenshotProcessor {

    public void process(BufferedImage image, Map<String, String> requestData);

    public void finish();

}
