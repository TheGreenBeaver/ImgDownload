package com.example.imgdownload

import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class LoaderService : Service() {

    private var loadingThread: Thread? = null
    private lateinit var messenger: Messenger

    internal class MessageHandler(private val context: Context) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.DOWNLOAD_REQUEST.ordinal ->
                    (context as LoaderService).downloadImage(msg.obj.toString(), msg.replyTo)
                else ->
                    super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        messenger = Messenger(MessageHandler(this))
        return messenger.binder
    }

    private fun downloadImage(loadUrl: String?, replyTo: Messenger?) {
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
                    Log.i("qwer", "Done downloading")
                    if (replyTo == null) {
                        Log.i("qwer", "Sending a broadcast 'cos service is started")
                        Intent().also { intent ->
                            intent.putExtra(
                                Constants.RES_BITMAP_KEY.name,
                                Uri.fromFile(fileToSave).toString()
                            )
                            intent.action = Constants.LOADING_SUCCESS_ACTION.name
                            sendBroadcast(intent)
                            stopSelf()
                        }
                    } else {
                        val response = Message.obtain(null, Constants.DOWNLOAD_RESPONSE.ordinal, Uri.fromFile(fileToSave).toString())
                        response.replyTo = messenger

                        try {
                            replyTo.send(response)
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@Thread
                }
            }
        }
        loadingThread!!.start()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("qwer", "Started")
        val loadUrl = intent.getStringExtra(Constants.URL_TO_LOAD_KEY.name)
        Log.i("qwer", "Extra data is $loadUrl")
        this.downloadImage(loadUrl, null);
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        loadingThread?.interrupt()
        super.onDestroy()
    }
}