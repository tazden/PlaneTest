package com.huda.kickfoot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import ru.momentkesh.kickfootball.BallOp
import ru.momentkesh.kickfootball.BigMedicine
import ru.momentkesh.kickfootball.Medicine
import ru.momentkesh.kickfootball.R
import java.util.Random

class OpponentKiller(private val context: Context) : View(context) {
    private var background: Bitmap
    private var lifeImage: Bitmap
    private var image1: Bitmap
    private var image2: Bitmap
    private var medicineTimer: Handler? = null
    private var button: Bitmap
    private var medicine: Medicine? = null
    private var bigMedicine: BigMedicine? = null
    private var handler: Handler? = Handler()
    private var points = 0
    private var paused = false
    private var life = 5
    private var scorePaint = Paint()

    private var Opponent = Opponent(context)
    private var base = Base(context)

    private var random: Random = Random()
    private var highScore: Int

    private var opponentBalls: ArrayList<BallOp> = arrayListOf()
    private var BaseBalls: ArrayList<Ball> = arrayListOf()
    private val bundle = Bundle()
    private var OpponentExplosion = false
    private lateinit var reaction: Reaction
    private var reactions: ArrayList<Reaction> = arrayListOf()
    private var OpponentShotAction = false
    private val runnable = Runnable {
        invalidate()
    }

