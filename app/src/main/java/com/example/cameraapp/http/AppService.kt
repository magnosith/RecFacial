package com.example.cameraapp.http

import com.example.cameraapp.model.ResponseClassifyRec
import com.example.cameraapp.requisicao_recfacial
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AppService {
    @Multipart
    @POST("classify")
    fun recFacial(
            @Part("nrCpf") nrCpf: RequestBody,
            @Part ("image") image: RequestBody
    ): Call<ResponseClassifyRec>




}