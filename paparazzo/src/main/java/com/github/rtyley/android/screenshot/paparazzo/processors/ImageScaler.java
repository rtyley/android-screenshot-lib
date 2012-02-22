package com.github.rtyley.android.screenshot.paparazzo.processors;

import com.github.rtyley.android.screenshot.paparazzo.processors.util.Dimensions;

import java.awt.image.BufferedImage;
import java.util.Map;

import static com.github.rtyley.android.screenshot.paparazzo.processors.util.Images.imageToBufferedImage;
import static com.github.rtyley.android.screenshot.paparazzo.processors.util.Images.scaleTo;

public class ImageScaler implements ScreenshotProcessor {

    private final ScreenshotProcessor delegate;
    private final Dimensions maxBounds;

    public ImageScaler(ScreenshotProcessor delegate, Dimensions maxBounds) {
        this.delegate = delegate;
        this.maxBounds = maxBounds;
    }

    @Override
    public void process(BufferedImage image, Map<String, String> request) {
        delegate.process(ensureWithinBounds(image), request);
    }

    private BufferedImage ensureWithinBounds(BufferedImage image) {
        Dimensions imageDimensions = new Dimensions(image.getWidth(), image.getHeight());

        if (maxBounds.contains(imageDimensions)) {
            return image;
        }

        return imageToBufferedImage(scaleTo(imageDimensions.scaledPreservingAspectRatioToFitWithin(maxBounds), image));
    }

    @Override
    public void finish() {
        delegate.finish();
    }

}
