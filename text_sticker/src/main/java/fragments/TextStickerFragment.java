package fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.R;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import adapters.BottomItemAdapter;
import interfaces.StickerOperation;
import models.CategoryItem;
import utils.RecyclerTouchListener;
import utils.Utils;


public class TextStickerFragment extends Fragment implements StickerOperation {
    private RecyclerView recyclerViewBottomBar;
    private BottomItemAdapter adapter;
    private FrameLayout containter_frags;
    private ViewGroup editorFragmentContainer;
    private StickerView stickerView;
    private ViewGroup editorRecyclerview;
    private Context context;
    private TextSticker selectedSticker;
    ////////////text text edit section
    public EditText addTextEditText;
    public TextView addTextDoneTextView;
    private InputMethodManager imm;
    private Dialog dialog;
    private String text;
    private ImageView tick_btn,closeTextStickerView;
    private ConstraintLayout bottomBar;
    private boolean newTextSticker = true;

    private static final String TAG = TextStickerFragment.class.getSimpleName();
//    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//            RelativeLayout.LayoutParams.MATCH_PARENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT
//    );
    public TextStickerFragment() {
    }

    public TextStickerFragment(StickerView stickerView, ViewGroup mView, ViewGroup editorFragmentContainer, Context context) {
        this.stickerView = stickerView;
        this.editorRecyclerview = mView;
        this.editorFragmentContainer = editorFragmentContainer;
        this.context = context;
    }

     public static TextStickerFragment newInstance(StickerView view) {

        Bundle args = new Bundle();

        TextStickerFragment fragment = new TextStickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.textsticker_fragment,
                container, false);

        /////////recyclerViews
        recyclerViewBottomBar = view.findViewById(R.id.bottombar_recycler);
        containter_frags = view.findViewById(R.id.contrainer_fragments);

        bottomBar = view.findViewById(R.id.m_included);
        bottomBar.setVisibility(View.GONE);
        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tick_btn", "bottomBar_redColor");
            }
        });
        tick_btn = view.findViewById(R.id.tick_btn);
        closeTextStickerView = view.findViewById(R.id.close_btn);
        tick_btn.setClickable(true);
        tick_btn.setOnClickListener(view12 -> {
            bottomBar.setVisibility(View.GONE);
            containter_frags.setVisibility(View.INVISIBLE);
            editorRecyclerview.setVisibility(View.VISIBLE);

            editorRecyclerview.setEnabled(true);
            recyclerViewBottomBar.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.top_corner_round_card));

        });
        closeTextStickerView.setOnClickListener(view12 -> {
            bottomBar.setVisibility(View.GONE);
            containter_frags.setVisibility(View.INVISIBLE);

            editorRecyclerview.setVisibility(View.VISIBLE);
            editorRecyclerview.setEnabled(true);
            recyclerViewBottomBar.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.top_corner_round_card));
        });
