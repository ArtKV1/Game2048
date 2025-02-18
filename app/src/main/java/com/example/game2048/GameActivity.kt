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
import androidx.core.content.ContextCompat


class GameActivity : ComponentActivity(), GestureDetector.OnGestureListener
{

    private lateinit var timerTextView: TextView  // Поле для отображения времени
    private var seconds = 0  // Счетчик секунд
    private var isCreated = false // Флаг проверки создания таймера
    private var isRunning = false  // Флаг состояния таймера
    private var isRestartDealogShowing = false
    private val handler = Handler(Looper.getMainLooper())
    private var totalTranslationX = 0f

    private val colorMap = mapOf(
        "" to R.color.game0,
        "2" to R.color.game2,
        "4" to R.color.game4,
        "8" to R.color.game8,
        "16" to R.color.game16,
        "32" to R.color.game32,
        "64" to R.color.game64,
        "128" to R.color.game128,
        "256" to R.color.game256,
        "512" to R.color.game512,
        "1024" to R.color.game1024,
        "2048" to R.color.game2048
    )

    companion object
    {
        public var masOfAnimationCount: Array<Int> = Array(16) { 0 }
        public lateinit var masOfNumbers: Array<Int>
        public var score: Int = 0
    }

    private lateinit var gestureDetector: GestureDetector

    private fun startGame()
    {
        masOfNumbers = Array(16) { 0 }
        //ДЛЯ ТЕСТА!
        //GameBrain.testGeneration()
        GameBrain.getTwoRandomNumbersForButtons()
        for (i in 0..15)
        {
            val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
            val myButton = findViewById<Button>(buttonId)
            if (masOfNumbers[i] != 0)
            {
                myButton.text = masOfNumbers[i].toString()
            }
            myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamescene)

        startGame()

        timerTextView = findViewById(R.id.timerTextView)
        startTimer()

        val button: Button = findViewById(R.id.button_pause)

        button.setOnClickListener {
            isRunning = false
            showRestartDialog()
            Toast.makeText(this, "Нажата кнопка: ${button.text}", Toast.LENGTH_SHORT).show()
        }

        val button3: Button = findViewById(R.id.button_pause2)

        button3.setOnClickListener {
            finish()
            Toast.makeText(this, "Нажата кнопка: ${button3.text}", Toast.LENGTH_SHORT).show()
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
        score = 0
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

        fun genRect()
        {
            val buttonIdInt = GameBrain.getRandomNumberForButton()
            if (buttonIdInt != null)
            {
                val buttonId = resources.getIdentifier("button${buttonIdInt+1}", "id", packageName)
                val myButton = findViewById<Button>(buttonId)
                myButton.text = masOfNumbers[buttonIdInt].toString()
                myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
            }
        }

        if (Math.abs(diffX) > Math.abs(diffY))
        {
            if (diffX > 0 && GameBrain.canTurnRight())
            {
                GameBrain.turnArrayRight()

                for (i in 0..15) {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null

                    when (masOfAnimationCount[i]) {
                        1 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_right1)
                        2 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_right2)
                        3 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_right3)
                    }

                    if (translateAnimation != null) {
                        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {
                                myButton.text = ""
                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Выполняем код после завершения анимации
                                if (masOfNumbers[i] != 0) {
                                    myButton.text = masOfNumbers[i].toString()
                                }
                                else
                                {
                                    myButton.text = ""
                                }
                                myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                            }

                            override fun onAnimationRepeat(animation: Animation?) {
                                // Не используется
                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    } else {
                        // Если анимации нет, сразу обновляем текст и цвет кнопки
                        if (masOfNumbers[i] != 0) {
                            myButton.text = masOfNumbers[i].toString()
                        }
                        else
                        {
                            myButton.text = ""
                        }
                        myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                    }
                }
                genRect()

            }
            else if (diffX < 0 && GameBrain.canTurnLeft())
            {
                GameBrain.turnArrayLeft()
                for (i in 0..15) {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null

                    when (masOfAnimationCount[i]) {
                        1 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_left1)
                        2 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_left2)
                        3 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_left3)
                    }

                    if (translateAnimation != null) {
                        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {
                                myButton.text = ""
                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Выполняем код после завершения анимации
                                if (masOfNumbers[i] != 0) {
                                    myButton.text = masOfNumbers[i].toString()
                                }
                                else
                                {
                                    myButton.text = ""
                                }
                                myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                            }

                            override fun onAnimationRepeat(animation: Animation?) {
                                // Не используется
                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    } else {
                        // Если анимации нет, сразу обновляем текст и цвет кнопки
                        if (masOfNumbers[i] != 0) {
                            myButton.text = masOfNumbers[i].toString()
                        }
                        else
                        {
                            myButton.text = ""
                        }
                        myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                    }
                }
                genRect()
            }
        }
        else
        {
            if (diffY > 0 && GameBrain.canTurnDown())
            {
                GameBrain.turnArrayDown()
                for (i in 0..15) {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null

                    when (masOfAnimationCount[i]) {
                        1 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_down1)
                        2 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_down2)
                        3 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_down3)
                    }

                    if (translateAnimation != null) {
                        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {
                                myButton.text = ""
                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Выполняем код после завершения анимации
                                if (masOfNumbers[i] != 0)
                                {
                                    myButton.text = masOfNumbers[i].toString()
                                }
                                else
                                {
                                    myButton.text = ""
                                }
                                myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                            }

                            override fun onAnimationRepeat(animation: Animation?) {
                                // Не используется
                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    } else {
                        // Если анимации нет, сразу обновляем текст и цвет кнопки
                        if (masOfNumbers[i] != 0)
                        {
                            myButton.text = masOfNumbers[i].toString()
                        }
                        else
                        {
                            myButton.text = ""
                        }
                        myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                    }
                }
                genRect()
            }
            else if (diffY < 0 && GameBrain.canTurnUp())
            {
                GameBrain.turnArrayUp()
                for (i in 0..15) {
                    val buttonId = resources.getIdentifier("button${i+1}", "id", packageName)
                    val myButton = findViewById<Button>(buttonId)
                    var translateAnimation: Animation? = null

                    when (masOfAnimationCount[i]) {
                        1 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_up1)
                        2 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_up2)
                        3 -> translateAnimation = AnimationUtils.loadAnimation(this, R.anim.move_up3)
                    }

                    if (translateAnimation != null) {
                        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {
                                myButton.text = ""
                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                // Выполняем код после завершения анимации
                                if (masOfNumbers[i] != 0) {
                                    myButton.text = masOfNumbers[i].toString()
                                }
                                else
                                {
                                    myButton.text = ""
                                }
                                myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                            }

                            override fun onAnimationRepeat(animation: Animation?) {
                                // Не используется
                            }
                        })

                        myButton.startAnimation(translateAnimation)
                    } else {
                        // Если анимации нет, сразу обновляем текст и цвет кнопки
                        if (masOfNumbers[i] != 0) {
                            myButton.text = masOfNumbers[i].toString()
                        }
                        else
                        {
                            myButton.text = ""
                        }
                        myButton.setBackgroundColor(getResources().getColor(colorMap[myButton.text.toString()]!!))
                    }
                }
                genRect()
            }
        }

        masOfAnimationCount = Array(16) { 0 }
        var textScore: TextView = findViewById(R.id.textView5)
        if (!GameBrain.isGameOver()) showToast("Игра закончена")
        textScore.text = score.toString()
        return true
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
            isRestartDealogShowing = false
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
                isRestartDealogShowing = false
            }
        }

        builder.show()
    }
}