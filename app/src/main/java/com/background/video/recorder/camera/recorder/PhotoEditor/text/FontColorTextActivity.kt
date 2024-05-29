package com.background.video.recorder.camera.recorder.PhotoEditor.text

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.background.video.recorder.camera.recorder.PhotoEditor.EditImageActivity
import com.background.video.recorder.camera.recorder.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import java.io.ByteArrayOutputStream

class FontColorTextActivity : AppCompatActivity(), View.OnClickListener, addTextInterface {

    private lateinit var pref: SettingsSharedPref
    private lateinit var done_text: ImageView
    private lateinit var back_text: ImageView
    var mAdView: AdView? = null
    var text: String? = null

    var texttosend: String? = null
    var textTypefacetosend: String? = null
    var textColortosend: Int? = 0
    private var activity: AppCompatActivity? = null
    lateinit var tempTypeface : Typeface
    var  typefaceFont : Typeface? = null
    lateinit var linearLayoutbanner: LinearLayout


    private var mInterstitialAd: InterstitialAd? = null
    var context: Context? = null
    private lateinit var fontsClick: View
    private lateinit var colorClick: View
    private lateinit var colorText: TextView
    private lateinit var fontText: TextView
    private lateinit var selectColor: RelativeLayout
    private lateinit var selectFonts: RelativeLayout
    private lateinit var previewTxt: EditText
    private var returnIntent: Intent? = null
    private var fromTop: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_colortextactivity)

        fromTop = intent.getStringExtra("fromTop")

        typefaceFont=Typeface.createFromAsset(getAssets(), "fonts/Choir.ttf")

        initThings()
