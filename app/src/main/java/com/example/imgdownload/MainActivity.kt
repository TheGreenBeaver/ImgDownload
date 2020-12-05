package com.example.imgdownload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var loadImg: ImageView
    private lateinit var loadBtn: Button
    private lateinit var spinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadImg = findViewById(R.id.load_img)
        loadBtn = findViewById(R.id.load_btn)
        spinner = findViewById(R.id.progress_bar)

        loadBtn.setOnClickListener {
            val task = ImgLoadTask()
            task.execute("https://im0-tub-ru.yandex.net/i?id=73b3f62833333e8a1817a3106ac1ef31&n=13")
        }
    }

    inner class ImgLoadTask: AsyncTask<String, Void, Bitmap>() {
        override fun onPreExecute() {
            super.onPreExecute()
            loadBtn.visibility = View.INVISIBLE
            spinner.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): Bitmap? {
            val url = params[0]!!
            val resBitmap: Bitmap?
            try {
                val inpStream = java.net.URL(url).openStream()
                resBitmap = BitmapFactory.decodeStream(inpStream)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

            return resBitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                loadImg.setImageBitmap(result)
            }
            loadBtn.visibility = View.VISIBLE
            spinner.visibility = View.GONE
        }
    }
}