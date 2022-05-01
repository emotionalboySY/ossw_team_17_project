package com.cauossw.snake;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cauossw.snake.databinding.PopupEndBinding;
import com.cauossw.snake.databinding.PopupPauseBinding;

public class PopupDeadDialog extends Dialog {


    private final String TAG = "popupDialog";
    private PopupEndBinding popupEndBinding;

    private String status;
    private GameThread thread;


    public PopupDeadDialog(@NonNull Context context) {
        super(context);
        init();
    }
    public PopupDeadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();

    }

    protected PopupDeadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupEndBinding = PopupEndBinding.inflate(getLayoutInflater());
        setContentView(popupEndBinding.getRoot());
    }

    private void init(){
    }
}
