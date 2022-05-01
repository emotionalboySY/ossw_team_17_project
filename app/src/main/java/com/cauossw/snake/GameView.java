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
import java.util.List;
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

    private ArrayList<Coordinate> snakePositions;
    private Coordinate applePosition;



    public GameView(Context context) {
        super(context);
        init();
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
        Log.i(TAG,"onDraw 호출"+ GameView.this.toString());
        if(applePosition != null){
            int x = setToCanvasPosition(applePosition.getX());
            int y = setToCanvasPosition(applePosition.getY());
            Log.i(TAG,"SnakeHead: "+x+","+y);
            canvas.drawBitmap(appleImage,null, new Rect(x,y,x+eachImageWidth, y+eachImageWidth), null);

            Log.i(TAG, "draw Apple");
        }

        if(snakePositions != null) {
            //뱀 몸체그리기
            Log.i(TAG,"start draw Snake");
            for (int snakeLength = 0; snakeLength < snakePositions.size(); snakeLength += 1) {
                int x = setToCanvasPosition(snakePositions.get(snakeLength).getX());
                int y = setToCanvasPosition(snakePositions.get(snakeLength).getY());
                if (snakeLength == 0) { //머리
                    Log.i(TAG,"SnakeHead: "+x+","+y);
                    canvas.drawBitmap(snakeHeadImage, null, new Rect(x, y, x + eachImageWidth, y + eachImageWidth), null);
                } else if (snakeLength == snakePositions.size() - 1) { //꼬리
                    canvas.drawBitmap(snakeTailImage, null, new Rect(x, y, x + eachImageWidth, y + eachImageWidth), null);
                } else {//몸통
                    canvas.drawBitmap(snakeBodyImage, null, new Rect(x, y, x + eachImageWidth, y + eachImageWidth), null);
                }
                Log.i(TAG,"end draw Snake");
            }
        }
    }

//    public void setSnake(ArrayList<Coordinate> snakePositions){
//        this.snakePositions = snakePositions;
//        Log.i(TAG,"set Snake object");
//        Log.i(TAG,"Snake length: "+snakePositions.size());
//    }
//
//    public void setApple(Coordinate applePosition){
//        this.applePosition = applePosition;
//        Log.i(TAG,"set Apple object");
//    }
    public void setBundle(Bundle bundle){
        if(bundle.getSerializable("snake")!=null) {
            Log.i(TAG, "game 인스턴스" + GameView.this.toString());
            snakePositions = (ArrayList<Coordinate>) bundle.getSerializable("snake");
            Log.i(TAG, "snake Head Position X:" + snakePositions.get(0).getX());
        }
        if(bundle.getSerializable("apple")!=null) {
            applePosition = (Coordinate) bundle.getSerializable("apple");
            Log.i(TAG, "applePosition X:" + applePosition.getX());
        }
    }

    private void init(){
        setWillNotDraw(false);
        Log.i(TAG,"게임뷰 생성");
//        snake = new Snake(new Coordinate());
//        apple = new Apple(new Coordinate());
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
            Log.i(TAG, "canvas width:"+getWidth()+" canvas Height:"+getHeight());
            Log.i(TAG, "이미지 사이즈 변경 완료 "+ canvasWidth/DefaultConst.WIDTH + "px");

            snakeHeadImage = Bitmap.createScaledBitmap(snakeHeadImage, eachImageWidth, eachImageWidth,true);
            snakeBodyImage = Bitmap.createScaledBitmap(snakeBodyImage, eachImageWidth, eachImageWidth,true);
            snakeTailImage = Bitmap.createScaledBitmap(snakeTailImage, eachImageWidth, eachImageWidth,true);
            appleImage = Bitmap.createScaledBitmap(appleImage, eachImageWidth, eachImageWidth,true);
            mapTileImage = Bitmap.createScaledBitmap(mapTileImage, eachImageWidth, eachImageWidth,true);
            Log.i(TAG, "리사이징 완료");
        }
        for(int vert = 0; vert<canvasWidth; vert+=eachImageWidth){ //draw mapTile
            for(int hor = 0; hor<canvasWidth; hor+=eachImageWidth){
                canvas.drawBitmap(mapTileImage, vert, hor, null);
            }
        }
    }
    private int setToCanvasPosition(int position){
        return position*eachImageWidth;
    }

    public void invalidate() {
        super.invalidate();
        Log.i(TAG,"invalidate 호출됨");
    }
}
