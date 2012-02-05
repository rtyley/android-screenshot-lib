package com.github.rtyley.android.screenshot.paparazzo.processors;

import static java.lang.System.currentTimeMillis;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ImageSaver implements ScreenshotProcessor {

    private final File screenshotDirectory;
    private final String fileNamePrefix;

    public ImageSaver(File screenshotDirectory, String fileNamePrefix) {
        this.screenshotDirectory = screenshotDirectory;
        this.fileNamePrefix = fileNamePrefix;
    }

    @Override
    public void process(BufferedImage image, Map<String, String> requestData) {
        File screenshotFile = new File(screenshotDirectory, fileNamePrefix + "-" + currentTimeMillis() + ".png");
        try {
            // log.info("Writing " + screenshotFile.getAbsolutePath());
            ImageIO.write(image, "png", screenshotFile);
        } catch (IOException e) {
        }
    }

    @Override
    public void finish() {
    }

}
