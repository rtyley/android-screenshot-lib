package com.github.rtyley.android.screenshot.paparazzo.processors.util;


import static java.lang.Math.round;

public class Dimensions {

    public final int width, height;
    public final double aspectRatio;

    public Dimensions(int width, int height) {
        this.width = width;
        this.height = height;
        aspectRatio = (double) width / height;
    }

    public static Dimensions square(int size) {
        return new Dimensions(size, size);
    }

    public Dimensions scaledPreservingAspectRatioToFitWithin(Dimensions bounds) {
        return bounds.aspectRatio > aspectRatio ? scaleToHeight(bounds.height) : scaleToWidth(bounds.width);
    }

    private Dimensions scaleToWidth(int targetWidth) {
        return new Dimensions(targetWidth, (int) round(targetWidth / aspectRatio));
    }

    private Dimensions scaleToHeight(int targetHeight) {
        return new Dimensions((int) round(targetHeight * aspectRatio), targetHeight);
    }

    public boolean contains(int w, int h) {
        return width >= w && height >= h;
    }

    public boolean contains(Dimensions otherDimensions) {
        return contains(otherDimensions.width, otherDimensions.height);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + toSimpleString() + "]";
    }

    public String toSimpleString() {
        return "" + width + "x" + height;
    }
}
