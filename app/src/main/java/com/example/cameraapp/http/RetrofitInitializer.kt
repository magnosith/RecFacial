package com.example.cameraapp.http

import android.content.Context
import android.widget.EditText
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInitializer (context: Context) {

    companion object{

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val okkHttpclient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()


    val retrofitRecFacial =
        Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("http://192.168.2.57:5003/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitrec by lazy {

            retrofitRecFacial.create(AppService::class.java)

        }

//    fun recFacialService(Image: String?, cpf: EditText): AppService{
//        return retrofitRecFacial.create(AppService::class.java)
    // }

    }
}