//        view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (editorFragmentContainer.getVisibility() == View.VISIBLE) {
//                    bottomBar.setVisibility(View.GONE);
//                    editorFragmentContainer.setVisibility(View.GONE);
//                    editorFragmentContainer.setEnabled(false);
//                    /////////
//                    editorRecyclerview.setVisibility(View.VISIBLE);
//                    editorRecyclerview.setEnabled(true);
//
//                }
//            }
//        });
        populateRecyclers();
        /////
        if (stickerView != null)
            setStickerViewIcons();
        ////////// Add new sticker

        return view;
    }


    /* Start ///////////////////Black Color Recycler (Bottom Bar)/////////////////////////*/
    private void populateRecyclers() {
        recyclerViewBottomBar.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        List<CategoryItem> items = new ArrayList<>();
        Resources res = getResources();
        Context context = requireActivity();

        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.ic_add_text), res.getString(R.string.add_txt)));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.ic_fontcolor), res.getString(R.string.font_txt)));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.ic_text_color), res.getString(R.string.textColor_txt)));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.ic_text_bag_color), res.getString(R.string.background_btn)));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.ic_text_shadow), res.getString(R.string.shadow_text)));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.ic_gradient), res.getString(R.string.gradientColor_txt)));


        adapter = new BottomItemAdapter(context, (ArrayList<CategoryItem>) items);


        recyclerViewBottomBar.setAdapter(adapter);

        /////// bottom buttons
        recyclerViewBottomBar.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerViewBottomBar, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (editorRecyclerview.getVisibility() == View.VISIBLE) {
                    editorRecyclerview.setVisibility(View.GONE);
                    bottomBar.setVisibility(View.VISIBLE);
                    editorRecyclerview.setEnabled(false);
                    recyclerViewBottomBar.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.bottom_bar_background));
                }
                if (containter_frags.getVisibility() == View.INVISIBLE)
                    containter_frags.setVisibility(View.VISIBLE);
                ///////////////////
                switch (position) {
                    case 0://///////// Add Text Sticker
                        newTextSticker = true;
//                        openAddTextPopupWindow("Text Sticker!");
                        openAddTextPopupWindow("");
                        break;

                    case 1://///////// Font Fragment
                        Utils.addFragment(R.id.contrainer_fragments, requireActivity(), FontsFragment.newinstance(TextStickerFragment.this));
                        break;

                    case 2://///////// Text Color Fragment
                        Utils.addFragment(R.id.contrainer_fragments, requireActivity(), TextColorFragment.newinstance(TextStickerFragment.this));
                        break;

                    case 3://///////// Text Background Fragment
                        Utils.addFragment(R.id.contrainer_fragments, requireActivity(), TextBackgroundFragment.newinstance(TextStickerFragment.this));
                        break;

                    case 4://///////// Text Border Fragment
                        Utils.addFragment(R.id.contrainer_fragments, requireActivity(), BorderFragment.newinstance(TextStickerFragment.this));
                        break;

                    case 5://///////// Text Gradient Fragment
                        Utils.addFragment(R.id.contrainer_fragments, requireActivity(), GradientFragment.newinstance(TextStickerFragment.this));
                        break;

                    case 6://///////// DrawingPaint Fragment
                        break;

                    case 7://///////// Stickers Activity
                        break;

                    case 8://///////// Save Layout
                        break;

                    ///////// show drawing Paint
                    case 9:
                        break;

                    default:
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }


    private void setStickerViewIcons() {
        //currently you can config your own icons and icon event
        //the event you can custom
//        stickerView = new StickerView(this);
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(context,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(context,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(context,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));

        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        //////////////////
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");

            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
//                Toast.makeText(getActivity(), "Double Tap to edit text", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onStickerClicked");
                stickerView.bringToFrontCurrentSticker = true;
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    selectedSticker = (TextSticker) stickerView.getCurrentSticker();
                    text = "" + selectedSticker.getText();
                    newTextSticker = false;
                    openAddTextPopupWindow(text);
                }
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

        });
    }

    public void openAddTextPopupWindow(String text) {
        try {
            requireActivity().runOnUiThread(() -> {
                if (!requireActivity().isFinishing()) {
                    LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.add_text_popup_window, null);
                    ///////// initialization
                    addTextEditText = view.findViewById(R.id.add_text_edit_text);
                    addTextEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    addTextEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    addTextDoneTextView = view.findViewById(R.id.add_text_done_tv);
                    ImageView closePopup = view.findViewById(R.id.closePopUp);

                    if (addTextEditText.getText().toString().isEmpty())
                        addTextDoneTextView.setVisibility(View.GONE);
                    addTextEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String text1 = addTextEditText.getText().toString();
                            if (text1.isEmpty())
                                addTextDoneTextView.setVisibility(View.GONE);
                            else
                                addTextDoneTextView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    /////click listner
                    addTextDoneTextView.setOnClickListener(popupClickview);
                    closePopup.setOnClickListener(popupClickview);

                    if (stringIsNotEmpty(text)) {
                        addTextEditText.setTextColor(Color.WHITE);
                        addTextEditText.setText(text);
                    }

                    imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    addTextEditText.requestFocus();

                    dialog = new Dialog(getActivity(), R.style.FullScreenDialogStyle);
                    dialog.setCancelable(false);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(view);
                    if (!getActivity().isFinishing()) {
                        dialog.show();
//                        if (mAdView != null)
//                            mAdView.setVisibility(View.GONE);
                    }

                }
            });
        } catch (WindowManager.BadTokenException e) {
        }
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener popupClickview = new View.OnClickListener() {
        public void onClick(View view) {
//            if (mAdView != null)
//                mAdView.setVisibility(View.VISIBLE);
            ////////////
            int id = view.getId();
            if (id == R.id.closePopUp) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (dialog != null && !getActivity().isFinishing())
                    dialog.dismiss();

            } else if (id == R.id.add_text_done_tv) {
                text = addTextEditText.getText().toString();

                if (addTextEditText.getText().toString().trim().length() > 0) {
                    setStickerText("" + text);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (dialog != null && !getActivity().isFinishing())
                        dialog.dismiss();
                }
                else Toast.makeText(context, "Add Some Text", Toast.LENGTH_SHORT).show();
                ////
//              layoutTextBottomBar.setVisibility(View.VISIBLE);
//              bottombarLayout.setVisibility(View.GONE);
            }

        }
    };

    private boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    private void showFragment(Fragment fragment) {
        containter_frags.setVisibility(View.VISIBLE);
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.contrainer_fragments, fragment);
            ft.addToBackStack(backStateName);
