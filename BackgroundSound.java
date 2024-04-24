package com.example.a726224.game;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundSound extends Service
{
    //Intent intent = new Intent(this, BackgroundSound.class); // Intent calling the BackgroundSound class. Put in onCreate!
    public BackgroundSound() // Class for BackgroundSound
    {

    }

    MediaPlayer player; // Mediaplayer obj


    @Override
    public IBinder onBind(Intent intent) // On bind event for the background service.
    {
        startService(intent); // Starting intent
        return null;
    }



    @Override
    public void onCreate()  // Starting the service.
    {
        super.onCreate();

        Intent intent = new Intent(this, BackgroundSound.class); // Calling intent to start service.
        startService(intent);

        player = MediaPlayer.create(this, R.raw.sw); // Setting the Media to play. Using an .mp3 called 'sw'. It's the star wars theme.
        player.setLooping(true); // Set looping
        player.setVolume(90,90); // Setting internal volume. Going to add a settings menu and you can control the sound from there.

    }
    public int onStartCommand(Intent intent, int flags, int startId) // Starting player; Setting Not Sticky so it doesn't restart after the app is gone.
    {
        player.start();
        return Service.START_NOT_STICKY; // This is telling the OS to not start the service again after force close / out of memory.

    }

    public IBinder onUnBind(Intent arg0) // Setting the service to unBind; arg0 is passed to stopService command.
    {

        stopService(arg0); // Stopping the service.
        return null;
    }

    public void onStop() {
        player.stop(); // Stop the player when it's stopped.

    }
    public void onPause() // No pause feature yet but I'm adding this with the settings menu.
    {

    }

    @Override
    public void onDestroy() // When the application gets destroyed, stop, release and close the intent.
    {
        player.stop();
        player.release();

        Intent intent = new Intent(this, BackgroundSound.class); // I'm hoping this stops the music.
        stopService(intent);

    }

}
