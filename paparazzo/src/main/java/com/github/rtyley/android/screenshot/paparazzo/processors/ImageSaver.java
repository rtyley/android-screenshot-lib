package com.github.rtyley.android.screenshot.paparazzo.processors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.lang.String.format;

public class ImageSaver implements ScreenshotProcessor {

    private final File screenshotDirectory;
    private int screenshotCount = 0;

    public ImageSaver(File screenshotDirectory) {
        this.screenshotDirectory = screenshotDirectory;
    }

    @Override
    public void process(BufferedImage image, Map<String, String> request) {
        String name = request.containsKey("name") ? request.get("name") : format("%04d", screenshotCount++);

        File screenshotFile = new File(screenshotDirectory, name + ".png");
        try {
            ImageIO.write(image, "png", screenshotFile);
        } catch (IOException e) {
            // log error?
        }
    }

    @Override
    public void finish() {
        // No resources to dispose of
    }

}
