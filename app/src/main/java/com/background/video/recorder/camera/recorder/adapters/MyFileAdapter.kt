package com.background.video.recorder.camera.recorder.adapters

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadMediationNative
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadNativeFacebookAd
import com.background.video.recorder.camera.recorder.databinding.NativeAdLayoutBinding
import com.background.video.recorder.camera.recorder.databinding.VideoItemViewBinding
import com.background.video.recorder.camera.recorder.databinding.VideoPreviewDialogBinding
import com.background.video.recorder.camera.recorder.model.MediaFiles
import com.background.video.recorder.camera.recorder.ui.fragment.FilesFragment.getMenu
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import roozi.app.billing.BillingManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class MyFileAdapter(
    val context: Context,
    var list: ArrayList<MediaFiles>,
    private val click: (MediaFiles) -> Unit,
    private val delete: (MediaFiles, Int) -> Unit,
    private val rename: (String, Int) -> Unit,
    private val nativeBgEnabled: Boolean,
    private val fbNativeSwitch: Boolean,
    private val admobNativeid: String,
    private val facebookNativeAdid: String,
    private val activity: Activity
) :
//    context: Context, path: String, extention: String, nativeBgEnabled:Boolean, fbNativeSwitch : Boolean, admobNativeid : String,
//facebookNativeAdid :String, activity :Activity
    RecyclerView.Adapter<MyFileAdapter.MyViewHolder>() {
    private val popupWindow = ListPopupWindow(context)
    private var isShow = true
    fun updateData(newMediaFiles: Collection<MediaFiles>) {
        list.clear()
        list.addAll(newMediaFiles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            VideoItemViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        if (getItemViewType(position) == nativeAd) {
//            holder as AdViewHolder
//                AdsManager.nativee(
//                    SplashTwoFragment.getBoolean(AdsKeys.Admob_Recordings_Native),
//                    SplashTwoFragment.getBoolean(AdsKeys.Facebook_Recordings_Native),
//                    context as Activity, holder.binding.nativeAd
//                )
//        } else {
        val data = list[position]
        if (data.type == 1) {
            holder.binding.favourite.setImageDrawable(
                context.resources.getDrawable(
                    R.drawable.favourite_fill,
                    null
                )
            )
        } else {

            holder.binding.favourite.setImageDrawable(
                context.resources.getDrawable(
                    R.drawable.favourite,
                    null
                )
            )
        }

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
            holder.binding.favourite.setImageDrawable(
                context.resources.getDrawable(
                    R.drawable.favourite_fill,
                    null
                )
            )
            click.invoke(data)
        }

        holder.binding.more.setOnClickListener {
            if (isShow) {
                isShow = false
                val popupAdapter = PopupAdapter(context, getMenu(context))
                popupWindow.anchorView = it
                popupWindow.width = 600
                popupWindow.setAdapter(popupAdapter)
                // Set background color
                popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))


                popupWindow.setOnItemClickListener { _, _, positionInMenu, _ ->
                    when (positionInMenu) {
                        0 -> {
                            rename.invoke(data.name, position)
                        }

                        1 -> {
                            Log.d("MYLOC", "onBindViewHolder: ${data.path}")
                            shareVideo(data.path)
                        }

                        2 -> {
                            // Inflate the custom view
                            val customView = LayoutInflater.from(context)
                                .inflate(R.layout.custom_delete_dialog, null)

                            // Confirmation dialog for delete using custom layout
                            val dialog = AlertDialog.Builder(context)
                                .setView(customView)
                                .create()

                            customView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.custom_positive_button)
                                .setOnClickListener {
                                    delete.invoke(data, position)
                                    dialog.dismiss()
                                }

                            customView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.custom_negative_button)
                                .setOnClickListener {
                                    dialog.dismiss()
                                }

                            dialog.show()
                        }
                    }
                    isShow = true
                    popupWindow.dismiss()
                }
                Log.d("MyKey", "onBindViewHolder:${popupWindow.isModal}")
                popupWindow.show()
            } else {
                isShow = true
                popupWindow.dismiss()
            }
        }

        holder.itemView.setOnClickListener {
            if (!isShow) {
                isShow = true
                popupWindow.dismiss()
            } else {
                playDialog(
                    context,
                    data.path,
                    file.extension,
                    nativeBgEnabled,
                    fbNativeSwitch,
                    admobNativeid,
                    facebookNativeAdid,
                    activity
                )

            }
        }
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun shareVideo(filePath: String) {

        val videoFile = File(filePath)
        val videoURI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            FileProvider.getUriForFile(context, context.packageName, videoFile)
        else
            Uri.fromFile(videoFile)
        ShareCompat.IntentBuilder.from(context as Activity)
            .setStream(videoURI)
            .setType("video/mp4")
            .setChooserTitle("Share video...")
            .startChooser()
    }

    class MyViewHolder(val binding: VideoItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    class AdViewHolder(val binding: NativeAdLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    companion object {
        fun playDialog(
            context: Context,
            path: String,
            extention: String,
            nativeBgEnabled: Boolean,
            fbNativeSwitch: Boolean,
            admobNativeid: String,
            facebookNativeAdid: String,
            activity: Activity
        ) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bind = VideoPreviewDialogBinding.inflate(inflater)
            val dialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)

            dialog.setContentView(bind.root)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.setOnShowListener {
//                AdsManager.nativee(
//                    SharePref.getBoolean(AdsKeys.Admob_Play_video_native, false),
//                    SharePref.getBoolean(AdsKeys.Facebook_Play_video_native, false),
//                    context as Activity,
//                    bind.nativeAd

                Log.d("hoem_frag", "onCreateView: Trim  fargment")

                if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                    bind.nativeAd.visibility = View.GONE
                } else if (nativeBgEnabled) {
                    loadMediationNative(
                        activity,
                        bind.nativeAd,
                        admobNativeid,
                        bind.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                    )
                } else if (fbNativeSwitch) {
                    loadNativeFacebookAd(
                        facebookNativeAdid,
                        activity,
                        bind.nativeAdContainer
                    )

                } else {
                    Log.d("checksswitcehs", "onViewCreated: both are off")
                    bind.nativeAd.setVisibility(View.GONE)
                }

            }

            bind.pro.setOnClickListener{
                BillingManager.showPremium(context){

                }
            }

            val player = ExoPlayer.Builder(context).build()
            val mediaItem = MediaItem.Builder()
                .setUri(path)
                .setMimeType(extention)
                .build()
            player.prepare()
            player.play()

            Handler(Looper.getMainLooper()).postDelayed({
                bind.player.visibility = View.VISIBLE
                bind.progress.visibility = View.GONE
                player.setMediaItem(mediaItem)
                bind.player.player = player
                bind.player.useController = true
            }, 2000)


            bind.back.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnDismissListener {
                player.stop()
                it.dismiss()
            }
            dialog.show()
        }
    }
}