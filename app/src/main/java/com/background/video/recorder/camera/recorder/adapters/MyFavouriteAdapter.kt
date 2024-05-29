package com.background.video.recorder.camera.recorder.adapters


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.background.video.recorder.camera.recorder.adapters.MyFileAdapter.Companion.playDialog
import com.background.video.recorder.camera.recorder.databinding.ItemViewFavouriteBinding
import com.background.video.recorder.camera.recorder.databinding.NativeAdLayoutBinding
import com.background.video.recorder.camera.recorder.model.MediaFiles
import com.background.video.recorder.camera.recorder.ui.fragment.SplashTwoFragment
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.bumptech.glide.Glide
import roozi.app.ads.AdsManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MyFavouriteAdapter(
    val context: Context,
    val list: ArrayList<MediaFiles>,
    val unfavClick: (MediaFiles) -> Unit,
    private val nativeBgEnabled: Boolean  ,
    private val fbNativeSwitch: Boolean  ,
    private val admobNativeid: String  ,
    private val facebookNativeAdid: String,
    private val activity: Activity
) : RecyclerView.Adapter<MyFavouriteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavouriteAdapter.ViewHolder {
        return ViewHolder(
                ItemViewFavouriteBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: MyFavouriteAdapter.ViewHolder, position: Int) {
//        if (getItemViewType(position) == nativeAd) {
//            holder as MyFileAdapter.AdViewHolder
//            AdsManager.nativee(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_Favourites_Native),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_Favourites_Native),
//                context as Activity, holder.binding.nativeAd
//            )
//        } else {
            val data = list[position]

            Glide.with(context).load(data.path).centerCrop().into(holder.binding.thumbnail)
            holder.binding.videoName.text = data.name
            val file = File(data.path)
            val size: Float = (file.length() / 1024).toFloat()
            val mb: Float = (size / 1024)
            try {
                val milisec = MediaPlayer.create(context, Uri.fromFile(file)).duration
                holder.binding.duration.text =
                    SimpleDateFormat("mm:ss").format(Date(milisec.toLong()))
            } catch (e: NullPointerException) {

            }

            holder.binding.size.text = StringBuilder("${Math.round(mb)}MB")

            holder.binding.favourite.setOnClickListener {
                unfavClick.invoke(data)
            }
            holder.itemView.setOnClickListener {
                playDialog(context, data.path, file.extension,nativeBgEnabled, fbNativeSwitch,admobNativeid,facebookNativeAdid,activity)
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemViewFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}