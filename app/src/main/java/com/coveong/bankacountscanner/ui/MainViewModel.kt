package com.coveong.bankacountscanner.ui


import android.util.Base64
import androidx.lifecycle.ViewModel
import com.coveong.bankacountscanner.BuildConfig
import com.coveong.bankacountscanner.remote.GoogleVisionService
import com.example.coveong.models.Feature
import com.example.coveong.models.GoogleApiRequest
import com.example.coveong.models.ImageInfo
import com.example.coveong.models.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.File


class MainViewModel : ViewModel() {

    private val BASE_URL = "https://vision.googleapis.com/"
    private val TEST_IMAGE_LOCATION = "/sdcard/Pictures/test.jpg"

    private lateinit var retrofit: Retrofit

    fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }


    fun getAccountInfo() {
        val googleVisionService: GoogleVisionService = retrofit.create(GoogleVisionService::class.java)

        val requests = makeRequestInfoWithImage(getTestImage())
        val googleApiRequest = GoogleApiRequest(requests)

        val call: Call<Object> = googleVisionService.getImageText(BuildConfig.GOOGLE_VISION_API_KEY, googleApiRequest)

        call.enqueue(object : Callback<Object> {
            override fun onResponse(call: Call<Object>, response: Response<Object>) {
                println(response.body())
            }

            override fun onFailure(call: Call<Object>, t: Throwable) {
                print(t.message)
            }
        })

    }

    private fun makeRequestInfoWithImage(image: String): List<Request> {
        val requests = Request(
            ImageInfo(image), Feature("TEXT_DETECTION", 1)
        )

        return listOf(requests)
    }

    // FIXME: 사진 전달받는 코드 생기면 삭제할 코드
    private fun getTestImage(): String {
        val file = File(TEST_IMAGE_LOCATION)
        return fileToString(file)
    }

    fun fileToString(attachment: File): String {
        return Base64.encodeToString(attachment.readBytes(), Base64.NO_WRAP)
    }


}
