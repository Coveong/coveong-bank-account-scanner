package com.coveong.bankacountscanner.remote

import com.example.coveong.models.GoogleApiRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GoogleVisionService {
    
    @POST("v1/images:annotate")
    fun getImageText(
        @Query("key") key: String? = null,
        @Body params: GoogleApiRequest
    ) : Call<Object>

}
