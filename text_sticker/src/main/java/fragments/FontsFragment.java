package fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.AdapterContent;
import adapters.AdapterFontCat;
import interfaces.StickerOperation;
import utils.QueryBuilder;
import utils.Utils;

public class FontsFragment extends Fragment
        implements AdapterFontCat.ItemClickListener, AdapterContent.onTypeFaceclick {

    private ConstraintLayout parentlayout;
    private ImageView slideUPDownImage;
    private String slideUP = "slideup";
    private String slideDOWN = "slidedown";
    private StickerOperation stickerOperation;

    ///////////
    private RecyclerView catRecycler, mainRecycler;
    private AdapterFontCat adapter;
    private AdapterContent adapterContent;
    private ArrayList<String> fontCat = new ArrayList<>();
    private ArrayList<String> fontName = new ArrayList<>();
    private Handler mHandler = null;
    private int isTTfFonts = 6; // 6 is fonts asset folders

    private ArrayList<List<String>> mFamilyNameSet = new ArrayList<>();

    public FontsFragment() {
    }

    public FontsFragment(StickerOperation stickerOperation) {
        this.stickerOperation = stickerOperation;
    }

    public static Fragment newinstance(StickerOperation stickerOperation) {

        return new FontsFragment(stickerOperation);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fonts_fragment,
                container, false);
        parentlayout = view.findViewById(R.id.parentlayout);
        slideUPDownImage = view.findViewById(R.id.slideup);
        slideUPDownImage.setOnClickListener(view1 -> {
            //callDropDownUPFunction();
        });
        ///////////////////
        mainRecycler = view.findViewById(R.id.main_recycler);
        mainRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false));
        catRecycler = view.findViewById(R.id.cat_recycler);
        fontCat.add("Blur");
        fontCat.add("Brush");
        fontCat.add("Calligraphy");
        fontCat.add("Decorative");
        fontCat.add("Dotted");
        fontCat.add("Groovy");
        fontCat.add("Curly");
        fontCat.add("Boldy");
        fontCat.add("Fancy");
        fontCat.add("Stylish");
        fontCat.add("Modern");
        fontCat.add("Cappy");
        // set up the RecyclerView
        catRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        adapter = new AdapterFontCat(getActivity(), fontCat);
        adapter.setClickListener(this);
        catRecycler.setAdapter(adapter);
        ////////////////Get all fonts Arrays and add in arraylist index
//        Utils.fetchAssetsFontsPath(getActivity(), "fonts/brush");
//        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/brush");
        mFamilyNameSet.add(Utils.fetchAssetsFontsPath(getActivity(), "fonts/blur")); // asset folder
        mFamilyNameSet.add(Utils.fetchAssetsFontsPath(getActivity(), "fonts/brush")); // asset folder
        mFamilyNameSet.add(Utils.fetchAssetsFontsPath(getActivity(), "fonts/calligraphy")); // asset folder
        mFamilyNameSet.add(Utils.fetchAssetsFontsPath(getActivity(), "fonts/decorative")); // asset folder
        mFamilyNameSet.add(Utils.fetchAssetsFontsPath(getActivity(), "fonts/dotted")); // asset folder
        mFamilyNameSet.add(Utils.fetchAssetsFontsPath(getActivity(), "fonts/groovy")); // asset folder
        mFamilyNameSet.add(Arrays.asList(getResources().getStringArray(R.array.Curly))); // font family google
        mFamilyNameSet.add(Arrays.asList(getResources().getStringArray(R.array.Boldy))); // font family google
        mFamilyNameSet.add(Arrays.asList(getResources().getStringArray(R.array.Fancy))); // font family google
        mFamilyNameSet.add(Arrays.asList(getResources().getStringArray(R.array.Stylish))); // font family google
        mFamilyNameSet.add(Arrays.asList(getResources().getStringArray(R.array.Modern))); // font family google
        mFamilyNameSet.add(Arrays.asList(getResources().getStringArray(R.array.Cappy))); // font family google
        ///////////// initial typeface
        onItemClick(0);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    private void callDropDownUPFunction() {
        if (slideUPDownImage.getTag() == slideUP) {
            slideDownScreen();
            slideUPDownImage.setTag(slideDOWN);
            slideUPDownImage.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            mainRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false));
        } else {
            slideUpScreen();
            slideUPDownImage.setTag(slideUP);
            slideUPDownImage.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            mainRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false));
        }
    }

    private void slideUpScreen() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) parentlayout.getLayoutParams();
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        parentlayout.setLayoutParams(params);
    }

    private void slideDownScreen() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) parentlayout.getLayoutParams();
        params.height = getActivity().getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp);
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        parentlayout.setLayoutParams(params);
    }

    @Override
    public void onTypeFaceItemClick(String fontPath, boolean isTTfFonts) {
        if (isTTfFonts) {
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
            if (stickerOperation != null) {
                stickerOperation.setStickerFont(typeface);
            }
        } else
            requestDownload(fontPath);
    }

    @Override
    public void onItemClick(int position) {
        if (position < isTTfFonts)
            adapterContent = new AdapterContent(getActivity(), mFamilyNameSet.get(position), true);
        else
            adapterContent = new AdapterContent(getActivity(), mFamilyNameSet.get(position), false);
        adapterContent.setClickListener(this);
        mainRecycler.setAdapter(adapterContent);
    }
    //////////

    private void requestDownload(String familyName) {
        QueryBuilder queryBuilder = new QueryBuilder(familyName);
        String query = queryBuilder.build();

//        Log.d(TAG, "Requesting a font. Query: " + query);
        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs);
        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat
                .FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                if (stickerOperation != null) {
                    stickerOperation.setStickerFont(typeface);
                }
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
            }
        };
        FontsContractCompat
                .requestFont(getActivity(), request, callback,
                        getHandlerThreadHandler());
    }

    private Handler getHandlerThreadHandler() {
        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }
}