//        if (!pref.prefForProVersion) {
//            if (!pref.prefForInAPPPurchase) {
//                initializeAds()
//            }
//        }
        loadFragment(ColorFragment())


        done_text.setOnClickListener {
            setTextImageview()

        }
        previewTxt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        previewTxt.viewTreeObserver
            .addOnGlobalLayoutListener {
                if (isKeyboardShown(previewTxt.rootView)) {
                    mAdView?.pause()
                } else {
                    mAdView?.resume()
                }
            }


    }
    private fun initThings() {
        pref = SettingsSharedPref(this)
        context = this
        fontsClick = findViewById(R.id.fontClick)
        colorClick = findViewById(R.id.colorClick)
        colorText = findViewById(R.id.color)
        fontText = findViewById(R.id.fonts)
        selectColor = findViewById(R.id.selectColor)
        selectFonts = findViewById(R.id.fontSelect)
        back_text = findViewById(R.id.back_textEd)
        previewTxt = findViewById(R.id.typetxt_edt)
//        linearLayoutbanner = findViewById(R.id.linearLayoutbanner)
        done_text = findViewById(R.id.done_textEd)
        selectColor.setOnClickListener(this)
        back_text.setOnClickListener(this)
        selectFonts.setOnClickListener(this)
        colorClick.visibility = View.VISIBLE
        colorText.setTextColor(resources.getColor(R.color.red_color_picker))
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) inputMethodManager.hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }

    override fun onClick(v: View) {
        hideKeyboard(v)
        when (v.id) {
            R.id.selectColor -> {
                colorClick.visibility = View.VISIBLE
                fontsClick.visibility = View.GONE
                colorText.setTextColor(resources.getColor(R.color.red_color_picker))
                fontText.setTextColor(resources.getColor(R.color.black))
                loadFragment(ColorFragment())
            }
            R.id.fontSelect -> {
                colorClick.visibility = View.GONE
                fontsClick.visibility = View.VISIBLE
                colorText.setTextColor(resources.getColor(R.color.black))
                fontText.setTextColor(resources.getColor(R.color.red_color_picker))
                loadFragment(FontFragment())
            }
            R.id.back_textEd -> finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }


    override fun updateMyTextColor(textColor: Int) {

        textColortosend = textColor
        previewTxt.setTextColor(textColor)

    }

    override fun updateMyTextFont(textFontPath: String?) {
        typefaceFont = Typeface.createFromAsset(
            assets,
            textFontPath
        )
        previewTxt.typeface = typefaceFont
        textTypefacetosend = textFontPath
    }

//    private fun setTextImageview() {
//        if (previewTxt.text.toString() == "") {
//            Toast.makeText(this@FontColorTextActivity, "Please Enter Some Text", Toast.LENGTH_SHORT).show()
//            finish()
//        } else {
//            textColortosend?.let {
//
//                returnIntent = Intent(this@FontColorTextActivity, EditImageActivity::class.java)
//                texttosend = previewTxt.text.toString()
//                returnIntent?.putExtra("textOriginal", texttosend)
//                val editImageActivity = EditImageActivity()
//                EditImageActivity.textBitmap = textAsBitmap(previewTxt.text.toString(),100F, previewTxt.currentTextColor,typefaceFont)
//                Log.d("TextBitmapForEdit", "setTextImageview: "+EditImageActivity.textBitmap)
//
////                returnIntent?.putExtra("textOriginal", textBitmap)
//
////                textBitmap = textBitmapEditor
////                returnIntent?.putExtra("textOriginal", textBitmap)
//                Log.d("TextBitmapForEditOr", "setTextImageviewOrignl: "+previewTxt.text.toString())
//                setResult(RESULT_OK, returnIntent)
//                finish()
//            }
//
//
//        }
//    }

    private fun setTextImageview() {
        if (previewTxt.text.toString() == "") {
            Toast.makeText(this@FontColorTextActivity, "Please Enter Some Text", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            textColortosend?.let {
                val returnIntent = Intent(this@FontColorTextActivity, EditImageActivity::class.java)
                texttosend = previewTxt.text.toString()
                returnIntent.putExtra("textOriginal", texttosend)

                // Create a bitmap from the entered text
                val textBitmap = textAsBitmap(previewTxt.text.toString(), 100F, previewTxt.currentTextColor, typefaceFont)

                // Convert the text bitmap to a byte array to pass it as Parcelable extra
                val byteArrayOutputStream = ByteArrayOutputStream()
                textBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                returnIntent.putExtra("textBitmap", byteArray)

                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }
    }




    private fun isKeyboardShown(rootView: View): Boolean {
        /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
        val SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm: DisplayMetrics = rootView.resources.displayMetrics
        /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
        val heightDiff: Int = rootView.bottom - r.bottom
        /* Threshold size: dp to pixels, multiply with display density */
        val isKeyboardShown: Boolean = heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density
        Log.d(
            "Keyboard ",
            "isKeyboardShown ? " + isKeyboardShown + ", heightDiff:" + heightDiff + ", density:" + dm.density
                    + "root view height:" + rootView.height + ", rect:" + r
        )
        return isKeyboardShown
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.i("AddTextSpinner", "textSpinner : $hasFocus")
        if (hasFocus) {

//            linearLayoutbanner.visibility = View.VISIBLE
        } else {

//            linearLayoutbanner.visibility = View.VISIBLE
        }
    }



    fun textAsBitmap(text: String?, textSize: Float, textColor: Int, typeface: Typeface?): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setTextSize(textSize)
        paint.setColor(textColor)
        paint.setTypeface(typeface)
        paint.setTextAlign(Paint.Align.LEFT)
//        val baseline: Float = -paint.ascent() + 40 // ascent() is negative
        val baseline: Float = -paint.ascent() // Remove the padding addition

        val width = (paint.measureText(text) + 120.5).toFloat().toInt() as Int // round
        val height = (baseline + paint.descent() + 120.5).toFloat().toInt() as Int
        val image: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        if (text != null) {
            canvas.drawText(text, 50F, baseline, paint)
        }
        return image
    }


    override fun onResume() {
        super.onResume()
        mAdView?.resume()
    }

    override fun onPause() {
        super.onPause()
        mAdView?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdView?.destroy()
    }
}