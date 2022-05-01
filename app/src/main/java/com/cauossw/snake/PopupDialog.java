package com.cauossw.snake;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cauossw.snake.databinding.PopupPauseBinding;

public class PopupDialog extends Dialog {


    private final String TAG = "popupDialog";
    private PopupPauseBinding popupPauseBinding;

    private String status;
    private GameThread thread;


    public PopupDialog(@NonNull Context context) {
        super(context);
        init();
    }
    public PopupDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();

    }
    public PopupDialog(@NonNull Context context, String status, GameThread thread){
        super(context);
        init();
        this.status = status;
        this.thread = thread;
    }
    protected PopupDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupPauseBinding = PopupPauseBinding.inflate(getLayoutInflater());
        setContentView(popupPauseBinding.getRoot());
    }

    private void init(){
    }
}
