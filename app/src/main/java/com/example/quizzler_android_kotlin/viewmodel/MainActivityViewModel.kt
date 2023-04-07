package com.example.quizzler_android_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizzler_android_kotlin.helpers.Quiz
import com.example.quizzler_android_kotlin.helpers.QuizTimer
import io.reactivex.rxjava3.subjects.BehaviorSubject

const val EMPTY = ""

class MainActivityViewModel : ViewModel() {

    private var mQuiz = lazy { Quiz() }
    private var mCorrectAnswers = 0
    val correctAnswers: Int
        get() = mCorrectAnswers
    private var mCurrentQuestion = 0


    private val mCurrentQuestionText: BehaviorSubject<String> by lazy {
        BehaviorSubject.create()
    }
    val currentQuestionText: BehaviorSubject<String> = mCurrentQuestionText

    private val mIsTimerUp: BehaviorSubject<Boolean> by lazy { BehaviorSubject.create() }
    val isTimerUp: BehaviorSubject<Boolean> = mIsTimerUp

    private val mIsAnswerCorrect: BehaviorSubject<Boolean> by lazy { BehaviorSubject.create() }
    val isAnswerCorrect: BehaviorSubject<Boolean> = mIsAnswerCorrect

    private var quizTimer: QuizTimer? = null
    val getQuestionCount = mQuiz.value.questionCount
    val getFirstQuestion = mQuiz.value.firstQuestion


    fun reset() {
        mCorrectAnswers = 0
        mCurrentQuestion = 0
    }

    fun startTimer(forSeconds: Int) {
        mIsTimerUp.onNext(false)
        quizTimer = QuizTimer(forSeconds) {
            mIsTimerUp.onNext(true)
            mIsTimerUp.onNext(false)
        }
        quizTimer?.startTimer()
    }

    fun nextQuestion() {
        mCurrentQuestion++
        mCurrentQuestionText
            .onNext(mQuiz.value.getQuestion(mCurrentQuestion) ?: EMPTY)
    }

    fun setIsAnswerCorrect(answer: String) {
        mIsAnswerCorrect.onNext(mQuiz.value.isAnswerCorrect(answer, mCurrentQuestion))
    }

    fun increaseCorrectAnswers() {
        mCorrectAnswers++
    }

    override fun onCleared() {
        super.onCleared()
        quizTimer?.cancel()
    }

}
