package com.example.quizzler_android_kotlin.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.quizzler_android_kotlin.R
import com.example.quizzler_android_kotlin.databinding.ActivityMainBinding
import com.example.quizzler_android_kotlin.viewmodel.EMPTY
import com.example.quizzler_android_kotlin.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var clickedButton: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        initViews()
        setClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        observeIsAnswerCorrect()
        observeIsTimerUp()
        observeCurrentQuestionText()
    }

    private fun observeCurrentQuestionText() {
        viewModel.currentQuestionText.subscribe { nextQuestion ->
            if (nextQuestion != EMPTY) {
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

    private fun observeIsTimerUp() {
        viewModel.isTimerUp.subscribe {
            if (it) {
                runOnUiThread { displayNextQuestion() }
            }
        }
    }

    private fun observeIsAnswerCorrect() {
        viewModel.isAnswerCorrect.subscribe {
            val otherButton = getOtherButton()
            if (it) {
                viewModel.increaseCorrectAnswers()
                updateScoreText()
                colorizeButtons(green = clickedButton, red = null)
            } else {
                colorizeButtons(green = otherButton, red = clickedButton)
            }
            disableButton(clickedButton)
            disableButton(otherButton)
            incrementProgressBar()
        }
    }

    private fun incrementProgressBar() {
        binding.progressbar.progress++
    }

    private fun initViews() {
        hideAndDisableButton(binding.startAgainBtn)
        initScoreText()
        initQuestionText()
        initProgressBar()
    }

    private fun initProgressBar() {
        binding.progressbar.max = viewModel.getQuestionCount
        binding.progressbar.progress = 1
    }

    private fun initQuestionText() {
        binding.questionText.text = viewModel.getFirstQuestion
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
        viewModel.reset()
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
            clickedButton = v as AppCompatButton
            viewModel.setIsAnswerCorrect(clickedButton?.text as String)
            viewModel.startTimer(1)
        }
    }

    private fun displayNextQuestion() {
        viewModel.nextQuestion()
    }

    private fun enableButton(button: AppCompatButton?) {
        button?.isEnabled = true
    }

    private fun disableButton(button: AppCompatButton?) {
        button?.isEnabled = false
    }

    private fun resetButtonColors() {
        binding.run {
            trueBtn.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_rounded_rectangle, null)
            falseBtn.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_rounded_rectangle, null)
        }
    }

    private fun colorizeButtons(green: AppCompatButton?, red: AppCompatButton?) {
        green?.background = ResourcesCompat.getDrawable(resources, R.drawable.color_green, null)
        red?.background = ResourcesCompat.getDrawable(resources, R.drawable.color_red, null)
    }

    private fun getOtherButton(): AppCompatButton =
        when (clickedButton?.text) {
            "True" -> binding.falseBtn
            "False" -> binding.trueBtn
            else -> binding.trueBtn
        }


    private fun updateScoreText() {
        binding.scoreText.text = "Score: ${viewModel.correctAnswers}"
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