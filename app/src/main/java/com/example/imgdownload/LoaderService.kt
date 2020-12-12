package com.example.imgdownload

import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class LoaderService : Service() {

//    private var loadingThread: Thread? = null
    private lateinit var loaderMessenger: Messenger

    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                17 ->
                    Toast.makeText(applicationContext, "hello! received ${msg.obj}", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
        loaderMessenger = Messenger(IncomingHandler(this))
        return loaderMessenger.binder
    }






//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//
//        val loadUrl = intent.getStringExtra(Constants.URL_TO_LOAD_KEY.name)
//
//        loadingThread = Thread {
//            if (!Thread.interrupted()) {
//                try {
//                    val inpStream = java.net.URL(loadUrl).openStream()
//                    val resBitmap = BitmapFactory.decodeStream(inpStream)
//
//                    val cw = ContextWrapper(applicationContext)
//                    val dir = cw.getDir("img_download", Context.MODE_PRIVATE)
//                    if (!dir.exists()) {
//                        dir.mkdirs()
//                    }
//                    val pathToFile = "${loadUrl?.replace(Regex("[+=;@#:^&*./%)(><?!-]+"), "_")}.jpg"
//                    val fileToSave = File(dir, pathToFile)
//
//                    val outputStream: FileOutputStream?
//                    try {
//                        outputStream = FileOutputStream(fileToSave, false)
//                        resBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                        outputStream.flush()
//                        outputStream.close()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//
//
//                    val resIntent = Intent(Constants.LOADING_SUCCESS_ACTION.name)
//                    resIntent.putExtra(Constants.RES_BITMAP_KEY.name, Uri.fromFile(fileToSave).toString())
//                    sendBroadcast(resIntent)
//                    stopSelf()
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    return@Thread
//                }
//            }
//        }
//        loadingThread!!.start()
//
//        return START_REDELIVER_INTENT
//    }
//
//    override fun onDestroy() {
//        loadingThread?.interrupt()
//        super.onDestroy()
//    }
}