package roozi.app.billing

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import roozi.app.R
import roozi.app.ads.AdsHelper
import roozi.app.ads.AdsManager
import roozi.app.databinding.LayoutPremiumApprovedBinding
import roozi.app.databinding.LayoutPremiumModifiedBinding
import roozi.app.databinding.LayoutPremiumNewBinding
import roozi.app.databinding.PrivacyPolycyBinding
import roozi.app.interfaces.IPurchaseListener
import roozi.app.models.AdData


class BillingManager {
    companion object {

        lateinit var purchaseListener: IPurchaseListener
        var click = "yearly"


        fun showPremium(context: Context, closed: (Boolean) -> Unit) {

            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val premiumBinding = LayoutPremiumBinding.inflate(inflater)
//            val premiumBinding = LayoutPremiumModifiedBinding.inflate(inflater)
            val premiumBinding = LayoutPremiumApprovedBinding.inflate(inflater)

            val dialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
            dialog.setCancelable(true)
            dialog.setContentView(premiumBinding.root)
            HandlePremiumClick(premiumBinding, context)

            customTextView(context, premiumBinding.tvTermsandoprivacy) {
                dialog.dismiss()
            }
            customTextViewUnderline(context, premiumBinding.textView13) {
                dialog.dismiss()
            }

            LifeTimeSku?.let {
                premiumBinding.weeklyPricelifeTimeCardView.text = it.price
            }
            YearlySku?.let {
                premiumBinding.yearlyPrice.text = it.price
            }
            MonthlySku?.let {
                premiumBinding.monthlyPrice.text = it.price
            }
            WeeklySku?.let {
                premiumBinding.weeklyPrice.text = it.price
            }
            premiumBinding.iLikeAd.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnShowListener {
                AdsManager.isInterShowing = true


            }





            premiumBinding.continueBtn.setOnClickListener {

                if (!AdsHelper.isNetworkAvailable(context)){
                    Toast.makeText(context, "Disconnected ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                when (click) {
                    "yearly" -> {
                        YearlySku?.let {
                            launchPurchaseFlow(context as Activity, it)
                        }

                    }

                    "monthly" -> {
                        MonthlySku?.let {
                            launchPurchaseFlow(context as Activity, it)
                        }
                    }

                    "weekly" -> {
                        WeeklySku?.let {
                            launchPurchaseFlow(context as Activity, it)
                        }
                    }

                    "lifeTime" -> {
                        LifeTimeSku?.let {
                            launchPurchaseFlow(context as Activity, it)
                        }

                    }
                }
                Log.d("showPremium", "showPremium: $click")
            }
            dialog.setOnDismissListener {
                AdsManager.isInterShowing = false
                closed.invoke(true)
                dialog.dismiss()
            }
            dialog.show()
        }

        private fun customTextViewUnderline(context: Context, tvTermsAndPrivacy: TextView, function: () -> Unit) {
            // Retrieve the current text from the TextView
            val currentText = tvTermsAndPrivacy.text.toString()

            // Create a SpannableString from the current text
            val spannableString = SpannableString(currentText)

            // Apply the UnderlineSpan to the entire text
            spannableString.setSpan(UnderlineSpan(), 0, currentText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Set the modified text back to the TextView
            tvTermsAndPrivacy.text = spannableString

            // Call the function passed as a parameter
            function.invoke()
        }

        @SuppressLint("NewApi")
//        fun HandlePremiumClick(binding: LayoutPremiumBinding, context: Context) {
//        fun HandlePremiumClick(binding: LayoutPremiumModifiedBinding, context: Context) {
        fun HandlePremiumClick(binding: LayoutPremiumApprovedBinding, context: Context) {
            binding.yearly.setOnClickListener {
                click = "yearly"
                binding.yearly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.one_year_bg, null)
//                binding.oneYearSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.green, null))
//                binding.oneMonthSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
//                binding.oneWeekSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
                binding.weekly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
                binding.monthly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
//                binding.lifeTimeCardView.background = context.getDrawable(R.color.white)
//                binding.lifeTimeCardView.background =
//                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
            }
            binding.weekly.setOnClickListener {
                click = "weekly"
                binding.weekly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.one_year_bg, null)
//                binding.oneWeekSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.green, null))
//                binding.oneMonthSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
//                binding.oneYearSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
                binding.yearly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
                binding.monthly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
                binding.lifeTimeCardView.background = context.getDrawable(R.color.white)
                binding.lifeTimeCardView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
            }
            binding.lifeTimeCardView.setOnClickListener {
                click = "lifeTime"
                binding.yearly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
//                binding.oneYearSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
//                binding.oneMonthSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
//                binding.oneWeekSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
                binding.weekly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
                binding.monthly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
                binding.lifeTimeCardView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.one_year_bg, null)
            }
            binding.monthly.setOnClickListener {
                click = "monthly"
                binding.monthly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.one_year_bg, null)
//                binding.oneMonthSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.green, null))
//                binding.oneYearSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
//                binding.oneWeekSaveBtn.backgroundTintList =
//                    ColorStateList.valueOf(context.resources.getColor(R.color.endColor, null))
                binding.weekly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
                binding.yearly.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)
                binding.lifeTimeCardView.background = context.getDrawable(R.color.white)
                binding.lifeTimeCardView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.unselected_bg, null)

            }
        }


        /**
         * returns callback when dialog is showed
         */
        fun showScreen(context: Context, closed: (Boolean) -> Unit) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val premiumBinding = LayoutPremiumBinding.inflate(inflater)
            val premiumBinding = LayoutPremiumNewBinding.inflate(inflater)

            val dialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
            dialog.setCancelable(true)
            dialog.setContentView(premiumBinding.root)


            premiumBinding.iLikeAd.setOnClickListener {
                dialog.dismiss()
            }
            premiumBinding.continueBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        }


        var LifeTimeSku: SkuDetails? = null
        var MonthlySku: SkuDetails? = null
        var YearlySku: SkuDetails? = null
        var WeeklySku: SkuDetails? = null

        const val LIFE_TIME_PRODUCT = "lifetime"
        const val MONTHLY_PRODUCT = "monthly_subscription"
        const val YEARLY_PRODUCT = "yearly_subscription"
        const val WEEKLY_PRODUCT = "weeklysub"


