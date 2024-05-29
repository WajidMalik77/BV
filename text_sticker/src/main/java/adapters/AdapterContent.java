package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.R;

import java.util.List;

import utils.QueryBuilder;

public class AdapterContent extends
        RecyclerView.Adapter<AdapterContent.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Handler mHandler = null;
    private onTypeFaceclick mClickListener;
    private Context context;
    private boolean isTTfFonts = false;

    // data is passed into the constructor
    public AdapterContent(Context context, List<String> data, boolean isTTfFonts) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.isTTfFonts = isTTfFonts;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isTTfFonts) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), mData.get(holder.getAdapterPosition()));
            holder.myTextView.setTypeface(typeface);
        } else
            requestDownload(mData.get(position), holder.myTextView);
        holder.myTextView.setOnClickListener(view -> {
            if (mClickListener != null) {
                if (isTTfFonts)
                    mClickListener.onTypeFaceItemClick(mData.get(holder.getAdapterPosition()), true);
                else
                    mClickListener.onTypeFaceItemClick(mData.get(holder.getAdapterPosition()), false);
            }
        });
    }

    private void requestDownload(String familyName, final TextView textView) {
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
                textView.setTypeface(typeface);
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
            }
        };
        FontsContractCompat
                .requestFont(context, request, callback,
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

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName2);
            myTextView.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            if (mClickListener != null)
//                mClickListener.onTypeFaceItemClick(mData.get(getAdapterPosition()));
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(onTypeFaceclick itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface onTypeFaceclick {
        void onTypeFaceItemClick(String fontPath, boolean isTTfFonts);
    }

    public void refreshItem(Typeface typeface) {

    }
}

