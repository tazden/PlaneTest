package com.huda.kickfoot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.momentkesh.kickfootball.R


class Ball(private val context: Context, var shx:Int, var shy:Int) {
    private var shot:Bitmap = resizeBitmap(BitmapFactory.decodeResource(context.resources,
        R.drawable.bullet
    ),50,50)

    fun getKick():Bitmap{
        return shot
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}