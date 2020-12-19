package com.example.imgdownload

import android.content.Context
import android.content.Intent
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar


class MainActivity : AppCompatActivity() {

    private lateinit var startDownloadBtn: Button
    private lateinit var spinner: ProgressBar
    private lateinit var urlInput: EditText
    private lateinit var pathOutput: TextView

    private lateinit var imageReceiver: ImageReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startDownloadBtn = findViewById(R.id.load_btn)
        urlInput = findViewById(R.id.url_input)
        pathOutput = findViewById(R.id.path_output)
        spinner = findViewById(R.id.progress_bar)

        imageReceiver = ImageReceiver()
        val filter = IntentFilter(Constants.LOADING_SUCCESS_ACTION.name)
        registerReceiver(imageReceiver, filter)

        startDownloadBtn.setOnClickListener {
            val loadUrl = urlInput.text.toString()
            if (URLUtil.isValidUrl(loadUrl)) {
                startDownloadBtn.visibility = View.INVISIBLE
                urlInput.visibility = View.INVISIBLE
                spinner.visibility = View.VISIBLE
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
                startDownloadBtn.visibility = View.VISIBLE
                urlInput.visibility = View.VISIBLE
                spinner.visibility = View.GONE
                pathOutput.text = pathToFile
            }
        }
    }
}