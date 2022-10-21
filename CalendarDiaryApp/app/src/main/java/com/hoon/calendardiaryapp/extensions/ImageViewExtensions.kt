package com.hoon.calendardiaryapp.extensions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


fun ImageView.setImageWithGlide(
    context: Context,
    imageUri: Uri,
) {

    Glide
        .with(context)
        .asBitmap()
        .load(imageUri)
        .into(this)
}

fun ImageView.setImageWithGlide(
    context: Context,
    imageUri: Uri,
    onResourceReady: () -> Unit,
) {

    Glide
        .with(context)
        .asBitmap()
        .load(imageUri)
        .listener(glideResultListener(onResourceReady))
        .into(this)
}

private fun glideResultListener(
    onResourceReady: () -> Unit,
): RequestListener<Bitmap> {
    return object : RequestListener<Bitmap> {

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onResourceReady()
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }
    }
}