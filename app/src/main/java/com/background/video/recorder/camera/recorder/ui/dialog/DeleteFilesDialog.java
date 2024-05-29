package com.background.video.recorder.camera.recorder.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.background.video.recorder.camera.recorder.R;


public class DeleteFilesDialog {
    private Context context;
    private Dialog dialog;
    private OnButtonClickedListener buttonCallBack;
    private ImageButton ibYes, ibNo;
    private boolean deleteFiles = false;
    private FilesDeletedListener filesDeletedCallBack;
    public DeleteFilesDialog(Context context, OnButtonClickedListener buttonCallBack,FilesDeletedListener filesDeletedCallBack) {
        this.context = context;
        this.buttonCallBack = buttonCallBack;
        this.filesDeletedCallBack = filesDeletedCallBack;
        initDialog();
    }

    public interface OnButtonClickedListener {
        void onYesButtonClicked(boolean deleteFiles);
    }
    public interface FilesDeletedListener{
        void isFilesDeleted(boolean deleted);
    }

    private void initDialog() {
        this.dialog = getDialog();

    }

    private Dialog getDialog() {


        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_delete);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setCancelable(true);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ibYes = dialog.findViewById(R.id.ibYes);
        ibNo = dialog.findViewById(R.id.ibNo);

        ibYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFiles = true;
                buttonCallBack.onYesButtonClicked(deleteFiles);
                dialog.dismiss();
                filesDeletedCallBack.isFilesDeleted(true);
            }
        });
        ibNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                filesDeletedCallBack.isFilesDeleted(false);
            }
        });

        dialog.show();
        return dialog;
    }
}

