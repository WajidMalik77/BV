package com.background.video.recorder.camera.recorder.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.background.video.recorder.camera.recorder.R;


public class AdLoadingDialog {

    private final Context context;
    private Dialog dialog;

    public AdLoadingDialog(Context context) {
        this.context = context;
    }

    public void initDialog() {
        this.dialog = getDialog();
    }

    private Dialog getDialog() {


        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_ad_loading_dialog);
        dialog.setCanceledOnTouchOutside(false);


        dialog.setCancelable(true);


        dialog.show();
        return dialog;
    }

    public void cancelDialog() {
        dialog.dismiss();
    }
}
