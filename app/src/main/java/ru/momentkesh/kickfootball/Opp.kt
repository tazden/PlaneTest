package com.huda.kickfoot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.momentkesh.kickfootball.R
import java.util.Random

class Opponent(private val context: Context) {
    private var Opponent: Bitmap = resizeBitmap(BitmapFactory.decodeResource(context.resources,
        R.drawable.plane_enemy
    ), 250, 250)
    private var random: Random = Random()

    var vx = 300
    var vy = 400
    var OpponentVelocity = 0

    init {
        resetOpponent()
    }

    fun getOpponentBitmap(): Bitmap {
        return Opponent
    }

    fun getOpponentWidth(): Int {
        return Opponent.width
    }

    fun getOpponentHeight(): Int {
        return Opponent.height
    }

    private fun resetOpponent() {
        vx = 200 + random.nextInt(400)
        vy = 0
        OpponentVelocity = 14 + random.nextInt(10)
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}
