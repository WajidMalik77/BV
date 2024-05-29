package com.background.video.recorder.camera.recorder.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.background.video.recorder.camera.recorder.R;

public class DeleteAnimationDialog {
    private static final String TAG = "WrongPasswordDialog";

    private final Context context;
    private Dialog dialog;

    public DeleteAnimationDialog(Context context) {
        this.context = context;
        initDialog();

    }


    private void initDialog() {
        this.dialog = getDialog();
    }

    private Dialog getDialog() {


        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_delete_password);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));


        dialog.show();
        return dialog;
    }

    public void cancelDialog() {
        dialog.dismiss();
    }
}
