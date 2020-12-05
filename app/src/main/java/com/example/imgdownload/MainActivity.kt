package com.example.imgdownload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val imgURL = "https://im0-tub-ru.yandex.net/i?id=73b3f62833333e8a1817a3106ac1ef31&n=13"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadImg = findViewById<ImageView>(R.id.load_img)
        val loadBtn = findViewById<Button>(R.id.load_btn)

        loadBtn.setOnClickListener {
            Picasso.get().load(imgURL).into(loadImg)
        }
    }
}