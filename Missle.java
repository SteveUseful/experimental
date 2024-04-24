package com.example.a726224.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Steve Silliker march 2017 prototype game
 */

class Missile extends GameObject // Class for handling objects coming from player; In this class, I'm setting the Missles.
{

    private int score; // score and speed settings
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Missile(Bitmap res, int x, int y, int w, int h, int s, int numFrames) // Bitmap function for missle images
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = 3 + (int) (rand.nextDouble()*score/10);
        if(speed>20)speed = 20; // stopping missles at certain speed.

        Bitmap[] image = new Bitmap[numFrames]; // Bitmap with numFrames for Missles

        spritesheet = res; // calling res again

        for(int i = 0; i<image.length;i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed); // set delay speed

    }

    public void update() // updating the missles and their speed.
    {
        x-=speed;
        animation.update();
    }
    public void draw(Canvas canvas) // creating the canvas with a try/catch for an exception error.
    {
        try
        {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }
        catch(Exception e){} // catching a general exception
    }

    @Override
    public int getWidth() // Collision for regular objects. Set at 10 seems to be good for now. Bosses will be more.
    {
        return width-10; //collision detection
    }

}

