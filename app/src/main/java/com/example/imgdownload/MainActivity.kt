package com.example.imgdownload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var bitmap: Bitmap? = null
    private lateinit var loadImgJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadImg = findViewById<ImageView>(R.id.load_img)
        val spinner = findViewById<ProgressBar>(R.id.progress_bar)
        val loadBtn = findViewById<Button>(R.id.load_btn)
        val urlInput = findViewById<EditText>(R.id.url_input)

        loadBtn.setOnClickListener {
            val loadUrl = urlInput.text.toString()
            if (URLUtil.isValidUrl(loadUrl)) {
                loadImgJob = lifecycle.coroutineScope.launch(Dispatchers.Main) {
                    loadBtn.visibility = View.INVISIBLE
                    spinner.visibility = View.VISIBLE
                    bitmap = withContext(Dispatchers.IO) {
                        BitmapFactory.decodeStream(URL(loadUrl).openStream())
                    }
                    bitmap?.run {
                        spinner.visibility = View.GONE
                        loadBtn.visibility = View.VISIBLE
                        loadImg.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        loadImgJob.cancel()
    }
}