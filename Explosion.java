package com.example.a726224.game;

/**
 * Created by Steve Silliker march 2017 Prototype game.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion  // This is what happens when you die or when collision is detected.
{
    private int x; // set coords
    private int y; // set coords

    private int width;
    private int height;

    private int row;

    private Animation animation = new Animation();
    private Bitmap spritesheet; // res

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames) // setting frames for explosion. Used code from other class
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        Bitmap[] image = new Bitmap[numFrames]; // Setting bitmap function

        spritesheet = res;

        for(int i = 0; i<image.length; i++)
        {
            if(i%5==0&&i>0)row++;
            image[i] = Bitmap.createBitmap(spritesheet, (i-(5*row))*width, row*height, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(5); // Shorter delay.



    }
    public void draw(Canvas canvas) //drawing canvas for explosions after collision
    {
        if(!animation.playedOnce()) // If the game is in retry mode/after first attempt
        {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }

    }
    public void update() // updating if explosion happens / collision detected
    {
        if(!animation.playedOnce())
        {
            animation.update(); // Update if playedOnce
        }
    }
    public int getHeight() // returning height ok.

    {
        return height;
    }
}

