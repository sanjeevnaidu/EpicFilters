package com.startup.epicfilters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class FinalImageActivity : AppCompatActivity() {

    lateinit var btnGoBack: Button
    lateinit var finalImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_image)

        btnGoBack = findViewById(R.id.btnGoBack)
        finalImage = findViewById(R.id.imgFinalImage)

        val selectedFilter: String = intent.getStringExtra("selectedFilter").toString()

        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(intent.getStringExtra("imageUri")))

        when(selectedFilter){
            "grayscale"->
                finalImage.setImageBitmap(createGrayscale(bitmap))
            "photo/effect"->
                finalImage.setImageBitmap(createBlackAndWhite(bitmap))
            "blur"->
                Glide.with(this)
                    .load(Uri.parse(intent.getStringExtra("imageUri")))
                    .fitCenter()
                    .override(300, 150) // (change according to your wish)
                    .into(finalImage)
        }

        btnGoBack.setOnClickListener {
            finish()
        }

    }

    private fun createBlackAndWhite(src: Bitmap): Bitmap? {
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                var gray = (0.2989 * R + 0.5870 * G + 0.1140 * B).toInt()

                // use 128 as threshold, above -> white, below -> black
                gray = if (gray > 128) 255 else 0
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray))
            }
        }
        return bmOut
    }

    private fun createGrayscale(src: Bitmap): Bitmap? {
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                val gray = (0.2989 * R + 0.5870 * G + 0.1140 * B).toInt()
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray))
            }
        }
        return bmOut
    }
}