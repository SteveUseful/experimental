package com.example.a726224.game;

/**
 * Created by Steve Silliker, March 2017 My First game.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback // I spent a long time on this class... It basically powers my entire app.
{
    public static final int WIDTH = 856; // May need to change these if I use a different BG image
    public static final int HEIGHT = 480; // Same goes for height as width
    public static final int MOVESPEED = -5;// moving speed.

    private long smokeStartTime; // starting the smoke from behind theplayer
    private long missileStartTime; // these are the objects flying towards the player.
 //   private long bossStartTime; // Adding boss start time.

    private ArrayList<Smokepuff> smoke; // These are the array lists for the stuff that reappears and repeats. Missles, Top and bottom border, Smoke from movng.
    private ArrayList<Missile> missiles; // array for missles
 //   private ArrayList<Bosses> boss1; // array for Bosses (only have one boss right now)

    private ArrayList<TopBorder> topborder; // array for top border
    private ArrayList<BotBorder> botborder; // array for bottom border

    private Random rand = new Random();
    private int maxBorderHeight; // borders top and bottom.
    private int minBorderHeight; // minimum height for broder

    private boolean topDown = true; // borders
    private boolean botDown = true; // borders
    private boolean newGameCreated; // new created


    private MainThread thread; // instantiate this later**************************************
    private Background bg; //background image and size
    private Player player; // player properties

    private int progressDenom = 15; //This is where the speed per progress goes. I set it low so it starts slow.

    private Explosion explosion; // Explosion from dying or crashing into something (border or missle)
    private long startReset; // After dying - Reset
    private boolean reset;
    private boolean dissapear; // clear screen

    private boolean started;
    private int best; // best score will display at the bottom of the screen. I should design another page to save HighScores.
//**************************************************************************************************//
//

    public GamePanel(Context context) //add the callback to the surfaceholder to catch the vents that happen
    {                                  //make gamePanel focusable so it can handle events. GamePanel is the main object of my game.
        super(context); // passing context to super class.
        getHolder().addCallback(this);
        setFocusable(true); // Making GP focusable
    }

    @Override

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) // This is where the change in SurfaceHolder happens.
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true; // After it is destryed, retry / restart.

        int counter = 0; // counter to hold retry option

        while (retry && counter < 1000)
        {
            counter++; // increasing ++

            try {
                thread.setRunning(false); // not running
                thread.join(); // opening thread
                retry = false; // not moving.
                thread = null;

            } catch (InterruptedException e) // catch exceptions and send them to stac trace
            {
                e.printStackTrace(); // printing to see error
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) { // Surfaceholder created on Surface Created. Setting basically everything.

        bg = new Background (BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1)); // Background Image
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3); // Player Image

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ player images. Get new ones.

        smoke = new ArrayList<Smokepuff>(); // array for smoke behind player
        missiles = new ArrayList<Missile>(); // array for the objects flying towards you calling class Missle
     //   boss1 = new ArrayList<Bosses>(); // array for Bosses. (under construction)**************************************

        topborder = new ArrayList<TopBorder>(); // Top border array
        botborder = new ArrayList<BotBorder>(); // bottom border array!! yay arrays.

        smokeStartTime = System.nanoTime(); //nano seconds, smoke start and missle start.

        missileStartTime = System.nanoTime(); // Starting point for missles.

        thread = new MainThread(getHolder(), this); // thread = MainThread

        thread.setRunning(true); //starting loop for game

        thread.start(); // starting the thread
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) // Touch events. Right now it just goes up when you touch and if you do nothing, you die.
    // I want to change this so the player stays level on the screen and sliding up and down moves the player. Add shooting.
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (!player.getPlaying() && newGameCreated && reset) {
                player.setPlaying(true); // set to true if playing/neewGame/reset is equal.
                player.setUp(true);
            }
            if (player.getPlaying()) // If started
            {

                if (!started) started = true;
                reset = false;
                player.setUp(true); // set up the player
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) // If you move up
        {
            player.setUp(false); // Don't setup anymore already in use; False makes you able to move up. I'm going to edit this to drag around the player.
            return true;
        }

        return super.onTouchEvent(event); // returning parent onTouchEvent
    }

    public void update() //updating the base panel, botborder and topborder. not using borders anymore.

    {
        if (player.getPlaying()) {

            if (botborder.isEmpty()) {
                player.setPlaying(false);
                return;
            }
            if (topborder.isEmpty()) {
                player.setPlaying(false);
                return;
            }

            bg.update(); // update background
            player.update(); // update player

            maxBorderHeight = 10 + player.getScore() / progressDenom; // setting the maximum for border covering screen
            //cap max border height so that borders can only take up a total of 1/2 the screen

            if (maxBorderHeight > HEIGHT / 4) maxBorderHeight = HEIGHT / 4; //max border height
            minBorderHeight = 5 + player.getScore() / progressDenom; // min height


            for (int i = 0; i < botborder.size(); i++) //bottom border collision
            {
                if (collision(botborder.get(i), player)) // If the player hits the bottom border, he dead.
                    player.setPlaying(false); // kill player
            }

            for (int i = 0; i < topborder.size(); i++)  // top collison spot; if collison = dead.
            {
                if (collision(topborder.get(i), player)) // if hit top border.
                    player.setPlaying(false); // kill player
            }

            this.updateTopBorder(); //update top border
//            this.updateBottomBorder(); //update bottom border


            // ********************************8 UNDER CONSTRUCTION************BOSSES********************************************

//            long bossElapsed = (System.nanoTime() - bossStartTime) / 1000000;  //boom you dead. Missle start. 100000
//            if (bossElapsed > (2000 - player.getScore() / 4)) {
//
//                if (boss1.size() == 0) //first missile is centered automatically; If no missles, make center missle.
//                {
//                    boss1.add(new Bosses(BitmapFactory.decodeResource(getResources(), R.drawable.
//                            boss1), WIDTH + 10, HEIGHT / 2, 45, 15, player.getScore(), 13)); // drawing boss after initial missle.
//
//                } else {
//
//                    boss1.add(new Bosses(BitmapFactory.decodeResource(getResources(), R.drawable.boss1),
//                            WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - (maxBorderHeight * 2)) + maxBorderHeight), 45, 15, player.getScore(), 13));
//                }
//                bossStartTime = System.nanoTime(); //reset
//            }
//
//            for (int i = 0; i < boss1.size(); i++) //loop Bosses. gotta check collision too
//            {
//                boss1.get(i).update(); // update missle
//
//                if (collision(boss1.get(i), player)) // collision for missle - player
//                {
//                    boss1.remove(i);
//                    player.setPlaying(false);
//                    break;
//                }
//
//                if (boss1.get(i).getX() < -100) {
//                    boss1.remove(i);
//                    break;
//                }
//            }

//***********************************************************************************************************************************


            long missileElapsed = (System.nanoTime() - missileStartTime) / 1000000;  //boom you dead. Missle start/creation. 1000000
            if (missileElapsed > (2000 - player.getScore() / 1)) {

                if (missiles.size() == 0) //first missile is centered automatically; If no missles, make center missle.
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.
                            missile), WIDTH + 10, HEIGHT / 2, 45, 15, player.getScore(), 13)); // drawing the missles after initial missle.

                } else {

                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - (maxBorderHeight * 2)) + maxBorderHeight), 45, 15, player.getScore(), 13));
                }
                missileStartTime = System.nanoTime(); //reset
            }

            for (int i = 0; i < missiles.size(); i++) //loop al missles. gotta check collision too
            {
                missiles.get(i).update(); // update missle

                if (collision(missiles.get(i), player)) // collision for missle - player
                {
                    missiles.remove(i); // take away missle
                    player.setPlaying(false); // playing no more. boolean
                    break; // stop
                }

                if (missiles.get(i).getX() < -100) {
                    missiles.remove(i); // take away missles
                    break; // // STOPSHIP
                }
            }

            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000; // Smoke start behind player
            if (elapsed > 120) {
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10)); // changing puffs
                smokeStartTime = System.nanoTime(); // start smoke
            }

            for (int i = 0; i < smoke.size(); i++) {
                smoke.get(i).update();
                if (smoke.get(i).getX() < -10) {
                    smoke.remove(i); // if less then remove.
                }
            }
        }
        else
            {
            player.resetDY();

            if (!reset) // if we are on reset mode
            {
                newGameCreated = false; // not making a new game. exploded.
                startReset = System.nanoTime();
                reset = true;
                dissapear = true; // erase everything

                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), player.getX(),
                        player.getY() - 30, 100, 100, 25);
            }

            explosion.update(); // update explosions

            long resetElapsed = (System.nanoTime() - startReset) / 1000000;

            if (resetElapsed > 2500 && !newGameCreated) {
                newGame();
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) // Collisions! This stuff is picky. Not sure if I'll stick with getRactangle or not.
    {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) // drawing the cnvas. Setting the scale factors for x and y coords.
    {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) { // if the canvas is null (dead or new game)
            final int savedState = canvas.save(); // savedState - save canvas
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas); // drawing actual canvas
            if (!dissapear) {
                player.draw(canvas); // if the player is dead is nothing is there, draw canvas.
            }
            for (Smokepuff sp : smoke) // for loop for the smoke puffs from the player
            {
                sp.draw(canvas);
            }

            for (Missile m : missiles) //draw objects coming towards player
            {
                m.draw(canvas);
            }
