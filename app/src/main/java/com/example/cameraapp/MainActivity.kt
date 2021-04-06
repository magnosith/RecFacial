package com.example.cameraapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraapp.http.RetrofitInitializer
import com.example.cameraapp.model.ResponseClassifyRec
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

var photoFile: Bitmap? = null
class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    var Image: String? = null
    var cpf_aux: String? = null
    var image_aux: String? = null
    var resposta: Boolean? = null
    var base64: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_cam:Button = findViewById(R.id.btn_camera)
        val cpf = findViewById<EditText>(R.id.cpf_number)
     //   val imagePhoto = findViewById<EditText>(R.id.img_64)

        btn_cam.setOnClickListener(){
            dispatchTakePictureIntent()
        }

    //Evento do botão para chamar a busca na API

        val btn_buscar:Button = findViewById(R.id.btn_buscar);

        btn_buscar.setOnClickListener(View.OnClickListener {

            base64 = photoFile?.let { it1 -> convertTo64(it1) }


            cpf_aux = cpf.text.toString()
            val bodyCpf: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), cpf_aux!!)

            image_aux = base64.toString() //imagePhoto.text.toString()
            val bodyImg: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), image_aux!!)

            val consulta = RetrofitInitializer.retrofitrec.recFacial(bodyCpf, bodyImg)

            consulta.enqueue(object : Callback<ResponseClassifyRec> {
                override fun onResponse(call: Call<ResponseClassifyRec>, response: Response<ResponseClassifyRec>) {
                //    Toast.makeText(this@MainActivity, "Cola no pai que é sucesso!", Toast.LENGTH_SHORT).show()
                    Log.i(response.body()?.default_face_matching_classification.toString(), "Match+")
                    Log.i(response.body()?.error.toString(), "Match ")
                    Log.i(response.body()?.error_code.toString(), "Match ")
                    Log.i(response.body()?.message.toString(), "Match Sucessfull ")
                    resposta = response.body()?.default_face_matching_classification

                    if (resposta == true){
                        Toast.makeText(this@MainActivity, "Validado com sucesso", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@MainActivity, "Não Reconhecido", Toast.LENGTH_SHORT).show()
                    }

                }
                override fun onFailure(call: Call<ResponseClassifyRec>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Erro API gatão", Toast.LENGTH_SHORT).show()
                }

            })

        })
    }

    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var img_space: ImageView = findViewById(R.id.img_scan)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            img_space.setImageBitmap(imageBitmap)
            photoFile = imageBitmap

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun convertTo64(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


}