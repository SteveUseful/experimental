package com.example.a726224.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import static android.R.attr.animation;
import static android.R.attr.height;
import static android.R.attr.width;

/**
 * Created by 726224 on 3/10/2017.
 */

public class Bosses extends GameObject // Created new class for Bosses that will appear every 1000 points. This class is not functioning right now, I'm working on the Graphics.
{
    private int score; // Player score.
    private int speed; // Objects speed.
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet; // Bitmap

    public Bosses(Bitmap res, int x, int y, int w, int h, int s, int numFrames) // Resources for Bitmap image.
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;
        Bitmap[] image = new Bitmap[numFrames];

        speed = 3 + (int) (rand.nextDouble()*score/10); // Random + next speed, adding x (int), called from super.
        if(speed>20)speed = 25; //capping missile speed and setting it to 25 if under 20.


        spritesheet = res; // set spritesheet to bitmap

        for(int i = 0; i<image.length;i++) // adding the image over and over again.
        {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image); // setting animation for the frames
        animation.setDelay(100-speed); // setting speed delay.

    }

    public void update() // update speed of animation.
    {
        x-=speed;
        animation.update();
    }
    public void draw(Canvas canvas) // draw the canvas with a try/catch to catch the exception.
    {
        try
        {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }

        catch(Exception e)
        {
            // leave this blank. Can put message here if I want to, probably just leave it alone.
        }
    }

    @Override
    public int getWidth() // Collision detection for bosses. They will be bigger images so the collision will be different than regular.
    {
        return width-12; //collision detection. Set differently for bosses.
    }

}
