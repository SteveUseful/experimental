package com.example.a726224.game;

/**
 * Created by 726224 on 3/5/2017.
 */

import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.view.View;

public abstract class GameObject // Game objects, coords, height and width with getters and setters.
{
    protected int x;
    protected int y;

    protected int dy;
    protected int dx;

    protected int width;
    protected int height;

    public void setX(int x) // x coord
    {
        this.x = x;
    }

    public void setY(int y) // and the y coord
    {
        this.y = y;
    }

    public int getX() // get the x coord
    {
        return x;
    }

    public int getY() // get the Y coord
    {
        return y;
    }

    public int getHeight() // returning the height
    {
        return height;
    }

    public int getWidth() // reutrning the width
    {
        return width;
    }

    public Rect getRectangle() // using getRec sort of scares me but a few websites suggested it for this game.
    {
        return new Rect(x, y, x+width, y+height);
    }


}