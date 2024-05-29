package com.background.video.recorder.camera.recorder.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.background.video.recorder.camera.recorder.R;

public class FeedBackDialog {
    private Context context;
    private Dialog dialog;
    private ShareFeedBackListener shareFeedBackListener;
    private ImageButton mbShare, mbNotNow;


    public interface ShareFeedBackListener {
        void shareFeedBack(boolean share);
    }

    public FeedBackDialog(Context context, ShareFeedBackListener shareFeedBackListener) {
        this.context = context;
        this.shareFeedBackListener = shareFeedBackListener;
        initDialog();

    }

    public void initDialog() {
        this.dialog = getDialog();

    }

    private Dialog getDialog() {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_feedback_);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        mbShare = dialog.findViewById(R.id.btnShare);
        mbNotNow = dialog.findViewById(R.id.btnMaybe);


        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

//            // For making background transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            dialog.setCancelable(true);


        mbShare.setOnClickListener(view -> shareFeedBackListener.shareFeedBack(true));
        mbNotNow.setOnClickListener(view -> {
            Log.e("feedbackDialog", "onClick: " + "true called");
            dialog.dismiss();
            shareFeedBackListener.shareFeedBack(false);
        });

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // For making background transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);


        dialog.show();
        return dialog;

    }
}

