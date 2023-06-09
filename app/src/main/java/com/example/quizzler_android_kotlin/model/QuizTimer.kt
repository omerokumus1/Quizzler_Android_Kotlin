package com.example.quizzler_android_kotlin.model

import java.util.*
import kotlin.concurrent.timerTask

class QuizTimer(private val totalSeconds: Int, private val onFinish: () -> Unit) {
    private val timer = Timer()

    fun startTimer() {
        timer.schedule(timerTask { onFinish()}, totalSeconds*1000L)
    }
}