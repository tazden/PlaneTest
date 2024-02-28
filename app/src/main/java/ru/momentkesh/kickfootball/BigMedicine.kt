package ru.momentkesh.kickfootball

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

class BigMedicine(context: Context, var x: Float, var y: Float) {
    private var medicineBitmap: Bitmap
    val width: Int
    val height: Int

    init {
        medicineBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.first_aid_kit_big)
        width = medicineBitmap.width
        height = medicineBitmap.height
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(medicineBitmap, x, y, null)
    }
}
