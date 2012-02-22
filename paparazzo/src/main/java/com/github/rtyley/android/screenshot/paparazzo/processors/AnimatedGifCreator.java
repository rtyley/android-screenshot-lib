package com.github.rtyley.android.screenshot.paparazzo.processors;

import com.madgag.gif.fmsware.AnimatedGifEncoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class AnimatedGifCreator implements ScreenshotProcessor {

    private final AnimatedGifEncoder gifEncoder;
    private final File file;

    public AnimatedGifCreator(File file) {
        this.file = file;
        gifEncoder = new AnimatedGifEncoder();
        gifEncoder.setDelay(500);
        gifEncoder.setRepeat(0);
    }

    @Override
    public void process(BufferedImage image, Map<String, String> requestData) {
        if (!gifEncoder.isStarted()) {
            gifEncoder.start(file.getAbsolutePath());
        }
        gifEncoder.addFrame(image);
    }

    @Override
    public void finish() {
        gifEncoder.finish();
    }

}
