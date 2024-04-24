package com.example.a726224.game;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;

import static com.example.a726224.game.R.raw.sw;

/**
 * Created by Steve Silliker march 2017 Prototype game.
 */

public class Game extends Activity // This is the first class I created / started off with. It holds basic functions for the entire app.
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); // Starting my background service for the music.
        Intent intent = new Intent(this, BackgroundSound.class); // Calling my background service from the main activity.
        startService(intent);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // Turning the title at the top off.

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //set to full screen viewmode.

        setContentView(new GamePanel(this));

        getApplication().onCreate();

}
//    @Override // unused class. Was looking for a menu, but I dont have one yet. This crashed my entire program. Lesson learned.
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_game, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)  // Simple If Statement
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}