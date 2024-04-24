package com.example.a726224.game;


/**
 * Created by Steve Silliker march 2017 prototype game
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TopBorder extends GameObject // Right now I've removed the Top and Bottom border but I didn't want to delete the code in case I want to add them again later.
{
    private Bitmap image;

    public TopBorder(Bitmap res, int x, int y, int h) // top border dimensions and set with GamePanel.
    {
        height = 1;
        width = 2;

        this.x = x;
        this.y = y;

        dx = GamePanel.MOVESPEED;
        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }
    public void update() //updating border. not sure if I will use this or not.
    {
        x+=dx;
    }
    public void draw(Canvas canvas) // try catch for canvas
    {
        try
        {
            canvas.drawBitmap(image,x,y,null);
        }
        catch(Exception e)
        {

        };
    }

}

