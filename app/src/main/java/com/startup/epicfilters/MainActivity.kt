package com.startup.epicfilters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    lateinit var btnUpload: Button
    lateinit var btnSubmit: Button
    lateinit var imgOriginal: ImageView
    lateinit var imgBW: ImageView
    lateinit var imgBlur: ImageView
    lateinit var imgThreshold: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnUpload = findViewById(R.id.btnUpload)
        btnSubmit = findViewById(R.id.btnSubmit)
        imgOriginal = findViewById(R.id.imgOriginal)
        imgBW = findViewById(R.id.imgBW)
        imgBlur = findViewById(R.id.imgBlur)
        imgThreshold = findViewById(R.id.imgThreshold)

        btnUpload.setOnClickListener {

        }

    }
}