package com.example.imgdownload

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {

    private lateinit var loadImg: ImageView
    private lateinit var loadBtn: Button
    private lateinit var spinner: ProgressBar
    private lateinit var urlInput: EditText
    private lateinit var imageReceiver: ImageReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadImg = findViewById(R.id.load_img)
        loadBtn = findViewById(R.id.load_btn)
        spinner = findViewById(R.id.progress_bar)
        urlInput = findViewById(R.id.url_input)

        imageReceiver = ImageReceiver()
        val filter = IntentFilter(Constants.LOADING_SUCCESS_ACTION.name)
        registerReceiver(imageReceiver, filter)

        loadBtn.setOnClickListener {
            loadBtn.visibility = View.INVISIBLE
            urlInput.visibility = View.INVISIBLE
            spinner.visibility = View.VISIBLE
            val loadUrl = urlInput.text.toString()
            if (URLUtil.isValidUrl(loadUrl)) {
                Intent(this, LoaderService::class.java).also { intent ->
                    intent.putExtra(Constants.URL_TO_LOAD_KEY.name, loadUrl)
                    startService(intent)
                }
            }
        }
    }

    override fun onStop() {
        stopService(Intent(this, LoaderService::class.java))
        unregisterReceiver(imageReceiver)
        super.onStop()
    }

    inner class ImageReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val pathToFile = intent?.getStringExtra(Constants.RES_BITMAP_KEY.name)
            if (pathToFile != null) {
                loadBtn.visibility = View.VISIBLE
                urlInput.visibility = View.VISIBLE
                spinner.visibility = View.GONE
                loadImg.setImageURI(Uri.parse(pathToFile))
            }
        }
    }
}