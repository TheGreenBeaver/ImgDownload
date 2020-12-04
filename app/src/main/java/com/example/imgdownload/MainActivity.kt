package com.example.imgdownload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var loadImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadImg = findViewById(R.id.load_img)
        val loadBtn = findViewById<Button>(R.id.load_btn)

        loadBtn.setOnClickListener {
            val task = ImgLoadTask()
            task.execute("https://im0-tub-ru.yandex.net/i?id=73b3f62833333e8a1817a3106ac1ef31&n=13")
        }
    }

    inner class ImgLoadTask: AsyncTask<String, Void, Bitmap>() {
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
        }
    }
}