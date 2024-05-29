package com.background.video.recorder.camera.recorder

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadMediationNative
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadNativeFacebookAd
import com.background.video.recorder.camera.recorder.databinding.FragmentVerifyPasswordBinding
import com.background.video.recorder.camera.recorder.databinding.RemovePasswordDialogBinding
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.background.video.recorder.camera.recorder.ui.fragment.SplashTwoFragment
import com.background.video.recorder.camera.recorder.util.FirebaseEvents
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.UserAuthenticationUtil
import com.background.video.recorder.camera.recorder.util.UserAuthenticationUtil.UserVerification
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel
import roozi.app.ads.AdsManager


class VerifyPassword : Fragment() {
    private val pass: Array<String> = Array(4) {
        return@Array it.toString()
    }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentVerifyPasswordBinding
    private val size = 1
    private var counter = 0
    private val TAG = "VerifyPassword1234"
    private lateinit var verification: UserVerification
    private lateinit var userVerification: UserAuthenticationUtil
    var password = ""
    private lateinit var action: String
    private lateinit var viewModel: MediaFilesViewModel

    var sharedPrefs: SharedPreferences? = null
    var homeInter = false
    var splashInter:kotlin.Boolean = false
    var native_bg = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAd()

        FirebaseEvents.logAnalytic("verify_password_screen")
        FirebaseEvents.Companion.logAnalytic("Verify_Password_Screen_Show")
    }

    private fun showAd() {
        if (isAdded)
            AdsManager.nativee(
                SplashTwoFragment.getBoolean(AdsKeys.Admob_Verify_Password_Native),
                SplashTwoFragment.getBoolean(AdsKeys.Facebook_Verify_Password_Native),
                requireActivity(), binding.nativeAd
            )
    }
    private var verify_password_admob_native = true
    private var verify_password_facebook_native = true
    private var facebook_native_ad_id = ""
    private var admob_native_id = ""
    private var prefs: SharedPrefsHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerifyPasswordBinding.inflate(layoutInflater, container, false)

        val localSharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        prefs = SharedPrefsHelper.getInstance(activity)
        val localPrefs = prefs
        if (localPrefs != null) {
            verify_password_admob_native = localPrefs.getverify_password_admob_nativeSwitch()
            verify_password_facebook_native = localPrefs.getverify_password_facebook_nativeSwitch()
            admob_native_id = localPrefs.getadmob_native_idId()
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId()
            Log.d(
                TAG,
                "onCreate:  admob_interstitial_splash_id  $verify_password_admob_native"
            )
            Log.d(
                TAG,
                "onCreate:  facebook_interstitial_splash_id  $verify_password_facebook_native"
            )
        }



        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
           binding.nativeAd.visibility =View.INVISIBLE
        } else  if (verify_password_admob_native) {
            val activity: Activity? = activity
            if (activity != null) {
                loadMediationNative(
                    activity,
                    binding.nativeAd,
                    admob_native_id,
                    binding.nativeAdContainer,
                    R.layout.ad_native_layout_modified
                )
            }
        } else if (verify_password_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);
            val activity: Activity? = activity
            if (activity != null) {
                loadNativeFacebookAd(
                    facebook_native_ad_id,
                    activity,
                    binding.nativeAdContainer
                )
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off")
            binding.nativeAd.visibility = View.GONE
        }


        if (isAdded)
            sharedPreferences =
                requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        action = arguments?.getString("action").toString()

        if (isAdded)
            viewModel = ViewModelProvider(
                this,
                (ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application) as ViewModelProvider.Factory)
            )[MediaFilesViewModel::class.java]
        setFocus()
        userVerification()
        binding.tvSave.setOnClickListener {
            FirebaseEvents.Companion.logAnalytic("Verify_password_Forget_Btn_Click")
            if (isAdded)
                NavigationUtils.navigate(requireActivity(), R.id.myForgotFragment)
        }
      binding.tvRemove.setOnClickListener {
            FirebaseEvents.Companion.logAnalytic("Verify_password_Forget_Btn_Click")
            if (isAdded)
                warningDialog()
        }
        return binding.root
    }

    fun warningDialog() {
//        if (isAdded) {
        val inflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val bind = RemovePasswordDialogBinding.inflate(inflater)
        val dialog = activity?.let { Dialog(it, android.R.style.Theme_Light_NoTitleBar_Fullscreen) }
        dialog!!.setContentView(bind.root)


        dialog.setOnShowListener {
            if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                bind.nativeAdRemovepassword.visibility =View.INVISIBLE
            } else  if (verify_password_admob_native) {
                val activity: Activity? = activity
                if (activity != null) {
                    loadMediationNative(
                        activity,
                        bind.nativeAdRemovepassword,
                        admob_native_id,
                        bind.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                    )
                }
            } else if (verify_password_facebook_native) {
                // bind.nativeAdRemovepassword.setVisibility(View.GONE);
                val activity: Activity? = activity
                if (activity != null) {
                    loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        bind.nativeAdContainer
                    )
                }
            } else {
                Log.d("checksswitcehs", "onViewCreated: both are off")
                bind.nativeAdRemovepassword.visibility = View.GONE
            }
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
            NavigationUtils.navigate(activity, R.id.verifyPassword, bundle)
        }
        dialog.show()
