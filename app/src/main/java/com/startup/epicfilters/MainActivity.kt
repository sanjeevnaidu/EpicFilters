package com.startup.epicfilters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.MultipartBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File


const val baseUri = "https://studio.pixelixe.com/api/"


class MainActivity : AppCompatActivity() {

    private lateinit var btnUpload: Button
    private lateinit var btnSubmit: Button
    lateinit var imgOriginal: ImageView
    private lateinit var imgBW: ImageButton
    private lateinit var imgBlur: ImageButton
    private lateinit var imgThreshold: ImageButton
    private lateinit var imgPreview: ImageView
    private var selectedFilter: String? = null
    private lateinit var bitmap: Bitmap
    private lateinit var imageUri: Uri
    private lateinit var pd: ProgressDialog
    lateinit var text: TextView


    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnUpload = findViewById(R.id.btnUpload)
        btnSubmit = findViewById(R.id.btnSubmit)
        imgOriginal = findViewById(R.id.imgOriginal)
        imgPreview = findViewById(R.id.imgPreview)
        imgBW = findViewById(R.id.imgBW)
        imgBlur = findViewById(R.id.imgBlur)
        imgThreshold = findViewById(R.id.imgThreshold)
        imageUri = Uri.parse("android.resource://com.startup.epicfilters/drawable/original")
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        pd = ProgressDialog(this)
        text = findViewById(R.id.textView5)

        imgBW.background = getDrawable(R.drawable.filter_background)
        imgBlur.background = null //getDrawable(R.drawable.filter_background)
        imgThreshold.background = null //getDrawable(R.drawable.filter_background)
        imgPreview.setImageBitmap(createGrayscale(bitmap))

        selectedFilter = "grayscale"

        btnUpload.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    pd.setMessage("Loading...")
                    pd.setCancelable(false)
                    pd.show()
                    startForProfileImageResult.launch(intent)
                }
        }

        imgBW.setOnClickListener {
            imgBW.background = getDrawable(R.drawable.filter_background)
            imgBlur.background = null //getDrawable(R.drawable.filter_background)
            imgThreshold.background = null //getDrawable(R.drawable.filter_background)
            imgPreview.setImageBitmap(createGrayscale(bitmap))

            selectedFilter = "grayscale"
        }

        imgBlur.setOnClickListener {
            imgBlur.background = getDrawable(R.drawable.filter_background)
            imgBW.background = null //getDrawable(R.drawable.filter_background)
            imgThreshold.background = null //getDrawable(R.drawable.filter_background)
            Glide.with(this)
                .load(imageUri)
                .fitCenter()
                .override(300, 150) // (change according to your wish)
                .into(imgPreview)

            selectedFilter = "blur"
        }

        imgThreshold.setOnClickListener {
            imgThreshold.background = getDrawable(R.drawable.filter_background)
            imgBlur.background = null //getDrawable(R.drawable.filter_background)
            imgBW.background = null //getDrawable(R.drawable.filter_background)
            imgPreview.setImageBitmap(createBlackAndWhite(bitmap))

            selectedFilter = "photo/effect"
        }


        btnSubmit.setOnClickListener {
            if (selectedFilter.toString() != "null"){
                //Toast.makeText(this, baseUri + selectedFilter, Toast.LENGTH_SHORT).show()

//                val sourceFile: File = File(imageUri.toString())
//
//                val MEDIA_TYPE_PNG = MediaType.parse(imageUri.toString())
//                val req: RequestBody = MultipartBuilder().addFormDataPart("image","sanjeev.png",
//                    RequestBody.create(MEDIA_TYPE_PNG,sourceFile)).build()
//
//                val okHttpClient = OkHttpClient()
//                val request = Request.Builder().url("http://192.168.101.173:3000/img").post(req).build()  //baseUri + selectedFilter.toString()
//                okHttpClient.newCall(request).execute()

                //val bitmap = imgPreview.drawable.toBitmap()

                val intent = Intent(this, FinalImageActivity::class.java)
                intent.putExtra("selectedFilter",selectedFilter)
                intent.putExtra("imageUri",imageUri.toString())
                startActivity(intent)

            }else{
                Toast.makeText(this, "Please select a filter", Toast.LENGTH_SHORT).show()
            }
        }

    }

//    fun uploadImage(file: File?): JSONObject? {
//        try {
//            val MEDIA_TYPE_PNG = MediaType.parse("image/png")
//            val req: RequestBody = MultipartBuilder().setType(MultipartBody.FORM)
//                .addFormDataPart("userid", "8457851245")
//                .addFormDataPart(
//                    "userfile",
//                    "profile.png",
//                    RequestBody.create(MEDIA_TYPE_PNG, file)
//                )
//                .build()
//            val request: Request = Request.Builder()
//                .url("url")
//                .post(req)
//                .build()
//            val client = OkHttpClient()
//            val response = client.newCall(request).execute()
//            Log.d("response", "uploadImage:" + response.body()!!.string())
//            return JSONObject(response.body()!!.string())
//        } catch (e: UnknownHostException) {
//            Log.e(TAG, "Error: " + e.localizedMessage)
//        } catch (e: UnsupportedEncodingException) {
//            Log.e(TAG, "Error: " + e.localizedMessage)
//        } catch (e: Exception) {
//            Log.e(TAG, "Other Error: " + e.localizedMessage)
//        }
//        return null
//    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK

                    imageUri = data!!.data!!
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

                    imgOriginal.setImageURI(data.data!!)
                    imgBW.setImageBitmap(createGrayscale(bitmap))
                    imgThreshold.setImageBitmap(createBlackAndWhite(bitmap))
                    //imgBlur.setImageBitmap(createBlurImage(bitmap))

                    imgBW.cropToPadding
                    //imgBlur.cropToPadding
                    imgThreshold.cropToPadding
                    Glide.with(this)
                        .load(imageUri)
                        .fitCenter()
                        .override(300, 150) // (change according to your wish)
                        .into(imgBlur)                    //mProfileUri = fileUri

                    imgBW.background = getDrawable(R.drawable.filter_background)
                    imgBlur.background = null //getDrawable(R.drawable.filter_background)
                    imgThreshold.background = null //getDrawable(R.drawable.filter_background)
                    imgPreview.setImageBitmap(createGrayscale(bitmap))

                    /*val sharedPreferences =
                        getSharedPreferences("uploadImage", Context.MODE_PRIVATE)

                    sharedPreferences.edit().putString("image_uri", fileUri.toString()).apply()

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            UploadImageFragment()
                        ).commit()
                    requireActivity().toolbar.title = "Preview Image"*/

                }
                ImagePicker.RESULT_ERROR -> {
                    /*activity?.let {
                        Snackbar.make(
                            it.findViewById(R.id.frame),
                            "An Error occurred",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }*/
                    Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    /*activity?.let {
                        Snackbar.make(
                            it.findViewById(R.id.frame),
                            "Task Cancelled",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }*/
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                pd.dismiss()
            }, 1000)
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