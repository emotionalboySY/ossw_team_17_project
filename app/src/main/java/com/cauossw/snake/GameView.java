package com.cauossw.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {

    private DrawCanvas drawCanvas = null;
    private Map map = null;



    public GameView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCanvas.draw(canvas);
    }
}

class DrawCanvas {

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

    }

    private void drawApple(Canvas canvas){

    }

    private void drawMap(Canvas canvas){

    }

}
