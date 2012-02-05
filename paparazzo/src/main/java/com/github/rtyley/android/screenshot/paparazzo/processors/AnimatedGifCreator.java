package com.github.rtyley.android.screenshot.paparazzo.processors;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class AnimatedGifCreator implements ScreenshotProcessor {

    private final AnimatedGifEncoder animatedGifEncoder;
    private final File file;

    public AnimatedGifCreator(File file) {
        this.file = file;
        animatedGifEncoder = new AnimatedGifEncoder();
        animatedGifEncoder.setDelay(500);
    }

    @Override
    public void process(BufferedImage image, Map<String, String> requestData) {
        if (!animatedGifEncoder.isStarted()) {
            animatedGifEncoder.start(file.getAbsolutePath());
        }
        animatedGifEncoder.addFrame(image);
    }

    @Override
    public void finish() {
        animatedGifEncoder.finish();
    }

}
