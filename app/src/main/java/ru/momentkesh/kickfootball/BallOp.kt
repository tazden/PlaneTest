package ru.momentkesh.kickfootball

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class BallOp(private val context: Context, var shx:Int, var shy:Int) {
    private var shot: Bitmap = resizeBitmap(
        BitmapFactory.decodeResource(context.resources,
            R.drawable.bullet_enemy
        ),50,50)

    fun getKick(): Bitmap {
        return shot
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}