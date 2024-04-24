package com.example.a726224.game;

/**
 * Created by Steve Silliker march 2017 prototype game
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.animation.Animation;

public class Player extends GameObject // Main Player class. Will hold all the player information.
{
    private Bitmap spritesheet;
    private int score;

    private boolean up;
    private boolean playing;
    private long startTime;

    private Animation animation = new Animation() // Passing the animation to the super class.
    {
        @Override
        public void start() {
            super.start();
        }
    };

    public Player(Bitmap res, int w, int h, int numFrames) // Resources for the player object.
    {

        x = 100;
        y = GamePanel.HEIGHT / 2; // Equals - GamePanel
        dy = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        startTime = System.nanoTime();
    }

    public void setUp(boolean b) // Up/Down - True/False
    {
        up = b;
    }

    public void update() // Updating the player and adding on the score
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {
            score++;
            startTime = System.nanoTime();
        }

        com.example.a726224.game.Animation.update(); // Full path for Animation update. Not sure why it wanted the full path..

        if(up) // If holding down on the screen
        {
            dy -=1;

        }
        else
        {
            dy +=1;
        }

        if(dy>14)dy = 1;
        if(dy<-14)dy = -1;

        y += dy; // Setting the Y axis for the player object.

    }

    public void draw(Canvas canvas) // Draw the canvas
    {
        canvas.drawBitmap(com.example.a726224.game.Animation.getImage(),x,y,null);
    }
    public int getScore() // Returning the player score. I don't think this is working properly right now, have to come back to this.
    {
        return score;
    }
    public boolean getPlaying() // Returning the playing object
    {
        return playing;
    }
    public void setPlaying(boolean b) // Playing is true or false.
    {
        playing = b;
    }
    public void resetDY() // Resetting
    {
        dy = 0;
    }
    public void resetScore() // Also need to reset the score to 0.
    {
        score = 0;
    }
}