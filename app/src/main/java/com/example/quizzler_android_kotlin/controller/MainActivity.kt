package com.example.quizzler_android_kotlin.controller

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import com.example.quizzler_android_kotlin.R
import com.example.quizzler_android_kotlin.databinding.ActivityMainBinding
import com.example.quizzler_android_kotlin.model.Quiz
import com.example.quizzler_android_kotlin.model.QuizTimer

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val quiz = Quiz()
    private var quizTimer: QuizTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setClickListeners()
    }

    private fun initViews() {
        hideAndDisableButton(binding.startAgainBtn)
        initScoreText()
        initQuestionText()
        initProgressBar()
    }

    private fun initProgressBar() {
        binding.progressbar.max = quiz.questionCount
        binding.progressbar.progress = 1
    }

    private fun initQuestionText() {
        binding.questionText.text = quiz.firstQuestion
    }

    private fun initScoreText() {
        binding.scoreText.text = "Score: 0"
    }

    private fun setClickListeners() {
        setStartAgainBtnClickListener()
        setTrueBtnClickListener()
        setFalseBtnClickListener()
    }

    private fun setStartAgainBtnClickListener() {
        binding.startAgainBtn.setOnClickListener { resetQuiz() }
    }

    private fun resetQuiz() {
        quiz.reset()
        initViews()
        showAndEnableButton(binding.trueBtn)
        showAndEnableButton(binding.falseBtn)
    }

    private fun setFalseBtnClickListener() {
        binding.falseBtn.setOnClickListener(this)
    }

    private fun setTrueBtnClickListener() {
        binding.trueBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            updateViews(v)
            quizTimer = QuizTimer(1, ::questionOnFinish)
            quizTimer?.startTimer()
        }
    }

    private fun questionOnFinish() {
        runOnUiThread {
            val nextQuestion = quiz.getNextQuestion()
            if (nextQuestion != null) {
                binding.questionText.text = nextQuestion
                resetButtonColors()
                enableButton(binding.trueBtn)
                enableButton(binding.falseBtn)
            } else {
                binding.questionText.text = "Quiz is Finished!"
                resetButtonColors()
                showAndEnableButton(binding.startAgainBtn)
                hideAndDisableButton(binding.trueBtn)
                hideAndDisableButton(binding.falseBtn)
            }
        }
    }


//    private fun setTimerForNextQuestion(button: View) {
//        timer.schedule(timerTask {
//            runOnUiThread {
//                resetButtonColors()
//                enableButton(button as AppCompatButton)
//                enableButton(getOtherButton(button))
//                goToNextQuestion()
//            }
//        }, 1000)
//    }

//    private fun goToNextQuestion() {
//        currentQuestion++
//        binding.progressbar.progress++
//        if (currentQuestion < questions.size) {
//            binding.questionText.text = getCurrentQuestion()
//        } else {
//            binding.questionText.text = "Quiz is Finished!"
//            showAndEnableButton(binding.startAgainBtn)
//            hideAndDisableButton(binding.trueBtn)
//            hideAndDisableButton(binding.falseBtn)
//        }
//    }

    private fun updateViews(button: View) {
        val otherButton = getOtherButton(button)
        val answer = (button as AppCompatButton).text.toString()
        if (quiz.isAnswerCorrect(answer)) {
            quiz.increaseCorrectAnswers()
            updateScoreText()
            colorizeButtons(green = button, red = null)
        } else {
            colorizeButtons(green = otherButton, red = button)
        }
        disableButton(button)
        disableButton(otherButton)

    }

    private fun enableButton(button: AppCompatButton) {
        button.isEnabled = true
    }

    private fun disableButton(button: AppCompatButton) {
        button.isEnabled = false
    }

    private fun resetButtonColors() {
        binding.run {
            trueBtn.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_rounded_rectangle, null)
            falseBtn.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_rounded_rectangle, null)
        }
    }

    private fun colorizeButtons(green: AppCompatButton, red: AppCompatButton?) {
        green.background = ResourcesCompat.getDrawable(resources, R.drawable.color_green, null)
        red?.background = ResourcesCompat.getDrawable(resources, R.drawable.color_red, null)
    }

    private fun getOtherButton(button: View): AppCompatButton =
        when ((button as AppCompatButton).text) {
            "True" -> binding.falseBtn
            "False" -> binding.trueBtn
            else -> binding.trueBtn
        }


    private fun updateScoreText() {
        binding.scoreText.text = "Score: ${quiz.correctAnswers}"
    }

    private fun hideAndDisableButton(button: AppCompatButton) {
        button.visibility = View.INVISIBLE
        button.isEnabled = false
    }

    private fun showAndEnableButton(startAgainBtn: AppCompatButton) {
        startAgainBtn.run {
            visibility = View.VISIBLE
            isEnabled = true
        }
    }
}