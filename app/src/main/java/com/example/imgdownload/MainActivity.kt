package com.example.imgdownload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var loadImg: ImageView
    private lateinit var loadBtn: Button
    private lateinit var spinner: ProgressBar
    private lateinit var urlInput: EditText

    private lateinit var loadTask: ImgLoadTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadImg = findViewById(R.id.load_img)
        loadBtn = findViewById(R.id.load_btn)
        spinner = findViewById(R.id.progress_bar)
        urlInput = findViewById(R.id.url_input)

        loadBtn.setOnClickListener {
            val loadUrl = urlInput.text.toString()
            if (URLUtil.isValidUrl(loadUrl)) {
                loadTask = ImgLoadTask()
                loadTask.execute(loadUrl)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        loadTask.cancel(true)
    }

    inner class ImgLoadTask: AsyncTask<String, Void, Bitmap>() {
        override fun onPreExecute() {
            super.onPreExecute()
            loadBtn.visibility = View.INVISIBLE
            spinner.visibility = View.VISIBLE
            urlInput.visibility = View.INVISIBLE
        }

        override fun doInBackground(vararg params: String?): Bitmap? {
            val url = params[0]!!
            var resBitmap: Bitmap? = null
            if (!isCancelled) {
                try {
                    val inpStream = java.net.URL(url).openStream()
                    resBitmap = BitmapFactory.decodeStream(inpStream)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }
            }

            return resBitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                loadImg.setImageBitmap(result)
            }
            loadBtn.visibility = View.VISIBLE
            spinner.visibility = View.GONE
            urlInput.visibility = View.VISIBLE
        }
    }
}