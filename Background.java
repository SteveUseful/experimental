package com.example.a726224.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Steve Silliker march 2017 Prototype game.
 */

public class Background { // Background class. May change as I design the app

    private Bitmap image; // image for BG
    private int x, y, dx; // coords

    public Background(Bitmap res) // setting the image to res to use later
    {
        image = res;
    }
    public void update() // update for game panel if = < GamePanel width
    {
        x+=dx;
        if(x<-GamePanel.WIDTH){
            x=0;
        }
    }

    public void draw(Canvas canvas) // drawing canvas with bg image with x and y; z set to null
    {
        canvas.drawBitmap(image, x, y,null);
        if(x<0)
        {
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y, null);
        }
    }

    public void setVector(int dx) // Vector positioning
    {
        this.dx = dx;
    }
}
