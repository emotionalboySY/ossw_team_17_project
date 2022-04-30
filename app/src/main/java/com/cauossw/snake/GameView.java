package com.cauossw.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;



public class GameView extends View {

    private final String TAG = "GameView";

    private int canvasWidth;
    private int eachImageWidth;

    private Bitmap snakeHeadImage;
    private Bitmap snakeBodyImage;
    private Bitmap snakeTailImage;
    private Bitmap appleImage;
    private Bitmap mapTileImage;

    private Snake snake;
    private Apple apple;

    private ArrayList<Coordinate> snakePositions;
    private Coordinate applePosition;

    private Bundle bundle;


    public GameView(Context context) {
        super(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //뷰 크기 설정
        if(heightMeasureSpec >= widthMeasureSpec){ //화면 세로>=가로
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec); //가로길이에 뷰 맞춤
            Log.i(TAG, "onMeasure size:"+widthMeasureSpec);
        }else{ //화면 가로 >= 세로
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec); //세로길이에 뷰 맞춤
            Log.i(TAG, "onMeasure size:"+heightMeasureSpec);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) { //캔버스 수정이 필요할때마다 호출됨
        super.onDraw(canvas);
        //저장된 캔버스 크기가, 현재 캔버스 크기와 맞지않을 때, 가로모드 등으로 화면 크기 변경
        imageReSet(canvas);

//        canvas.drawBitmap(snakeHeadImage,);
//        canvas.drawBitmap(snakeBodyImage,);
//        canvas.drawBitmap(snakeTailImage,);
    }

    public void setSnake(Snake snake){
        this.snake = snake;
        Log.i(TAG,"set Snake object");
    }

    public void setApple(Apple apple){
        this.apple = apple;
        Log.i(TAG,"set Snake object");
    }

    private void init(){
        bundle = new Bundle();
        imageSet();
    }
    private void imageSet(){
        snakeHeadImage = BitmapFactory.decodeResource(getResources(),R.drawable.snake_head);
        Log.i(TAG, "snakeHead 로드완료");
        snakeBodyImage = BitmapFactory.decodeResource(getResources(), R.drawable.snake_body);
        Log.i(TAG, "snakeBody 로드완료");
        snakeTailImage = BitmapFactory.decodeResource(getResources(), R.drawable.snake_tail);
        Log.i(TAG, "snakeTail 로드완료");
        appleImage = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
        Log.i(TAG, "apple 로드완료");
        mapTileImage = BitmapFactory.decodeResource(getResources(), R.drawable.map_tile);
        Log.i(TAG, "Map 로드완료");
    }
    private void imageReSet(Canvas canvas){
        if(canvasWidth != getWidth()){
            canvasWidth = getWidth(); //현재 크기로 저장
            eachImageWidth = canvasWidth/DefaultConst.WIDTH; //각 요소당 크기 계산
            Log.i(TAG, "이미지 사이즈 변경 완료 "+ canvasWidth/DefaultConst.WIDTH + "px");

            snakeHeadImage = Bitmap.createScaledBitmap(snakeHeadImage, eachImageWidth, eachImageWidth,true);
            snakeBodyImage = Bitmap.createScaledBitmap(snakeBodyImage, eachImageWidth, eachImageWidth,true);
            snakeTailImage = Bitmap.createScaledBitmap(snakeTailImage, eachImageWidth, eachImageWidth,true);
            appleImage = Bitmap.createScaledBitmap(appleImage, eachImageWidth, eachImageWidth,true);
            mapTileImage = Bitmap.createScaledBitmap(mapTileImage, eachImageWidth, eachImageWidth,true);

            Log.i(TAG, "canvas width:"+getWidth()+" canvas Height:"+getHeight());

            for(int vert = 0; vert<canvasWidth; vert+=eachImageWidth){ //draw mapTile
                for(int hor = 0; hor<canvasWidth; hor+=eachImageWidth){
                    canvas.drawBitmap(mapTileImage, vert, hor, null);
                }
            }
        }
    }


}