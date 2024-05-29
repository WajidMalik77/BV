package com.background.video.recorder.camera.recorder.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.adapters.QualityAdapter
import com.background.video.recorder.camera.recorder.databinding.CameraSelectorDialogBinding
import com.background.video.recorder.camera.recorder.databinding.FragmentMySettingBinding
import com.background.video.recorder.camera.recorder.databinding.RemovePasswordDialogBinding
import com.background.video.recorder.camera.recorder.model.QualityModel
import com.background.video.recorder.camera.recorder.util.FirebaseEvents
import com.background.video.recorder.camera.recorder.util.FirebaseEvents.Companion.logAnalytic
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils
import roozi.app.ads.AdsManager
import roozi.app.billing.BillingManager


class MySettingFragment : Fragment() {
    private lateinit var binding: FragmentMySettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var show = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMySettingBinding.inflate(layoutInflater, container, false)
        if (isAdded) {

            sharedPreferences =
                requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE)
        }
        editor = sharedPreferences.edit()
        binding.lockSwitch.isChecked = sharedPreferences.getBoolean("appLocked", false)
        loadData()
        setQuality()
        listeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAd()
        FirebaseEvents.Companion.logAnalytic("Settings_screen_Show")
    }

    private fun showAd() {
        AdsManager.nativee(
            SplashTwoFragment.getBoolean(AdsKeys.Admob_Settings_Native),
            SplashTwoFragment.getBoolean(AdsKeys.Facebook_Settings_Native),
            requireActivity(), binding.nativeAd
        )
    }

    private fun loadData() {

        if (isFront()) {
            binding.currentCamera.text = "Front"
        } else {
            binding.currentCamera.text = " Back"
        }
        if (sharedPreferences.getBoolean("noSound", false)) {
            binding.silentCheck.isChecked = true
        }
    }

    private fun listeners() {

        binding.videoCamera.setOnClickListener {
            FirebaseEvents.Companion.logAnalytic("Setting_Video_Camera_Btn_Click")
            if (isAdded)
                cameraSelectorDialog(requireContext())
        }
        binding.silentCheck.setOnCheckedChangeListener { compoundButton, b ->
            Log.d("MYCHeckBox", "listeners: $b")
            FirebaseEvents.Companion.logAnalytic("Settings_Silent_Record_Btn_Click")
            if (b) {
                editor.putBoolean("noSound", true)
                editor.apply()
            } else if (!b) {
                editor.putBoolean("noSound", false)
                editor.apply()
            }
        }
        binding.qualityy.setOnClickListener {
            FirebaseEvents.Companion.logAnalytic("Settings_Video_Quality_Btn_Click")
            if (isAdded)
                openMenu(requireContext())
        }
        binding.lock.setOnClickListener {
            FirebaseEvents.Companion.logAnalytic("Settings_Lock_Btn_Click")
            if (sharedPreferences.getBoolean("appLocked", false)) {
                warningDialog()
//                Toast.makeText(requireContext(), "Already Set ", Toast.LENGTH_SHORT).show()
            } else {
                if (isAdded)
                    NavigationUtils.navigate(requireActivity(), R.id.mySetPassword)
            }
        }
        binding.terms.setOnClickListener {
            FirebaseEvents.Companion.logAnalytic("Settings_Term_Btn_CLick")
            if (isAdded)
                BillingManager.showPrivacyDialog(
                    requireContext(),
                    "https://barakatappssole.wordpress.com/2022/11/03/terms-conditions/",
                    "Terms & Condition"
                )
        }
        binding.privacy.setOnClickListener {
            FirebaseEvents.Companion.logAnalytic("Settings_Privacy_Btn_CLick")
            if (isAdded)
                BillingManager.showPrivacyDialog(
                    requireContext(),
                    "https://barakatappssole.wordpress.com/blog/",
                    "Privacy Policy"
                )
        }
    }

    fun warningDialog() {
        if (isAdded) {
            val inflater =
                requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bind = RemovePasswordDialogBinding.inflate(inflater)
            val dialog = Dialog(requireContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen)
            dialog.setContentView(bind.root)

            dialog.setOnShowListener {
                AdsManager.nativee(
                    SplashTwoFragment.getBoolean(AdsKeys.Admob_Remove_Password_Dialog_Native),
                    SplashTwoFragment.getBoolean(AdsKeys.Facebook_Remove_Password_Dialog_Native),
                    requireActivity() , bind.nativeAdRemovepassword
                )
            }

            bind.back.setOnClickListener {
                dialog.dismiss()
            }
            bind.pro.setOnClickListener {
//                BillingManager.showPremium(requireContext()) {}
            }
            bind.cancel.setOnClickListener {
                dialog.dismiss()
            }
            bind.yesBtn.setOnClickListener {
                dialog.dismiss()
                val bundle = Bundle()
                bundle.putString("action", "settings")
                NavigationUtils.navigate(requireActivity(),R.id.verifyPassword, bundle)
            }
            dialog.show()
        }
    }

    private fun setQuality() {
        val quality = sharedPreferences.getInt("quality", 0)
        when (quality) {
            1 -> binding.quality.text = "1080P"
            2 -> binding.quality.text = "720P"
            3 -> binding.quality.text = "480P"
            4 -> binding.quality.text = "144P"
        }
    }

    private fun openMenu(context: Context) {
        val popupWindow = ListPopupWindow(context)
        if (show) {
            show = false
            val qualityAdapter = QualityAdapter(context, getQualityList())
            popupWindow.anchorView = binding.dropDownImage
            popupWindow.width = 600
            popupWindow.setAdapter(qualityAdapter)
            popupWindow.setOnItemClickListener { p0, p1, p2, p3 ->
                when (p2) {
                    0 -> {
                        editor.putInt("quality", 1)
                        editor.apply()
                        binding.quality.text = "1080P"
                    }
                    1 -> {
                        editor.putInt("quality", 2)
                        editor.apply()
                        binding.quality.text = "720P"
                    }
                    2 -> {
                        editor.putInt("quality", 3)
                        editor.apply()
                        binding.quality.text = "480P"
                    }
                    3 -> {
                        editor.putInt("quality", 4)
                        editor.apply()
                        binding.quality.text = "144P"
                    }
                }
                popupWindow.dismiss()
            }
            Log.d("MyKey", "onBindViewHolder:${popupWindow.isModal}")
            popupWindow.show()
        } else {
            show = true
            popupWindow.dismiss()
        }

    }

    fun cameraSelectorDialog(
        context: Context
    ) {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val bind = CameraSelectorDialogBinding.inflate(inflater)
        val dialog = Dialog(context)
        dialog.setContentView(bind.root)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.decorView.setBackgroundColor(Color.TRANSPARENT)
        if (isFront()) {
            bind.backCam.isChecked = false
            bind.frontCam.isChecked = true
        } else {
            bind.backCam.isChecked = true
            bind.frontCam.isChecked = false
        }
        bind.backCam.setOnClickListener {
            bind.backCam.isChecked = true
            bind.frontCam.isChecked = false
        }
        bind.frontCam.setOnClickListener {
            bind.frontCam.isChecked = true
            bind.backCam.isChecked = false
        }
        bind.startBtn.setOnClickListener {
            if (bind.backCam.isChecked) {
                binding.currentCamera.text = " Back"
                editor.putBoolean("frontCamera", false)
                editor.apply()
            } else {
                binding.currentCamera.text = " Front"
                editor.putBoolean("frontCamera", true)
                editor.apply()
            }

            dialog.dismiss()

        }
        dialog.show()
    }

    fun isFront(): Boolean {
        return sharedPreferences.getBoolean("frontCamera", false)
    }

    fun getQualityList(): List<QualityModel> {
        val listt: MutableList<QualityModel> = ArrayList()
        listt.add(QualityModel("1080P"))
        listt.add(QualityModel("720P"))
        listt.add(QualityModel("480P"))
        listt.add(QualityModel("144P"))
        return listt
    }
}