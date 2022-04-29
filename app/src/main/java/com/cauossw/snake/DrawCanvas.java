package com.cauossw.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class DrawCanvas {

    private ArrayList<Bitmap> snake = null;
    private Bitmap apple = null;
    private Bitmap map = null;
    private int numWidthSquare;

    public DrawCanvas(int numWidthSquare, Bitmap map, ArrayList<Bitmap> snake, Bitmap apple){
        this.numWidthSquare = numWidthSquare;
        this.snake = snake;
        this.apple = apple;
        this.map = map;
    }

    public void draw(Canvas canvas){
        final int eachSquarePixel = canvas.getWidth()/numWidthSquare;

        drawMap(canvas);
        drawSnake(canvas);
        drawApple(canvas);
    }

    private void drawSnake(Canvas canvas){
        canvas.drawBitmap();
    }

    private void drawApple(Canvas canvas){

    }

    private void drawMap(Canvas canvas){

    }

}