//        }
    }


    private fun setFocus() {
        binding.tvDigitOne.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == size) {
                    counter++
                    binding.tvDigitTwo.requestFocus()
                    pass[0] = s.toString()
                }
            }
        })
        binding.tvDigitTwo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == size) {
                    counter++
                    binding.tvDigitThree.requestFocus()
                    pass[1] = s.toString()
                }
            }
        })
        binding.tvDigitThree.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == size) {
                    binding.tvDigitFour.requestFocus()
                    counter++
                    pass[2] = s.toString()
                }
            }
        })
        binding.tvDigitFour.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == size) {
                    counter++
                    pass[3] = s.toString()
                    Log.d(TAG, "onTextChanged: " + "i m here")
                    Log.d(TAG, "onTextChanged: " + binding.tvDigitOne.text)
                    Log.d(TAG, "onTextChanged: " + binding.tvDigitTwo.text)
                    Log.d(TAG, "onTextChanged: " + binding.tvDigitThree.text)
                    Log.d(TAG, "onTextChanged: " + binding.tvDigitFour.text)
                    if (counter == 4 && !binding.tvDigitOne.text.toString()
                            .isEmpty() && !binding.tvDigitTwo.text.toString().isEmpty()
                        && !binding.tvDigitThree.text.toString()
                            .isEmpty() && !binding.tvDigitFour.text.toString().isEmpty()
                    ) {
                        if (isAdded)
                            userVerification = UserAuthenticationUtil(
                                requireActivity().baseContext,
                                verification,
                                true
                            )
                        val imm =
                            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.tvDigitFour.windowToken, 0)
                        for (i in pass.indices) {
                            password += pass.get(i)
                        }
                        Log.d(TAG, "onTextChanged:$password")
                        SplashTwoFragment.userEnteredPassword = password.toInt()
                        Log.d(
                            TAG,
                            "onTextChanged: " + SplashTwoFragment.userEnteredPassword
                        )
                        binding.tvDigitOne.text!!.clear()
                        binding.tvDigitTwo.text!!.clear()
                        binding.tvDigitThree.text!!.clear()
                        binding.tvDigitFour.text!!.clear()
                        binding.tvDigitOne.requestFocus()
                        counter = 0
                        password = ""
                        if (isAdded)
                            viewModel.getAllUser
                                .observe(
                                    requireActivity()
                                ) {
                                    userVerification.userAuthentication(it)
                                }
                    } else {
                        binding.tvDigitOne.text!!.clear()
                        binding.tvDigitTwo.text!!.clear()
                        binding.tvDigitThree.text!!.clear()
                        binding.tvDigitFour.text!!.clear()
                        binding.tvDigitOne.requestFocus()
                        counter = 0
                        password = ""
                        Log.d(TAG, "afterTextChanged: ")
                    }

                    // userDetailsProvider.getUserDetails(stringBuilder);
                }
            }
        })
    }

    private fun userVerification() {
        verification = UserVerification { password: Boolean ->
            if (password) {
                if (isAdded)
                    requireActivity().runOnUiThread {
                        binding.passLay.visibility = View.GONE
                        binding.animation.setAnimation(R.raw.anim_correct_password)
                        binding.animation.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                Log.d(TAG, "run: " + "pass correct")
                                when (action) {
                                    "recordings" ->
                                        if (isAdded)
                                            Navigation.findNavController(
                                                requireActivity(),
                                                R.id.fragmentContainerViewHome
                                            )
                                                .navigate(R.id.filesFragment)
                                    "trim" ->
                                        if (isAdded)
                                            Navigation.findNavController(
                                                requireActivity(),
                                                R.id.fragmentContainerViewHome
                                            )
                                                .navigate(R.id.trimFragment)
                                    "favourite" ->
                                        if (isAdded)
                                            Navigation.findNavController(
                                                requireActivity(),
                                                R.id.fragmentContainerViewHome
                                            )
                                                .navigate(R.id.favFilesFragment)
                                 "gallery" ->
                                        if (isAdded)
                                            Navigation.findNavController(
                                                requireActivity(),
                                                R.id.fragmentContainerViewHome
                                            )
                                                .navigate(R.id.galleryFragment)
                                    "settings" -> {
                                        if (isAdded)
                                            Navigation.findNavController(
                                                requireActivity(),
                                                R.id.fragmentContainerViewHome
                                            )
                                                .navigate(R.id.homeFragment)
                                        editor.putBoolean("appLocked", false)
                                        editor.apply()
                                        viewModel.deleteAllTable()
                                    }
                                }
                            }, 2000)
                    }
            } else {
                if (isAdded)
                    requireActivity().runOnUiThread {
                        binding.passLay.visibility = View.GONE
                        binding.animation.setAnimation(R.raw.anim_wrong_password)
                        binding.animation.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                binding.passLay.visibility = View.VISIBLE
                                binding.animation.visibility = View.GONE
                            }, 2000)
                    }
            }
        }
    }


}