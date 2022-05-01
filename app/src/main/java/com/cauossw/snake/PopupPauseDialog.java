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

public class PopupPauseDialog extends Dialog {


    private final String TAG = "popupDialog";
    private PopupPauseBinding popupPauseBinding;


    public PopupPauseDialog(@NonNull Context context) {
        super(context);
        init();
    }
    public PopupPauseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();

    }

    protected PopupPauseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
