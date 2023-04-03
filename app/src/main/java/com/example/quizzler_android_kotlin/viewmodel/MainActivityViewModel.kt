package com.example.quizzler_android_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizzler_android_kotlin.helpers.ObservableObject

class MainActivityViewModel : ViewModel() {

    private var mQuiz = lazy { Quiz() }
    private var mCorrectAnswers = 0
    val correctAnswers: Int
        get() = mCorrectAnswers
    private var mCurrentQuestion = 0

    private var mCurrentQuestionText: ObservableObject<String?> =
        ObservableObject(mQuiz.value.firstQuestion)
    val currentQuestionText: ObservableObject<String?>
        get() = mCurrentQuestionText

    private var quizTimer: QuizTimer? = null
    private var mIsTimerUp = ObservableObject(false)
    val isTimerUp: ObservableObject<Boolean>
        get() = mIsTimerUp

    private var mIsAnswerCorrect = ObservableObject(false)
    val isAnswerCorrect: ObservableObject<Boolean>
        get() = mIsAnswerCorrect

    val getQuestionCount = mQuiz.value.questionCount
    val getFirstQuestion = mQuiz.value.firstQuestion


    fun reset() {
        mCorrectAnswers = 0
        mCurrentQuestion = 0
    }

    fun startTimer(forSeconds: Int) {
        mIsTimerUp.setValue(false)
        quizTimer = QuizTimer(forSeconds) {
            isTimerUp.setValue(true)
            isTimerUp.setValue(value = false, doNotNotify = true)
        }
        quizTimer?.startTimer()
    }

    fun nextQuestion() {
        mCurrentQuestion++
        currentQuestionText.setValue(mQuiz.value.getQuestion(mCurrentQuestion))
    }

    fun setIsAnswerCorrect(answer: String) {
        mIsAnswerCorrect.setValue(mQuiz.value.isAnswerCorrect(answer, mCurrentQuestion))
    }

    fun increaseCorrectAnswers() {
        mCorrectAnswers++
    }

    override fun onCleared() {
        super.onCleared()
        quizTimer?.cancel()
    }

}