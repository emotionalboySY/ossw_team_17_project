package com.cauossw.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class GameView extends View {

    DrawCanvas drawCanvas = null;

    public GameView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCanvas.draw(canvas);
    }
}