//        To handle these situations, be sure that your app calls BillingClient.queryPurchasesAsync() in your onResume() and onCreate() methods to
//        ensure that all purchases are successfully processed as described in processing

        lateinit var billingClient: BillingClient

        fun init(context: Context, initialized: (Boolean) -> Unit) {
            billingClient = BillingClient.newBuilder(context)
                .setListener { billingResult, purchases ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
                        for (purchase in purchases) {
                            runBlocking {
                                handlePurchase(context, purchase)
                            }
                        }
                    } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                        // Handle an error caused by a user cancelling the purchase flow.
//                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle any other error codes.
                        Log.i("MyError", "init: ${billingResult.responseCode}")
                    }
                }
                .enablePendingPurchases()
                .build()

            //initialize billing client
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    initialized.invoke(false)
                }

                override fun onBillingSetupFinished(result: BillingResult) {
                    initialized.invoke(true)
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        runBlocking { queryProductDetails() }
                    }
                }

            })
        }

        fun launchPurchaseFlow(activity: Activity, skuDetails: SkuDetails) {


            val flowParams = skuDetails.let {
                BillingFlowParams.newBuilder()
                    .setSkuDetails(it)
                    .build()
            }
            flowParams.let {
                billingClient.launchBillingFlow(
                    activity,
                    it
                ).responseCode
            }
        }


        suspend fun queryProductDetails() {

            val skuListLifeTime = ArrayList<String>()
            skuListLifeTime.add(LIFE_TIME_PRODUCT)
            val paramsLifeTime = SkuDetailsParams.newBuilder()
                .setSkusList(skuListLifeTime)
                .setType(BillingClient.SkuType.INAPP)

            val skuListSubs = ArrayList<String>()
            skuListSubs.add(MONTHLY_PRODUCT)
            skuListSubs.add(YEARLY_PRODUCT)
            skuListSubs.add(WEEKLY_PRODUCT)
            val paramsSubs = SkuDetailsParams.newBuilder()
                .setSkusList(skuListSubs)
                .setType(BillingClient.SkuType.SUBS)


            // leverage querySkuDetails Kotlin extension function
            withContext(Dispatchers.IO) {
                billingClient.querySkuDetailsAsync(
                    paramsLifeTime.build()
                ) { _, skuList ->
                    if (!skuList.isNullOrEmpty())
                        LifeTimeSku = skuList[0]
                    else
                        Log.i("MySku", "querySkuDetails: empty")
//                    for (sku in skuList!!) {
//
//                        Log.i("MySkus", "querySkuDetails: ${sku.sku}")
//                    }
                }

                billingClient.querySkuDetailsAsync(paramsSubs.build()) { _, skuList ->
                    skuList?.let {
                        for (sku in skuList) {
                            when (sku.sku) {
                                MONTHLY_PRODUCT -> MonthlySku = sku
                                YEARLY_PRODUCT -> YearlySku = sku
                                WEEKLY_PRODUCT -> WeeklySku = sku
                            }
                            Log.i("MySkus", "querySkuDetails: ${sku.sku}")
                        }
                    }
                }

            }

        }


        private fun querySubscriptions(adData: AdData) {
            billingClient.queryPurchasesAsync(
                BillingClient.SkuType.SUBS
            ) { result, records ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK && records.isNotEmpty()) {
                    var isPremium = false

                    for (record in records) {
                        Log.i("MyPurchases", "queryPurchases: ${record.originalJson}")
                        if (record.skus.contains(YEARLY_PRODUCT)) {
                            if (checkValidity(record.purchaseTime, 360)) {
                                if (::purchaseListener.isInitialized && !adData.inApp)
                                    purchaseListener.activatePremiumVersion()
                                isPremium = true
                                break
                            }
                        } else if (record.skus.contains(MONTHLY_PRODUCT)) {
                            if (checkValidity(record.purchaseTime, 30)) {
                                if (::purchaseListener.isInitialized && !adData.inApp)
                                    purchaseListener.activatePremiumVersion()
                                isPremium = true
                                break
                            }
                        } else if (record.skus.contains(WEEKLY_PRODUCT)) {
                            if (checkValidity(record.purchaseTime, 30)) {
                                if (::purchaseListener.isInitialized && !adData.inApp)
                                    purchaseListener.activatePremiumVersion()
                                isPremium = true
                                break
                            }
                        }

                    }
//                    <----Update your shared preference value here---->
//                    PowerPreference.getDefaultFile().putBoolean(is_Subscribed, isPremium)
                } else {
//                <----Update your shared preference value here---->
//                    PowerPreference.getDefaultFile().putBoolean(
//                        is_Subscribed,
//                        false
//                    )  //This may require second open to apply changes
                }


            }
        }

        fun queryPurchases(adData: AdData) {
            billingClient.queryPurchaseHistoryAsync(
                BillingClient.SkuType.INAPP
            ) { result, records ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK && !records.isNullOrEmpty() && records[0].skus.contains(
                        LIFE_TIME_PRODUCT
                    )
                ) {
                    if (::purchaseListener.isInitialized && !adData.inApp)
                        purchaseListener.activatePremiumVersion()
                } else
                    querySubscriptions(adData)

            }
        }


        private fun checkValidity(purchaseTime: Long, duration: Int): Boolean {

            val difference: Long = System.currentTimeMillis() - purchaseTime
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()
            val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
//            min =
//                (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) as Int / (1000 * 60)
//            hours = if (hours < 0) -hours else hours
//            Log.i("======= Hours", " :: $hours")
            Log.i("MyDays", "checkValidity: $hours")
            return days < duration
        }


        private suspend fun handlePurchase(context: Context, purchase: Purchase) {
            Log.i("MyPurchase", "handlePurchase:Order id ${purchase.orderId}")
            Log.i("MyPurchase", "handlePurchase:Package name ${purchase.packageName}")

            if ((purchase.skus.contains(LIFE_TIME_PRODUCT.lowercase()) || purchase.skus.contains(
                    YEARLY_PRODUCT.lowercase()
                ) || purchase.skus.contains(WEEKLY_PRODUCT.lowercase()) || purchase.skus.contains(
                    MONTHLY_PRODUCT.lowercase()
                ))
                && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            ) {
                //<-------update your boolean value stored in shared preferences here--------->
//                PreferenceHelper.putBoolean(Constants.INAPP, true)
                if (!purchase.isAcknowledged) {

                    Log.i("MyKey", "handlePurchase: Acknowleding purchase")
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                    withContext(Dispatchers.IO) {
                        billingClient.acknowledgePurchase(
                            acknowledgePurchaseParams.build()
                        ) { result ->
                            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                Log.i("MyKey", "handlePurchase: Dialog shown")

                                updatePurchaseStatus(context, true)

                                if (::purchaseListener.isInitialized)
                                    purchaseListener.activatePremiumVersion()
                            }
                        }
                    }


                } else {
                    Log.i("MyKey", "handlePurchase: Purchase is already acknowledged")
                    if (::purchaseListener.isInitialized)
                        purchaseListener.activatePremiumVersion()
                }
            } else if ((purchase.skus.contains(YEARLY_PRODUCT.lowercase()) || purchase.skus.contains(
                    WEEKLY_PRODUCT.lowercase()
                ) || purchase.skus.contains(
                    MONTHLY_PRODUCT.lowercase()
                ))
                && purchase.purchaseState == Purchase.PurchaseState.PENDING
            ) {
                Log.i("MyKey", "handlePurchase: Pending")

            } else {
                Log.i("MyKey", "handlePurchase: No purchase yet")

            }


            // Verify the purchase.
            // Ensure entitlement was not already granted for this purchaseToken.
            // Grant entitlement to the user.

            val consumeParams =
                ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
            val consumeResult = withContext(Dispatchers.IO) {
                billingClient.consumeAsync(
                    consumeParams
                ) { p0, p1 -> }
            }
        }

        fun updatePurchaseStatus(context: Context, purchased: Boolean) {
            val sharedPreferences =
                context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("IsPurchased", purchased)
            editor.apply()
        }


        //        <--- Wrap calling of this method around below line of code -->
