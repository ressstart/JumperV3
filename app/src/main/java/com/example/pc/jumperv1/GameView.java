package com.example.pc.jumperv1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by PC on 25.01.2018.
 */

public class GameView extends View{
    private int viewWidth; // ширина и высота игрового поля
    private int viewHeight;
    private int points = 0;
    private Sprite playerBird;
    private final int timerInterval = 25;

    public GameView(Context context) {
        super(context);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.sprites2var);
        int w = b.getWidth()/7;
        int h = b.getHeight()/1;
        Rect firstFrame = new Rect(0,0, w, h);
        playerBird = new Sprite(10,0,0,0, firstFrame, b);

        class Timer extends CountDownTimer {
            public Timer() {
                super(Integer.MAX_VALUE, timerInterval);
            }
            @Override
            public void onTick(long millisUntilFinished) {
                update ();
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


    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);

        viewWidth = w;
        viewHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN)  {
            playerBird.jump();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){ // чтобы рисовать на поверхности компонента
        super.onDraw(canvas);
        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.sprites2var),20,15,null); //32-37 фон и кол-во очков на поверх-и компонента
        canvas.drawARGB(100,200,220,100);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.BLUE);
        canvas.drawText(points+"", viewWidth - 100, 70, p);
        playerBird.draw(canvas);
    }

    protected void update () {
        playerBird.update(timerInterval);
        invalidate();
    }


}