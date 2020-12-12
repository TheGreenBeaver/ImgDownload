package com.example.imgdownload


import android.content.Intent
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var urlInput: EditText
    private lateinit var loadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlInput = findViewById(R.id.url_input)
        loadButton = findViewById(R.id.load_btn)

        loadButton.setOnClickListener {
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
        super.onStop()
    }
}