package com.example.quizzler_android_kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.quizzler_android_kotlin.helpers.ObservableObject
import com.example.quizzler_android_kotlin.helpers.Quiz
import com.example.quizzler_android_kotlin.helpers.QuizTimer

class MainActivityViewModel : ViewModel() {

    private var mQuiz = lazy { Quiz() }
    private var mCorrectAnswers = 0
    val correctAnswers: Int
        get() = mCorrectAnswers
    private var mCurrentQuestion = 0

    private val mCurrentQuestionText: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }
    val currentQuestionText: LiveData<String?> = mCurrentQuestionText

    private val mIsTimerUp: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isTimerUp: LiveData<Boolean> = mIsTimerUp

    private val mIsAnswerCorrect: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isAnswerCorrect: LiveData<Boolean> = mIsAnswerCorrect

    private var quizTimer: QuizTimer? = null
    val getQuestionCount = mQuiz.value.questionCount
    val getFirstQuestion = mQuiz.value.firstQuestion


    fun reset() {
        mCorrectAnswers = 0
        mCurrentQuestion = 0
    }

    fun startTimer(forSeconds: Int) {
        mIsTimerUp.value = false
        quizTimer = QuizTimer(forSeconds) {
            mIsTimerUp.postValue(true)
            mIsTimerUp.postValue(true)
        }
        quizTimer?.startTimer()
    }

    fun nextQuestion() {
        mCurrentQuestion++
        mCurrentQuestionText.value = mQuiz.value.getQuestion(mCurrentQuestion)
    }

    fun setIsAnswerCorrect(answer: String) {
        mIsAnswerCorrect.value = mQuiz.value.isAnswerCorrect(answer, mCurrentQuestion)
    }

    fun increaseCorrectAnswers() {
        mCorrectAnswers++
    }

    override fun onCleared() {
        super.onCleared()
        quizTimer?.cancel()
    }

}