package com.example.imgdownload

import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.IBinder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class LoaderService : Service() {

    private var loadingThread: Thread? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val loadUrl = intent.getStringExtra(Constants.URL_TO_LOAD_KEY.name)

        loadingThread = Thread {
            if (!Thread.interrupted()) {
                try {
                    val inpStream = java.net.URL(loadUrl).openStream()
                    val resBitmap = BitmapFactory.decodeStream(inpStream)

                    val cw = ContextWrapper(applicationContext)
                    val dir = cw.getDir("img_download", Context.MODE_PRIVATE)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val pathToFile = "${loadUrl?.replace(Regex("[+=;@#:^&*./%)(><?!-]+"), "_")}.jpg"
                    val fileToSave = File(dir, pathToFile)

                    val outputStream: FileOutputStream?
                    try {
                        outputStream = FileOutputStream(fileToSave, false)
                        resBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.flush()
                        outputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }


                    Intent().also { intent ->
                        intent.putExtra(Constants.RES_BITMAP_KEY.name, Uri.fromFile(fileToSave).toString())
                        intent.action = Constants.LOADING_SUCCESS_ACTION.name
                        sendBroadcast(intent)
                        stopSelf()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@Thread
                }
            }
        }
        loadingThread!!.start()

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        loadingThread?.interrupt()
        super.onDestroy()
    }
}