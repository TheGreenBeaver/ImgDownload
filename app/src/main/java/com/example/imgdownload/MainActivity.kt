package com.example.imgdownload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var bitmap: Bitmap? = null
    private val imgURL = "https://im0-tub-ru.yandex.net/i?id=73b3f62833333e8a1817a3106ac1ef31&n=13"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadImg = findViewById<ImageView>(R.id.load_img)
        val spinner = findViewById<ProgressBar>(R.id.progress_bar)
        val loadBtn = findViewById<Button>(R.id.load_btn)

        loadBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                loadBtn.visibility = View.INVISIBLE
                spinner.visibility = View.VISIBLE
                bitmap = withContext(Dispatchers.IO) {
                    BitmapFactory.decodeStream(URL(imgURL).openStream())
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