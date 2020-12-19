package com.example.imgdownload

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView


class ImageReceiver(
    private val pathDisplay: TextView,
    private val spinner: ProgressBar,
    private val urlInput: EditText,
    private val startDownloadButton: Button
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Constants.LOADING_SUCCESS_ACTION.name == intent.action) {
            val pathToFile = intent.getStringExtra(Constants.RES_BITMAP_KEY.name)
            pathDisplay.text = pathToFile

            spinner.visibility = View.GONE
            urlInput.visibility = View.VISIBLE
            startDownloadButton.visibility = View.VISIBLE
        }
    }
}