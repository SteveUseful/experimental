package com.example.a726224.game;

/**
 * Created by Steve Silliker march 2017 Prototype game.
 */

import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class Animation { // Class for animations. May or may not have media player object. Going to comment it out for now.
//    private MediaPlayer mediaPlayer;

    private static boolean playedOnce; // First load of gameplay
    private static Bitmap[] frames; // Frames calling Bitmap function
    private static int currentFrame; // BG not currently moving, helped with the choppyness of the frames.

    private static long startTime; // Used at starttime for when the game first loads or after a retry/reset.
    private static long delay;

    public void setFrames(Bitmap[] frames) //setting the frames to 0
    {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }
    public void setDelay(long d) // delay for starting . used later for retry
    {
        delay = d;
    }

    public void setFrame(int i) // current frame
    {
        currentFrame= i;
    }

    public static void update() // updating animations / Start time in nano seconds; 1000000 seems to be a good speed.
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;

        if(elapsed>delay)
        {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }
    public static Bitmap getImage() // images for the bitmap function
    {
        return frames[currentFrame];
    }
    public int getFrame() // returning the current frame
    {
        return currentFrame;
    }
    public boolean playedOnce() // true or false for first start.
    {
        return playedOnce;
    }

}