//            for (Bosses b : boss1) // adding bosses incrementally ; UNDER CONSTRUCTION*********************************
//            {
//                b.draw(canvas);
//            }


            for (TopBorder tb : topborder) // drawing the top border; NOT USING BORDERS NOW. MAY USE LATER.*********
            {
                tb.draw(canvas);
            }

            for (BotBorder bb : botborder) // drawing the bottom border! NOT USING BORDERS NOW. MAY USE LATER.*********
            {
                bb.draw(canvas);
            }

            if (started) // if loop for explosions
            {
                explosion.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState); // bringing canvas back to savedState
        }
    }

    public void updateTopBorder() //NOT USING BORDERS NOW. MAY USE LATER.********* att 200 points the pattern for border changes. make sure to add this to botborder!
    {

        if (player.getScore() % 200 == 0) {
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
            ), topborder.get(topborder.size() - 1).getX() + 21, 0, (int) ((rand.nextDouble() * (maxBorderHeight
            )) + 1)));
        }

        for (int i = 0; i < topborder.size(); i++) // top border size and update - NOT USING BORDERS NOW. MAY USE LATER.*********
        {
            topborder.get(i).update();
            if (topborder.get(i).getX() < -20) {
                topborder.remove(i); // removing item from array and adding a new one

                if (topborder.get(topborder.size() - 1).getHeight() >= maxBorderHeight) //calculate topdown
                {
                    topDown = false; //NOT USING BORDERS NOW. MAY USE LATER.*********
                }

                if (topborder.get(topborder.size() - 1).getHeight() <= minBorderHeight) {
                    topDown = true;
                }
                //new border added will have larger height
                if (topDown) {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20,
                            0, topborder.get(topborder.size() - 1).getHeight() + 1));
                }
                //new border added wil have smaller height
                else {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20,
                            0, topborder.get(topborder.size() - 1).getHeight() - 1));
                }

            }
        }

    }


    //************************************************************************************************************
