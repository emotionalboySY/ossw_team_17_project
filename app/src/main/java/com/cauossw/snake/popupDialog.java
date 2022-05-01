package com.cauossw.snake;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cauossw.snake.databinding.PopupPauseBinding;

public class popupDialog extends Dialog {


    private final String TAG = "popupDialog";
    private PopupPauseBinding popupPauseBinding;

    public popupDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public popupDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();

    }

    protected popupDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();

    }

    private void init(){

    }
}
