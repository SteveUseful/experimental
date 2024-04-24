package com.example.a726224.game;

/**
 * Created by Steve Silliker march 2017 prototype game
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Smokepuff extends GameObject // Smokepuf extends GameObject because it is another object extneding from the player
{
    public int r; // just setting the variable int r

    public Smokepuff(int x, int y)
    {
        r = 4;
        super.x = x;
        super.y = y;
    }
    public void update() // updating x.
    {
        x-=10;
    }
    public void draw(Canvas canvas) // drawing the canvas for the puffs. Setting colors, etc. I set them to yellow so I could see them/
    {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x-r, y-r, r, paint); // painting circles! over and over again...

        canvas.drawCircle(x-r+2, y-r-2,r, paint);

        canvas.drawCircle(x-r+4, y-r+1, r, paint);
    }

}

