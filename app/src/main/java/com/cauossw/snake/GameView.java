package com.cauossw.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {

    private final String TAG = "GameView";

    private Context context;

    private int displayWidth;
    private int displayHeight;

    private Snake snake = null;
    private Map map = null;
    private Apple apple = null;

    private Bitmap snakeHeadImage= null;
    private Bitmap snakeBodyImage= null;
    private Bitmap snakeTailImage= null;
    private Bitmap appleImage= null;

    public GameView(Context context) {
        super(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){
        //start시 게임에 필요한 요소들에 대해 새 객체 생성
        if(map ==null){
            map = new Map();
            Log.i(TAG, "map 생성완료");
        }
        if(snake == null){
            snake = new Snake();
            Log.i(TAG, "snake 생성완료");
        }
        if(apple == null){
            apple = new Apple();
            Log.i(TAG, "apple 생성완료");

        }

        snakeHeadImage = BitmapFactory.decodeResource(getResources(),R.drawable.snake_head);
        Log.i(TAG, "snakeHead 로드완료");

        snakeBodyImage = BitmapFactory.decodeResource(getResources(), R.drawable.snake_body);
        Log.i(TAG, "snakeBody 로드완료");

        snakeTailImage = BitmapFactory.decodeResource(getResources(), R.drawable.snake_tail);
        Log.i(TAG, "snakeTail 로드완료");

        appleImage = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
        Log.i(TAG, "apple 로드완료");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(heightMeasureSpec >= widthMeasureSpec){ //화면 세로>=가로
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec); //가로길이에 뷰 맞춤
            Log.i(TAG, "onMeasure size:"+widthMeasureSpec);
        }else{ //화면 가로 >= 세로
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec); //세로길이에 뷰 맞춤
            Log.i(TAG, "onMeasure size:"+heightMeasureSpec);
        }



    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "canvas width:"+canvas.getWidth()+" canvas Height:"+canvas.getHeight());

        appleImage = Bitmap.createScaledBitmap(appleImage,canvas.getWidth()/40, canvas.getHeight()/40,true);

        //TODO
        for(int i =0;i<canvas.getWidth();i+=36){
            for(int j = 0; j< canvas.getWidth(); j+=36){
                canvas.drawBitmap(appleImage, i,j, null);
                Log.i(TAG, "이미지로드"+i+","+j+"번째");
            }
        }
    }
}