//    public void updateBottomBorder() NOT USING BORDERS NOW. MAY USE LATER.********* WANTED TO SAVE CODE FOR LATER USE.
//    {
//        //every 40 points, insert randomly placed bottom blocks that break pattern
//        if(player.getScore()%40 == 0)
//        {
//            botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
//                    botborder.get(botborder.size()-1).getX()+20,(int)((rand.nextDouble()
//                    *maxBorderHeight)+(HEIGHT-maxBorderHeight))));
//        }

    //update bottom border
//        for(int i = 0; i<botborder.size(); i++)
//        {
//            botborder.get(i).update();
//
//            //if border is moving off screen, remove it and add a corresponding new one
//            if(botborder.get(i).getX()<-5) {
//                botborder.remove(i);
//
//
//                //determine if border will be moving up or down
//                if (botborder.get(botborder.size() - 1).getY() <= HEIGHT-maxBorderHeight) {
//                    botDown = true;
//                }
//                if (botborder.get(botborder.size() - 1).getY() >= HEIGHT - minBorderHeight) {
//                    botDown = false;
//                }
//
//                if (botDown) {
//                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
//                    ), botborder.get(botborder.size() - 1).getX() + 10, botborder.get(botborder.size() - 1
//                    ).getY() - 1));
//                } else {
//                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
//                    ), botborder.get(botborder.size() - 1).getX() + 2, botborder.get(botborder.size() - 1
//                    ).getY() - 1));
//                }
//*******************************************************************************************************************

    public void newGame() // newGame class; Clearing borders and objects including smoke from player
    {
        dissapear = false;

        botborder.clear(); // NOT USING BORDERS NOW. MAY USE LATER.*********
        topborder.clear(); // NOT USING BORDERS NOW. MAY USE LATER.*********

        missiles.clear(); // Clear all missles/
       // boss1.clear(); // Clearing all bosses (in this case I only have one boss right now)
        smoke.clear(); // Clear all smoke behind player

        minBorderHeight = 0; // max and min border height
        maxBorderHeight = 1; //NOT USING BORDERS NOW. MAY USE LATER.*********

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT / 2);

        if (player.getScore() > best) // Setting the score for the player. Saving if over previous score. (best)
        {
            best = player.getScore();

        }


        for (int i = 0; i * 20 < WIDTH + 10; i++)//initial top border; NOT USING BORDERS NOW. MAY USE LATER.*********
        {
            //first top border create
            if (i == 0) {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                ), i * 1, 0, 1));
            } else {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                ), i * 1, 0, topborder.get(i - 1).getHeight() + 1));
            }
        }

        //initial bottom border
        for (int i = 0; i * 1 < WIDTH + 1; i++) // NOT USING BORDERS NOW. MAY USE LATER.*********
        {
            //first border ever created
            if (i == 0) {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick)
                        , i * 10, HEIGHT - minBorderHeight));
            }
            //adding borders until the initial screen is filed
            else {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 10, botborder.get(i - 1).getY() - 1));
            }
        }
        newGameCreated = true; // setting new game to true.
    }

    public void drawText(Canvas canvas) // canvas - Setting start message when app loads, also keeping track of distance and best score/high score.
    {
        Paint paint = new Paint(); // paint1 will control my menu stuff. Right now it's just the starting text for the app.
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("HOW FAR: " + (player.getScore() * 3), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if (!player.getPlaying() && newGameCreated && reset) // setting the first message. Also displays after dying.
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(25);
            paint1.setColor(Color.WHITE);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Thanks for trying my App!", WIDTH / 2 - 40, HEIGHT / 4, paint1);
            canvas.drawText("Press and Hold Screen - You go up", WIDTH / 2 - 50, HEIGHT / 3 + 20, paint1);
            canvas.drawText("Do Nothing - Go Down - Dead", WIDTH / 2 - 50, HEIGHT / 2 + 40, paint1);
//            canvas.drawText("START", WIDTH/2-50, HEIGHT/2, paint1);

        }
    }


}