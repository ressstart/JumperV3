package com.example.pc.jumperv1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 25.01.2018.
 */

public class Sprite {
    ImageView image;
    private Bitmap bitmap; //ссылка на изображение с набором кадров
    private List<Rect> frames; //коллекшн прямоугол областей на изобр - кадры,участвующ в анимац послед-ти
    private int frameWidth; // ширина объекта
    private int frameHeight; // высота объекта
    private int currentFrame; // номер текущего кадра в коллекции frames
    private double frameTime; // время отведенное на отображ каждого кадра анимац послед-ти
    private double timeForCurrentFrame; // текущее время отображ кадра.
                                        // номер текущ кадра currentFrame меняется на след при достиж-и
                                        //перемен timeForCurrentFrame значения из frameTime
    private double x; // местоположение левого верхнего угла спрайта на экране
    private double y;
    private double velocityX; //скорости перемещения по осям Ох и Оу
    private double velocityY;
    private int padding; // внутренний отступ от границ спрайта для более точн опр пересеч спрайтов
    private boolean isJumping = false;

    //ImageView imageView = (image).findViewById(R.id.);
    //

    public Sprite(double x, double y, double velocityX, double velocityY, Rect initialFrame, Bitmap bitmap){
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.bitmap = bitmap;
        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);
        this.bitmap = bitmap;
        this.timeForCurrentFrame = 0.0;
        this.frameTime = 100;
        this.currentFrame = 0;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();
        this.padding = 2;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public int getFrameWidth() {
        return frameWidth;
    }
    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }
    public int getFrameHeight() {
        return frameHeight;
    }
    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }
    public double getVx() {
        return velocityX;
    }
    public void setVx(double velocityX) {
        this.velocityX = velocityX;
    }
    public double getVy() {
        return velocityY;
    }
    public void setVy(double velocityY) {
        this.velocityY = velocityY;
    }
    public int getCurrentFrame() {
        return currentFrame;
    }
    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame%frames.size();
    }
    public double getFrameTime() {
        return frameTime;
    }
    public void setFrameTime(double frameTime) {
        this.frameTime = Math.abs(frameTime);
    }
    public double getTimeForCurrentFrame() {
        return timeForCurrentFrame;
    }
    public void setTimeForCurrentFrame(double timeForCurrentFrame) {
        this.timeForCurrentFrame = Math.abs(timeForCurrentFrame);
    }
    public int getFramesCount() {
        return frames.size();
    }
    public void addFrames(Rect frame){ //метод добавления кадров в анимац послед-ть
        frames.add(frame);
    }

    public void addFrame (Rect frame) {
        frames.add(frame);
    }

    public void update(int ms){ // обновление внутреннего состояния спрайта, анимация.
                                // вызов таймера с опр переодич-ю. в метод передается время в мс (с момента
                                // последнего вызова этого метода). Время испол-ся для изм-я сост спрайта
                                // его перемещ в простр-е за это время и расчит необход-ь перехода к след кадру
        if (isJumping) {
            timeForCurrentFrame += ms;

            if (timeForCurrentFrame >= frameTime) {
                if (currentFrame + 1 == frames.size())
                    isJumping = false;
                else
                    currentFrame = currentFrame + 1;
                timeForCurrentFrame = timeForCurrentFrame - frameTime;
            }
        }
        x = x + velocityX * ms/1000.0;
        y = y + velocityY * ms/1000.0;

    }

    public void jump() {
        isJumping = true;
        currentFrame = 0;
    }

    /*public boolean isStanding(){
        if
    }*/

    public void draw(Canvas canvas){ //этот метод рисует на переданном холсте текущ кадр frames.get(currentFrame)
                                     //в области экрана, заданной в прямоугольнике destination
        Paint p = new Paint();
        Rect destination = new Rect((int)x, (int)y, (int)(x + frameWidth), (int)(y + frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, p);
    }
    public Rect getBoundBoxRect(){ // метод, возвращ прямоугол обл,участ-ую в опред-и столкновений
        return new Rect((int)x+padding, (int)y+padding,(int)(x+frameWidth - 2*padding),
                (int)(y + frameHeight - 2*padding));
    }


    public boolean intersect(Sprite s){ // метод опред-я получения спрайтов

        return getBoundBoxRect().intersect(s.getBoundBoxRect());
    }

    public void stop() {
        velocityX = 0;
        velocityY = 0;
    }
}