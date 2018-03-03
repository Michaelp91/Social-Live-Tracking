package com.slt.statistics.achievements;

import android.graphics.Bitmap;

/**
 * class representing images. This allowes to bundle more information and functions
 * together with the bitmap
 */
public class ImageItem {
    /**
     * image itself
     */
    private Bitmap image;

    /**
     * title of the image
     */
    private String title;

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
