package com.example.quizzler_android_kotlin.helpers

class ObservableObject<T : Any?>(private var value: T) {
    private var observer: Any? = null
    private var onValueChanged: ((T) -> Unit)? = null

    fun observe(observer: Any, onValueChanged: (T) -> (Unit)) {
        this.observer = observer
        this.onValueChanged = onValueChanged
    }

    fun setValue(value: T, doNotNotify: Boolean = false) {
        this.value = value
        if (!doNotNotify) onValueChanged?.invoke(value)
    }
}