    init {
        setScreenSize(context)
        background = BitmapFactory.decodeResource(context.resources, R.drawable.background04)
        val originalLifeImage = BitmapFactory.decodeResource(context.resources, R.drawable.life)
        val scaledWidth = originalLifeImage.width / 2 // Уменьшаем в два раза
        val scaledHeight = originalLifeImage.height / 2 // Уменьшаем в два раза
        lifeImage = Bitmap.createScaledBitmap(originalLifeImage, scaledWidth, scaledHeight, false)
        button = BitmapFactory.decodeResource(context.resources, R.drawable.menu)
        image1 = BitmapFactory.decodeResource(context.resources, R.drawable.text_score01)
        image2 = BitmapFactory.decodeResource(context.resources, R.drawable.score)
        highScore = loadHighScore()



    }
    private fun loadHighScore(): Int {
        val sharedPreferences = context.getSharedPreferences("HighScore", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("highScore", 0)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(background, null, Rect(0, 0, screenWidth, screenHeight), null)
        canvas.drawBitmap(button, 30f, 25f, null)
        val image1X = (screenWidth - image1.width) / 2f
        val image1Y = 50f
        val image2X = (screenWidth - image2.width) / 2f
        val image2Y = image1Y + image1.height + 20f
        canvas.drawBitmap(image1, image1X, image1Y, null)
        val image2Width = image2.width.toFloat()
        val image2Height = image2.height.toFloat()

        val text = "$points"

// Получаем ширину текста
        val textWidth = scorePaint.measureText(text)

// Вычисляем координаты для центрирования текста внутри ImageView2
        val textX = image2X + (image2Width - textWidth) / 2
        val textY = image2Y + image2Height / 2 + SIZE / 2  // Регулируйте SIZE для вертикального выравнивания
        val scorePaint = Paint().apply {
            color = Color.WHITE
            textSize = SIZE
            textAlign = Paint.Align.CENTER
            typeface = ResourcesCompat.getFont(context, R.font.rockwell_extra_bold) // R.font.your_font - замените на реальный идентификатор вашего шрифта
        }

        canvas.drawBitmap(image2, image2X, image2Y, null)
        canvas.drawText(text, textX, textY, scorePaint)

        for (i in life downTo 1) {
            canvas.drawBitmap(lifeImage, screenWidth.toFloat()-100 - (lifeImage.width * i), 55f, null)
        }
        if (random.nextInt(100) < 2) { // Измените 2 на любое другое значение для регулировки частоты появления
            startBigMedicineTimer()
        }
        bigMedicine?.draw(canvas)

        medicine?.draw(canvas)
        if (random.nextInt(100) < 5) { // Измените 2 на любое другое значение для регулировки частоты появления
            startMedicineTimer()
        }
        medicine?.draw(canvas)

        if (medicine != null && base.mx < medicine!!.x + medicine!!.width && base.mx + base.getMedicineWidth() > medicine!!.x
            && base.my - 300 < medicine!!.y + medicine!!.height && base.my + base.getMedicineHeight() - 300 > medicine!!.y) {
            points++
            // Дополнительные действия, если необходимо, например, удаление лекарства
            medicine = null
        }

        if (life == 0) {
            paused = true
            handler = null
            bundle.putBoolean("paused", true)
            bundle.putInt("points", points)
            bundle.putInt("high", highScore)
            checkAndSaveHighScore()
            if (highScore<points){
                findNavController().navigate(R.id.record, bundle)
            }else {
                findNavController().navigate(R.id.game_over, bundle)
            }
        }
        if (points>=10){

        }
        Opponent.vx += Opponent.OpponentVelocity
        if (Opponent.vx + Opponent.getOpponentWidth() >= screenWidth || Opponent.vx <= 0) {
            Opponent.OpponentVelocity *= -1
        }
        if ((!OpponentShotAction) && (Opponent.vx >= 200 + random.nextInt(400))) {
            val OpponentBall = BallOp(context, Opponent.vx + (Opponent.getOpponentWidth() / 2), Opponent.vy+300)
            opponentBalls.add(OpponentBall)
            OpponentShotAction = true
        }
        if (!OpponentExplosion) {
            canvas.drawBitmap(Opponent.getOpponentBitmap(), Opponent.vx.toFloat(), (Opponent.vy+300).toFloat(), null)
        }
        if (base.isAlive) {
            if (base.mx > screenWidth - base.getMedicineWidth()) {
                base.mx = screenWidth - base.getMedicineWidth()
            } else if (base.mx < 0) {
                base.mx = 0
            }
            canvas.drawBitmap(
                base.getMedicineBitmap(),
                base.mx.toFloat(),
                (base.my-300).toFloat(),
                null
            )
        }
        for (i in 0 until opponentBalls.size) {
            opponentBalls[i].shy += 15
            canvas.drawBitmap(
                opponentBalls[i].getKick(),
                opponentBalls[i].shx.toFloat(),
                opponentBalls[i].shy.toFloat(),
                null
            )
            if (opponentBalls[i].shx >= base.mx && opponentBalls[i].shx <= base.mx + base.getMedicineWidth()
                && opponentBalls[i].shy >= base.my-300 && opponentBalls[i].shy <= base.my+300 + base.getMedicineHeight()
            ) {
                life--
                opponentBalls.removeAt(i)
                reaction = Reaction(context, base.mx, base.my-300)
                reactions.add(reaction)
            } else if (opponentBalls[i].shy >= screenHeight) {
                opponentBalls.removeAt(i)
            }
            if (opponentBalls.size == 0) {
                OpponentShotAction = false
            }
        }
        var i = 0
        while (BaseBalls.size > 0 && i < BaseBalls.size) {
            BaseBalls[i].shy -= 15
            canvas.drawBitmap(
                BaseBalls[i].getKick(),
                BaseBalls[i].shx.toFloat(),
                BaseBalls[i].shy.toFloat(),
                null
            )
            if (medicine != null &&
                BaseBalls[i].shx >= (medicine?.x ?: 0).toFloat() &&
                BaseBalls[i].shx <= (medicine?.x?.plus(medicine?.width ?: 0) ?: 0).toFloat() &&
                BaseBalls[i].shy >= (medicine?.y ?: 0).toFloat() &&
                BaseBalls[i].shy <= (medicine?.y?.plus(medicine?.height ?: 0) ?: 0).toFloat()
            ) {
                if (life < 5) {
                    life++
                }
                medicine = null
            }
            if (bigMedicine != null &&
                BaseBalls[i].shx >= (bigMedicine?.x ?: 0).toFloat() &&
                BaseBalls[i].shx <= (bigMedicine?.x?.plus(bigMedicine?.width ?: 0) ?: 0).toFloat() &&
                BaseBalls[i].shy >= (bigMedicine?.y ?: 0).toFloat() &&
                BaseBalls[i].shy <= (bigMedicine?.y?.plus(bigMedicine?.height ?: 0) ?: 0).toFloat()
            ) {
                // Ваша логика здесь
                life=5
                bigMedicine = null
            }
            if (BaseBalls[i].shx >= Opponent.vx
                && BaseBalls[i].shx <= Opponent.vx + Opponent.getOpponentWidth()
                && BaseBalls[i].shy >= Opponent.vy+300
                && BaseBalls[i].shy <= Opponent.vy+300 + Opponent.getOpponentHeight()
            ) {
                points+=10
                BaseBalls.removeAt(i)
                reaction = Reaction(context, Opponent.vx, Opponent.vy+300)
                reactions.add(reaction)
                i--
            } else if (BaseBalls[i].shy <= 0) {
                BaseBalls.removeAt(i)
                i--
            }
            i++
        }
        var j=0
        while (reactions.size>0&&j<reactions.size) {
            canvas.drawBitmap(
                reactions[j].getExplosionFrame(reactions[j].frame),
                reactions[j].ex.toFloat(), (reactions[j].ey).toFloat(), null
            )
            reactions[j].frame++
            if (reactions[j].frame > 3) {
                reactions.removeAt(j)
                j--
            }
            j++
        }
        if (!paused) {
            handler?.postDelayed(runnable, UPDATE_MILLIS)
        }

    }

    private fun checkAndSaveHighScore() {
        val sharedPreferences = context.getSharedPreferences("HighScore", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("highScore", 0)

        if (points > highScore) {
            // Текущий счет превышает рекорд, сохраняем новый рекорд
            with(sharedPreferences.edit()) {
                putInt("highScore", points)
                apply()
            }


        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y
        if (event?.action == MotionEvent.ACTION_UP) {
            // Проверяем, что касание произошло в пределах кнопки
            if (touchX != null && touchY != null && touchX < button.width && touchY < button.height) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            } else {
                // Если касание не на кнопке, то обрабатываем движение plane
                if (BaseBalls.size < 3) {
                    val ball = Ball(context, (base.mx + base.getMedicineWidth() / 2), base.my-300)
                    BaseBalls.add(ball)
                }
            }
        }

        if (event?.action == MotionEvent.ACTION_DOWN || event?.action == ACTION_MOVE) {
            // Проверяем, что касание не на кнопке, иначе не обрабатываем движение plane
            if (!(touchX != null && touchY != null && touchX < button.width && touchY < button.height)) {
                base.mx = touchX?.toInt() ?: 0
            }
        }
        return true
    }


    companion object {
        private const val UPDATE_MILLIS = 30L
        private const val SIZE = 60f
        var screenWidth = 0
        var screenHeight = 0
        fun setScreenSize(context: Context) {
            val display = (context as Activity).windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
            screenHeight = size.y
        }
    }
    private fun startMedicineTimer() {
        if (medicineTimer == null) {
            medicineTimer = Handler()
            medicineTimer?.postDelayed({
                val minX = screenWidth * 0.3f // 20% от ширины экрана
                val maxX = screenWidth * 0.8f // 80% от ширины экрана
                val minY = screenHeight * 0.3f // 20% от высоты экрана
                val maxY = screenHeight * 0.7f // 80% от высоты экрана
                medicine = Medicine(context, minX + random.nextFloat() * (maxX - minX), minY + random.nextFloat() * (maxY - minY))
                invalidate() // Перерисовываем View
                medicineTimer = null
            }, 5000L)
        }
    }

    private fun startBigMedicineTimer() {
        if (medicineTimer == null) {
            medicineTimer = Handler()
            medicineTimer?.postDelayed({
                val minX = screenWidth * 0.3f // 20% от ширины экрана
                val maxX = screenWidth * 0.8f // 80% от ширины экрана
                val minY = screenHeight * 0.3f // 20% от высоты экрана
                val maxY = screenHeight * 0.6f // 80% от высоты экрана
                bigMedicine = BigMedicine(context, minX + random.nextFloat() * (maxX - minX), minY + random.nextFloat() * (maxY - minY))
                invalidate() // Перерисовываем View
                medicineTimer = null
            }, 5000L)
        }
    }



}
