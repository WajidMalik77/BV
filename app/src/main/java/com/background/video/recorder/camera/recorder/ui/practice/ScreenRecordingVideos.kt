//package com.background.video.recorder.camera.recorder.ui.practice
//
//import android.media.MediaMetadataRetriever
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.background.video.recorder.camera.recorder.databinding.FragmentScreenRecordingVideosBinding
//import com.background.video.recorder.camera.recorder.util.FirebaseEvents.Companion.logAnalytic
//import com.background.video.recorder.camera.recorder.util.VideoFileUtil.Screen_Recording
//import roozi.app.ads.AdsManager
//import java.io.File
//import java.util.concurrent.TimeUnit
//
//
//class ScreenRecordingVideos : Fragment() {
//
//    private val binding by lazy {
//        FragmentScreenRecordingVideosBinding.inflate(layoutInflater)
//    }
//    private lateinit var adapter: ScreenRecordingAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        getVideosFile()
//        binding.ibBack.setOnClickListener {
//            if (activity != null) {
//                requireActivity().onBackPressed()
//            }
//        }
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        logAnalytic("MY_SCREEN_RECORDINGS")
//
//    }
//
//    private fun getVideosFile() {
//        val dir = requireActivity().getExternalFilesDir(Screen_Recording)
//        val list = ArrayList<File>()
//        val adapterList = ArrayList<Any>()
//        var duration = "0"
//        val mediaMetadataRetriever = MediaMetadataRetriever()
//        dir?.listFiles()?.let {
//            list.addAll(it)
//            if (list.isNotEmpty())
//                for (i in list) {
//                    mediaMetadataRetriever.setDataSource(i.path)
//
//                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//                        ?.let { s ->
//                            duration = s
//                        }
//                    val durationInSec = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
//                    adapterList.add(
//                        VideoFiles(
//                            i.nameWithoutExtension,
//                            VideoAdapter.getFileSize(i.length()),
//                            durationInSec,
//                            i.path
//                        )
//                    )
//                }
//            adapterList.add(0, "Ad")
//            Log.d("MyScreenKey", "adapterList: $adapterList")
//        }
//
//        binding.rv.layoutManager = LinearLayoutManager(requireContext())
//        adapter = ScreenRecordingAdapter(requireContext(), adapterList) { key, item, pos ->
//            when (key) {
//                "item_Click" -> {
//                    ///    clickedFile = file;
//                    //       videoPositionInList = position;
//                    val bundle = Bundle()
//                    bundle.putString("path", item.path)
//                    NavController.navigateToNext(
//                        binding.root,
//                        R.id.action_screenRecordingVideos_to_mediaPlayer,
//                        bundle
//                    )
//                }
//                "share" -> {
//                    FilesFragment().shareVideo(File(item.path), requireContext())
//                }
//                "delete" -> {
//                    deleteAudio(
//                        File(item.path),
//                        pos
//                    )
//                }
////                "trim" -> {
////                    val bundle = Bundle()
////                    bundle.putString("VideoPath", item.path)
////                    if (activity != null)
////                        findNavController(requireActivity(), R.id.fragmentContainerViewHome).navigate(R.id.action_screenRecordingVideos_to_trimmerFragment, bundle)
////                }
//            }
//        }
//        binding.rv.adapter = adapter
//
//        Log.d("MyScreenKey", "getVideosFile: $list")
//    }
//
//    private fun deleteAudio(file: File, pos: Int) {
//        if (file.exists()) {
//            file.delete()
//            adapter.deleteItem(pos)
//        }
//    }
//}