package com.huda.kickfoot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.momentkesh.kickfootball.R


class Reaction(private val context: Context, var ex:Int, var ey:Int) {
    private var explosion=arrayOfNulls<Bitmap>(4)
    var frame:Int=0
    init {
        explosion[0] = scaleBitmap(R.drawable.ex)
        explosion[1] = scaleBitmap(R.drawable.ex)
        explosion[2] = scaleBitmap(R.drawable.ex)
        explosion[3] = scaleBitmap(R.drawable.ex)

    }

    private fun scaleBitmap(resourceId: Int, scaleFactor: Float = 0.10f): Bitmap {
        val originalBitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        val width = (originalBitmap.width * scaleFactor).toInt()
        val height = (originalBitmap.height * scaleFactor).toInt()
        return Bitmap.createScaledBitmap(originalBitmap, width, height, true)
    }

    fun getExplosionFrame(frame:Int):Bitmap{
        return explosion[frame]!!
    }

}