//          ft.addToBackStack(null);
            ft.commit();
        }
    }

    //////////////////////////Stickers Listener's/////////////////////////

    @Override
    public void setStickerFont(Typeface typeface) {
        if (stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() != null) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    selectedSticker = (TextSticker) stickerView.getCurrentSticker();
                    selectedSticker.setTypeface(typeface);
                    selectedSticker.resizeText();
                    stickerView.replace(selectedSticker);
                    stickerView.invalidate();
                }
            }
        } else
            Toast.makeText(context, "Please Add Text", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setStickerTextColor(String color) {
        if (stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                selectedSticker = (TextSticker) stickerView.getCurrentSticker();
//                selectedSticker.setTextColor(Color.parseColor(color));
                selectedSticker.setTextColorAndremoveTextGradientIfexist(color);
                stickerView.replace(selectedSticker);
                stickerView.invalidate();
            }
        } else
            Toast.makeText(context, "Please Add Text", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setStickerBackgroundColor(String color, int opacity) {
        if (stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                selectedSticker = (TextSticker) stickerView.getCurrentSticker();
                selectedSticker.setDrawable(selectedSticker.setBackgroundColorOpacity(null, color,
                        opacity, selectedSticker.getWidth(), selectedSticker.getHeight()));
                selectedSticker.resizeText();
                stickerView.replace(selectedSticker);
                stickerView.invalidate();
            }
        } else
            Toast.makeText(context, "Please Add Text", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setStickerGradientColor(String startColor, String endColor) {
        if (stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                selectedSticker = (TextSticker) stickerView.getCurrentSticker();
                selectedSticker.setGradientTextColor(startColor, endColor);
                stickerView.replace(selectedSticker);
                stickerView.invalidate();
            }
        } else
            Toast.makeText(context, "Please Add Text", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setStickerBorder(String color) {
        Log.e("TextSticker","OnBorderColorReceved");
        if (stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                selectedSticker = (TextSticker) stickerView.getCurrentSticker();
                selectedSticker.setBorderTextColor(color,1);
                stickerView.replace(selectedSticker);
                stickerView.invalidate();
            }
        } else
            Toast.makeText(context, "Please Add Text", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setBorderSize(int borderSize) {
        if (stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                selectedSticker = (TextSticker) stickerView.getCurrentSticker();

                stickerView.replace(selectedSticker);
                stickerView.invalidate();
            }
        } else
            Toast.makeText(context, "Please Add Text", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setStickerTextAlpha(int opacity) {
        if (stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                selectedSticker = (TextSticker) stickerView.getCurrentSticker();
                selectedSticker.setAlpha(opacity);
                stickerView.replace(selectedSticker);
                stickerView.invalidate();
            }
        } else
            Toast.makeText(context, "Please Add Text", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setStickerText(String text) {
        if (newTextSticker == false && stickerView.getCurrentSticker() != null) {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                selectedSticker.setText("" + text);
                selectedSticker.resizeText();
                stickerView.replace(selectedSticker);
                stickerView.invalidate();
            }
        } else { /// new text sticker
            TextSticker sticker = new TextSticker(getActivity());
            sticker.setText("" + text);
            sticker.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(),
                    R.drawable.sticker_transparent_background)));
            sticker.setTextColor(Color.BLACK);
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
            sticker.resizeText();
            stickerView.addSticker(sticker);
        }
    }
}