//        if (activity.window.decorView.rootView.isShown)
        fun showProgressDialog(context: Context, intent: Intent) {
            //<-------update your boolean value stored in shared preferences here--------->
//            PreferenceHelper.putBoolean(Constants.INAPP, true)
            Log.i("MyPurchase", "showProgressDialog: in progress dialog")

            Handler(Looper.getMainLooper()).post {
                val myView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_restart_dialog, null)
                val alertDialog = AlertDialog.Builder(context)
                    .setView(myView)
                    .setCancelable(false)
                    .create()


                myView.findViewById<Button>(R.id.btn_ok)
                    .setOnClickListener {
                        alertDialog.dismiss()
                        AdsManager.clearInstances()
                        (context as Activity).finishAffinity()
                        (context as Activity).startActivity(intent)
                    }

                alertDialog.show()
                alertDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
                Log.i("MyPurchase", "querySkuDetails: showed")
            }

        }

//        private fun customTextView(context: Context, view: TextView , click :(Boolean)->Unit) {
//
//            val spanTxt = SpannableStringBuilder(
////                "By Using Our App, You Agree to Our "
//                "Cancel Anytime For Any Reason\n"
//            )
//            spanTxt.append(Html.fromHtml(" <b>Terms of Condition</b>"))
//            spanTxt.setSpan(object : ClickableSpan() {
//                override fun onClick(widget: View) {
//                    showPrivacyDialog(
//                        context,
//                        "https://barakatappssole.wordpress.com/2022/11/03/terms-conditions/",
//                        "Terms of Condition"
//                    )
//                    click.invoke(true)
//                }
//            }, spanTxt.length - "Terms of Condition".length, spanTxt.length, 0)
//            spanTxt.append("&")
//            spanTxt.setSpan(ForegroundColorSpan(Color.BLACK), spanTxt.length, spanTxt.length, 0)
//            spanTxt.append(Html.fromHtml("<b>Privacy Policy.</b>"))
//            spanTxt.setSpan(object : ClickableSpan() {
//                override fun onClick(widget: View) {
//                    showPrivacyDialog(
//                        context,
//                        "https://barakatappssole.wordpress.com/blog/",
//                        "Privacy Policy"
//                    )
//                    click.invoke(true)
//                }
//            }, spanTxt.length - " Privacy Policy.".length, spanTxt.length, 0)
//            view.movementMethod = LinkMovementMethod.getInstance()
//            view.setText(spanTxt, TextView.BufferType.SPANNABLE)
//        }


        private fun customTextView(context: Context, view: TextView, click: (Boolean) -> Unit) {
            val spanTxt = SpannableStringBuilder("")

            // Add spaces to separate "Terms of Condition" and "Privacy Policy"
            val space1 = SpannableString("    ")
            spanTxt.append(space1)

            spanTxt.append(Html.fromHtml("<b>Terms of Condition</b>"))
            spanTxt.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    showPrivacyDialog(
                        context,
                        "https://barakatappssole.wordpress.com/2022/11/03/terms-conditions/",
                        "Terms of Condition"
                    )
                    click.invoke(true)
                }
            }, spanTxt.length - "Terms of Condition".length, spanTxt.length, 0)

            // Add more spaces to create a gap between "Terms of Condition" and "Privacy Policy"
            val space2 = SpannableString("\t\t\t\t\t\t\t\t")
            spanTxt.append(space2)

            spanTxt.append(Html.fromHtml("<b>Privacy Policy</b>"))
            spanTxt.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    showPrivacyDialog(
                        context,
                        "https://barakatappssole.wordpress.com/blog/",
                        "Privacy Policy"
                    )
                    click.invoke(true)
                }
            }, spanTxt.length - "Privacy Policy".length, spanTxt.length, 0)

            view.movementMethod = LinkMovementMethod.getInstance()
            view.setText(spanTxt, TextView.BufferType.SPANNABLE)
            view.setTextColor(Color.BLACK)
        }


        fun showPrivacyDialog(context: Context, myUrl: String, heading: String) {

            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bind = PrivacyPolycyBinding.inflate(inflater)
            val dialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(bind.root)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            bind.mainHeading.text = heading
            bind.pro.setOnClickListener {
                showPremium(context) {}
                dialog.dismiss()
            }
            bind.webviewPrivacy.loadUrl(myUrl)

            bind.webviewPrivacy.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    bind.webviewPrivacy.visibility = View.VISIBLE
                    bind.progressWebview.visibility = View.GONE
                }
            }

            dialog.setOnDismissListener {
                AdsManager.isInterShowing = false
            }

            dialog.setOnShowListener {
                AdsManager.isInterShowing = true


            }
            bind.back.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }

            dialog.show()
        }
    }

}