package com.example.game2048

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout


class GameActivity : ComponentActivity(), GestureDetector.OnGestureListener
{

    private lateinit var timerTextView: TextView  // Поле для отображения времени
    private var seconds = 0  // Счетчик секунд
    private var isCreated = false // Флаг проверки создания таймера
    private var isRunning = false  // Флаг состояния таймера
    private var isRestartDealogShowing = false
    private val handler = Handler(Looper.getMainLooper())
    private var totalTranslationX = 0f
    private var masOfAnimationCount: Array<Int> = Array(16) { 0 }

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?)
    {
        //TODO: Сделать кнопку паузы.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamescene)

        for (i in 0..15)
        {
            if ((i + 1) % 4 == 0)
                masOfAnimationCount[i] = 0
            else if ((i + 1) % 4 == 1)
                masOfAnimationCount[i] = 3
            else if ((i + 1) % 4 == 2)
                masOfAnimationCount[i] = 2
            else if ((i + 1) % 4 == 3)
                masOfAnimationCount[i] = 1
        }

        timerTextView = findViewById(R.id.timerTextView)
        startTimer()

        val button: Button = findViewById(R.id.button_pause)

        button.setOnClickListener {
            isRunning = false
            showRestartDialog()
            Toast.makeText(this, "Нажата кнопка: ${button.text}", Toast.LENGTH_SHORT).show()
        }

        val button2: Button = findViewById(R.id.buttonReset)

        button2.setOnClickListener {
            finish()
            val intent = Intent(this, GameActivity::class.java)
            Toast.makeText(this, "Нажата кнопка: ${button2.text}", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onStart()
    {
        //TODO: Сделать привязки свайпов. СДЕЛАНО!
        super.onStart()

        gestureDetector = GestureDetector(this, this)

        val gameLayout: ConstraintLayout = findViewById(R.id.gameLayout)

        // Устанавливаем обработчик нажатий для Layout
        //showToast("Обработчик свайпов был добавлен")
        gameLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }

    }


    override fun onPause()
    {
        //TODO: ОСТАНОВКА ТАЙМЕРА. СДЕЛАНО!
        super.onPause()
        isRunning = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStop()
    {
        //TODO: Убрать привязки свайпов. ОНО 100% РАБОТАЕТ! Потому что кнопка меняется!
        //TODO: СДЕЛАНО!
        super.onStop()

//        val button: Button = findViewById(R.id.button_pause)
//
//        button.text = "СТОП"

        val gameLayout: ConstraintLayout = findViewById(R.id.gameLayout)
        gameLayout.setOnTouchListener(null)
    }

    override fun onRestart()
    {
        super.onRestart()
        if (!isRestartDealogShowing)
            showRestartDialog()
    }

    override fun onDestroy()
    {
        //TODO: Сохранить счёт игрока в бд
        super.onDestroy()
        showToast("Активити GameActivity уничтожена")
    }

    override fun onResume() {
        //TODO: Начало таймера. СДЕЛАНО!
        super.onResume()
        if (!isCreated) {
            startTimer()
        }
    }

    private fun startTimer()
    {
        isRunning = true
        isCreated = true
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isRunning) {
                    seconds++
                    updateTimerText()
                    handler.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.dispatchTouchEvent(event)
    }

    @SuppressLint("ResourceType")
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 == null) return false

        val diffX = e2.x - e1.x
        val diffY = e2.y - e1.y

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > 0) {
                for (i in 0..15)
                {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null
                    if (masOfAnimationCount[i] == 1)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_right1)
                    }
                    else if (masOfAnimationCount[i] == 2)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_right2)
                    }
                    else if (masOfAnimationCount[i] == 3)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_right3)
                    }
                    if (translateAnimation != null) {
                        translateAnimation?.setAnimationListener(object :
                            Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {

                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Изменение цвета кнопки после завершения анимации
                                myButton.setBackgroundResource(0)

                                myButton.setBackgroundResource(android.R.drawable.btn_default)
                                myButton.setBackgroundColor(R.color.backgroundColor)
                            }

                            override fun onAnimationRepeat(animation: Animation?) {

                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    }

                }

            } else {
                for (i in 0..15)
                {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null
                    if (masOfAnimationCount[i] == 1)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_left1)
                    }
                    else if (masOfAnimationCount[i] == 2)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_left2)
                    }
                    else if (masOfAnimationCount[i] == 3)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_left3)
                    }
                    if (translateAnimation != null) {
                        translateAnimation?.setAnimationListener(object :
                            Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {

                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Изменение цвета кнопки после завершения анимации
                                myButton.setBackgroundResource(0)

                                myButton.setBackgroundResource(android.R.drawable.btn_default)
                                myButton.setBackgroundColor(R.color.backgroundColor)
                            }

                            override fun onAnimationRepeat(animation: Animation?) {

                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    }

                }
            }
        } else {
            if (diffY > 0) {
                for (i in 0..15)
                {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null
                    if (masOfAnimationCount[i] == 1)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_down1)
                    }
                    else if (masOfAnimationCount[i] == 2)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_down2)
                    }
                    else if (masOfAnimationCount[i] == 3)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_down3)
                    }
                    if (translateAnimation != null) {
                        translateAnimation?.setAnimationListener(object :
                            Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {

                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Изменение цвета кнопки после завершения анимации
                                myButton.setBackgroundResource(0)

                                myButton.setBackgroundResource(android.R.drawable.btn_default)
                                myButton.setBackgroundColor(R.color.backgroundColor)
                            }

                            override fun onAnimationRepeat(animation: Animation?) {

                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    }

                }
            } else {
                for (i in 0..15)
                {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null
                    if (masOfAnimationCount[i] == 1)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_up1)
                    }
                    else if (masOfAnimationCount[i] == 2)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_up2)
                    }
                    else if (masOfAnimationCount[i] == 3)
                    {
                        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_up3)
                    }
                    if (translateAnimation != null) {
                        translateAnimation?.setAnimationListener(object :
                            Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {

                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Изменение цвета кнопки после завершения анимации
                                myButton.setBackgroundResource(0)

                                myButton.setBackgroundResource(android.R.drawable.btn_default)
                                myButton.setBackgroundColor(R.color.backgroundColor)
                            }

                            override fun onAnimationRepeat(animation: Animation?) {

                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    }

                }
            }
        }
        return true
    }

    private fun dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDown(e: MotionEvent): Boolean = true
    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean = false
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean = false
    override fun onLongPress(e: MotionEvent) {}

    @SuppressLint("DefaultLocale")
    private fun updateTimerText()
    {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        val timeString = String.format("%02d:%02d:%02d", hours, minutes, secs)
        timerTextView.text = timeString
    }

    private fun showRestartDialog() {
        isRestartDealogShowing = true
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Пауза")
        builder.setMessage("Вы хотите продолжить игру или выйти в меню?")

        builder.setPositiveButton("Продолжить") { dialog, _ ->
            dialog.dismiss()
            if (!isRunning) {
                startTimer()
            }
        }

        builder.setNegativeButton("В меню") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        builder.setOnDismissListener {
            if (!isRunning) {
                startTimer()
            }
        }

        builder.show()
    }
}