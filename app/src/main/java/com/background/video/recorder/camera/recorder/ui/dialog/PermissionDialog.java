package com.background.video.recorder.camera.recorder.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.background.video.recorder.camera.recorder.R;
import com.google.android.material.button.MaterialButton;

public class PermissionDialog {
    private Context context;
    private Dialog dialog;
    private OnPermissionGrantedListener buttonCallBack;
    private MaterialButton btnYes, btnNo;

    public PermissionDialog(Context context, OnPermissionGrantedListener yesCallBack) {
        this.context = context;
        this.buttonCallBack = yesCallBack;
        initDialog();
    }

    public interface OnPermissionGrantedListener {
        void onYesButtonClicked(boolean permissionGranted);
    }

    private void initDialog() {
        this.dialog = getDialog();

    }

    private Dialog getDialog() {


        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_permission);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        btnYes = dialog.findViewById(R.id.btnYes);
        btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCallBack.onYesButtonClicked(true);
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCallBack.onYesButtonClicked(false);
                dialog.dismiss();
            }
        });


        dialog.show();
        return dialog;
    }
}
