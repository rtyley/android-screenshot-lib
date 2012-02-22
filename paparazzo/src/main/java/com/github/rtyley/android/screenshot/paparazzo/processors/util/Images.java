package com.github.rtyley.android.screenshot.paparazzo.processors.util;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Images {

    public static BufferedImage imageToBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        BufferedImage target = newImage(image.getWidth(null), image.getHeight(null));
        Graphics2D graphics = target.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return target;
    }

    public static BufferedImage newImage(int width, int height) {
        return new BufferedImage(width, height, TYPE_INT_RGB);
    }

    public static Image scaleTo(Dimensions scaledDimensions, BufferedImage image) {
        return image.getScaledInstance(scaledDimensions.width, scaledDimensions.height, SCALE_SMOOTH);
    }

}
