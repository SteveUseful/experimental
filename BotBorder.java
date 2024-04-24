package com.example.a726224.game;
/**
 * Created by Steve Silliker march 2017 Prototype game.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BotBorder extends GameObject // Right now I've removed the Top and Bottom border but I didn't want to delete the code in case I want to add them again later.
{

    private Bitmap image; // calling the image for the bottom border. Using a black 20x200 image.
    public BotBorder(Bitmap res, int x, int y)
    {
        height = 2;

        width = 1;

        this.x = x;

        this.y = y;

        dx = GamePanel.MOVESPEED; // Moving speed,

        image = Bitmap.createBitmap(res, 0, 0, width, height); // setting the bottom border

    }
    public void update() // update for moving bottom border
    {
        x +=dx;

    }
    public void draw(Canvas canvas) // making the canvas for bottom border. no z .
    {
        canvas.drawBitmap(image, x, y, null);

    }
}