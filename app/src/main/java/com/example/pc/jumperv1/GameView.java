package com.example.pc.jumperv1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by PC on 25.01.2018.
 */

public class GameView extends View {
    private int viewWidth; // ширина и высота игрового поля
    private int viewHeight;
    private int points = 0; // очки
    int blockX = 0;
    private Sprite playerBird, iceBlock;
    private Sprite block;
    private boolean flag = true;
    private final int timerInterval = 40;
    private Sprite [] iceBlocks = new Sprite [4];
    private Bitmap iceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icestack);
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
        playerBird = new Sprite(getScreenWidth() / 3, getScreenHeight() - iceBitmap.getHeight() - b.getHeight() * 2, 0, 105, firstFrame, b);

        for (int i = 0; i < iceBlocks.length; i++) { //потом переделать в while потому что стэк будет без счетчика, а по условию
            int flag = i % 2; //четное или нет; если да, то скорость блока положит., нет - отриц.
            int x;
            int y;
            int velocityXforBlock;
            int criticalX;
            if(flag == 1){
                velocityXforBlock = 220;
                x = -getScreenWidth()/2*(i+1);

                criticalX = getScreenWidth();
            }
            else {
                velocityXforBlock = -220;
                x = getScreenWidth()/2 * (i+1);
                criticalX = 0;
            }
            iceBlocks[i] = new Sprite(x, getScreenHeight() - iceBitmap.getHeight() * (i + 1), velocityXforBlock, 0, new Rect(0, 0, iceBitmap.getWidth(), iceBitmap.getHeight()), iceBitmap);
            //if (iceBlock.getX() == criticalX) если достигает края экрана
            /*if (iceBlock.getX() == getScreenWidth() / 4 * 3) {
                iceBlock.stop();
            }*/

        }
        /*




        создать наследование от класса Sprite , почистить и прописать методы и перемнные для iceblock
        пошаманить со скоростями и координатами





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
            playerBird.addFrame(new Rect(i * w, 0, i * w + w, h));
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
        // canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.spriteshigh),20,15,null); //32-37 фон и кол-во очков на поверх-и компонента
        canvas.drawARGB(100, 224, 255, 255);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.BLUE);
        canvas.drawText(points + "", viewWidth - 100, 70, p);
        for (int i = 0; i < iceBlocks.length; i++) {
            if (iceBlocks[i].intersect(playerBird)) {
                playerBird.stop();
                iceBlocks[i].stop();
            }
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