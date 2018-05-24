package com.example.pc.jumperv1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by PC on 25.01.2018.
 */

public class GameView extends View {
    private int viewWidth; // ширина и высота игрового поля
    private int viewHeight;
    private int points = 0; // очки
    int blockX = 0;
    private Sprite playerBird;
    private Block block, iceBlock;
    private boolean flag = true;
    private final int timerInterval = 40;
    private Block [] iceBlocks = new Block [100];
    private Bitmap iceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icestack);
    Bitmap background;
    Matrix matrix;
    /*Display display = getWindowManager().getDefaultDisplay();
    int width = display.getWidth();  // deprecated
    int height = display.getHeight();  // deprecated*/

    /*Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int width = size.x;
    int height = size.y;*/

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    public GameView(Context context) {
        super(context);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.spriteshigh);
        int w = b.getWidth() / 7;
        int h = b.getHeight();
        Rect firstFrame = new Rect(0, 0, w, h);
        playerBird = new Sprite(getScreenWidth() / 3, getScreenHeight() - iceBitmap.getHeight() - b.getHeight() * 2 +150, 0, 105, firstFrame, b);

        for (int i = 0; i < iceBlocks.length; i++) {
            //int flag = i % 2; //четное или нет; если да, то скорость блока положит., нет - отриц.
            int x,y;
            int velocityXforBlock;
            int criticalX;
            if(i % 2 == 1){
                velocityXforBlock = 220;
                x = -getScreenWidth()/2*(i+1);
             //   criticalX = getScreenWidth();
            }
            else {
                velocityXforBlock = -220;
                x = getScreenWidth()/2 * (i+1);
             //   criticalX = 0;
            }
            if (i < 9)
                iceBlocks[i] = new Block(x + iceBitmap.getWidth(), getScreenHeight() - iceBitmap.getHeight() * (i + 1), velocityXforBlock, 0, new Rect(0, 0, iceBitmap.getWidth(), iceBitmap.getHeight()), iceBitmap);
            else
                iceBlocks[i] = new Block(x + iceBitmap.getWidth(), getScreenHeight() - iceBitmap.getHeight() * 10, velocityXforBlock, 0, new Rect(0, 0, iceBitmap.getWidth(), iceBitmap.getHeight()), iceBitmap);

            //if (iceBlock.getX() == criticalX) если достигает края экрана
            /*if (iceBlock.getX() == getScreenWidth() / 4 * 3) {
                iceBlock.stop();
            }*/

        }
        background = BitmapFactory.decodeResource(getResources(),R.drawable.mountain);
        matrix = new Matrix();
        matrix.setScale(3f, 4.0f, 200, 0);
        /*




        создать активность-менюшку
        увелечение очков
        завершение игры
        фон




         */
        class Timer extends CountDownTimer {
            public Timer() {
                super(Integer.MAX_VALUE, timerInterval);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                update();
            }

            @Override
            public void onFinish() {
            }
        }

        Timer t = new Timer();
        t.start();
        for (int i = 0; i < 7; i++) {
            playerBird.addFrames(new Rect(i * w, 0, i * w + w, h));
        }
    }


    /*public boolean isStanding(){

    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //here will be proverka on touching with iceblock
            int eventAction = event.getAction();
            if (eventAction == MotionEvent.ACTION_DOWN && !playerBird.isJumping) {
                playerBird.jump();
            }
            return true;
    }

    @Override
    protected void onDraw(Canvas canvas) { // чтобы рисовать на поверхности компонента
        super.onDraw(canvas);
        //Bitmap mBackgroundImage = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_4444);
        //Canvas c = new Canvas(mBackgroundImage);
        //c.setBitmap(mBackgroundImage);
        //mCanvas = new Canvas(mBackgroundImage);
        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.mountain),0,0,null); // фон
        //canvas.drawARGB(100, 224, 255, 255);
        //canvas.drawColor(Color.TRANSPARENT);
        // canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background, matrix, null);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.BLUE);
        canvas.drawText(points + "", viewWidth - 100, 70, p);
        for (int i = 0; i < iceBlocks.length; i++) {
            if (iceBlocks[i].intersect(playerBird)) {  //а если сбоку?

                playerBird.setY(playerBird.getY() - 20);
                iceBlocks[i].stop();
                points++;
                /*if (iceBlocks[i].getX() > playerBird.getX())
                    Toast.makeText("", Toast.LENGTH_SHORT).show();*/
                if (iceBlocks[i].getY() <= getScreenHeight()/2){
                    for(int j = i; j > 0; j--){
                        iceBlocks[j].setVy(40);
                    }
                    playerBird.stop(40);
                    iceBlocks[i].stop();
                } else {
                    playerBird.stop(0);
                }
            }
            /*if (iceBlocks[i].getX() <= getScreenHeight()){
                for(int j = i; j > 0; j--){
                    iceBlocks[j].setVy(iceBlocks[j].getVy()-20);
                }
                playerBird.stop();
                iceBlocks[i].stop();
            }*/
            iceBlocks[i].draw(canvas);

        }

        playerBird.draw(canvas);

    }



    protected void update() {
        playerBird.update(timerInterval);
        for (int i = 0; i < iceBlocks.length; i++) {
            iceBlocks[i].update(timerInterval);
        }
        //blockX++;
        invalidate();
    }


}