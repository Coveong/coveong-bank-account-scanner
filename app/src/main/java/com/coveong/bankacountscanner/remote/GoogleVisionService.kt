package com.coveong.bankacountscanner.remote

import com.coveong.bankacountscanner.models.GoogleApiRequest
import com.coveong.bankacountscanner.models.GoogleApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GoogleVisionService {
    
    @POST("v1/images:annotate")
    fun getImageText(
        @Query("key") key: String? = null,
        @Body params: GoogleApiRequest
    ) : Call<GoogleApiResponse>

}
