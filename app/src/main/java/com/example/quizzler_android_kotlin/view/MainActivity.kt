package com.example.quizzler_android_kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizzler_android_kotlin.R
import com.example.quizzler_android_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}