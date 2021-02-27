package www.digitalexperts.church_tracker.fragments

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import www.digitalexperts.church_tracker.Utils.visible
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentWvinfoBinding


class wvinfo : Fragment(R.layout.fragment_wvinfo) {
    private  var _binding : FragmentWvinfoBinding?=null
    private val binding get() = _binding!!
    private val args:wvinfoArgs by navArgs()
    private lateinit var adView: AdView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentWvinfoBinding.bind(view)

        if (binding.wvvs!=null) {
            val url=if(arguments?.getString("web")==null){
                args.vlinks
            }else{
                arguments?.getString("web")
            }
            binding.wvvs.settings.javaScriptEnabled = true
            binding.wvvs.webViewClient = WebViewClient()
            binding.wvvs.webChromeClient = WebChromeClient()
            if (url != null) {
                binding.wvvs.loadUrl(url)
            }

            binding.wvvs.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    try {
                        binding.pgbar.visible(true)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }

                    super.onPageStarted(view, url, favicon)

                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    try {
                        binding.pgbar.visible(false)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }

                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    try {
                        binding.pgbar.visible(false)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                    val myerrorpage = "file:///android_asset/android/errorpage.html";
                    binding.wvvs.loadUrl(myerrorpage)
                    /*super.onReceivedError(view, errorCode, description, failingUrl)*/

                }
            }
            binding.wvvs.setDownloadListener(object : DownloadListener {
                override fun onDownloadStart(
                    url: String, userAgent: String,
                    contentDisposition: String, mimetype: String,
                    contentLength: Long
                ) {

                    //getting file name from url
                    val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
                    //DownloadManager.Request created with url.
                    val request = DownloadManager.Request(Uri.parse(url))
                    //cookie
                    val cookie = CookieManager.getInstance().getCookie(url)
                    //Add cookie and User-Agent to request
                    request.addRequestHeader("Cookie", cookie)
                    request.addRequestHeader("User-Agent", userAgent)
                    //file scanned by MediaScannar
                    request.allowScanningByMediaScanner()
                    request.setDescription("Download file...")
                    //Download is visible and its progress, after completion too.
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    //DownloadManager created
                    val downloadmanager =
                        activity?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    //Saving file in Download folder
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        filename
                    )
                    //download enqued
                    downloadmanager.enqueue(request)

                    Toast.makeText(context, "Downloading file", Toast.LENGTH_SHORT).show()
                }

            })

            binding.wvvs.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && binding.wvvs.canGoBack()) {
                    binding.wvvs.goBack()
                    return@OnKeyListener true
                }
                false
            })
        }
        adView = AdView(context)
        binding.bannerContainertwo.addView(adView)
        adView.adUnitId = "ca-app-pub-4814079884774543/6358507489"

        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()
        // Start loading the ad in the background.
        adView.loadAd(adRequest)

       /* adView = AdView(context, "376366029998847_376375873331196", AdSize.BANNER_HEIGHT_50)
        binding.bannerContainertwo.addView(adView)
        adView!!.loadAd()*/
    }

    private val adSize: AdSize
        get() {
            val display =activity?.windowManager!!.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.bannerContainertwo.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}