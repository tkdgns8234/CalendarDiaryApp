package com.hoon.calendardiaryapp.extensions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * glide 로 imageView 이미지 세팅
 */

fun ImageView.setImageWithGlide(
    context: Context,
    imageUri: Uri,
) {
    Glide
        .with(context)
        .asBitmap()
        .load(imageUri)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

/**
 * @param onResourceReady : 이미지 load 완료 시 호출됨
 */
fun ImageView.setImageWithGlide(
    context: Context,
    imageUri: Uri,
    onResourceReady: () -> Unit,
) {
    Glide
        .with(context)
        .asBitmap()
        .load(imageUri)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
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