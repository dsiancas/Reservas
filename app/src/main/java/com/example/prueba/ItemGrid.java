package com.example.prueba;

import android.graphics.Bitmap;


public class ItemGrid {
    int status;
    Bitmap image;
    String title;

    public ItemGrid(Bitmap image, String title, int st) {
        super();
        this.image = image;
        this.title = title;
        this.status = st;
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


    public int getStatus() {
        return this.status;
    }
    public void setStatus(int st) {
        this.status = st;
    }


}