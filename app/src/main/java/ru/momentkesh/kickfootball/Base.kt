package com.huda.kickfoot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.momentkesh.kickfootball.R
import java.util.Random


class Base(val context: Context) {
    private var medicine:Bitmap = resizeBitmap(BitmapFactory.decodeResource(context.resources,
        R.drawable.plane
    ),250,250)
    private var random: Random = Random()
    var mx=0
    var my=0
    var isAlive:Boolean=true
    var medicineVelocity=0

    init {
        resetMedicine()
    }
    fun getMedicineBitmap():Bitmap{
        return medicine
    }
    fun getMedicineWidth():Int{
        return medicine.width
    }

    fun getMedicineHeight():Int{
        return medicine.height
    }
    private fun resetMedicine() {
        OpponentKiller.setScreenSize(context)
        mx = 200+random.nextInt(OpponentKiller.screenWidth)
        my= OpponentKiller.screenHeight -medicine.height
        medicineVelocity = 10+random.nextInt(6)
        
    }
    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}