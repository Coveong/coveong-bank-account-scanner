package com.coveong.bankacountscanner.ui


import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.coveong.bankacountscanner.BuildConfig
import com.coveong.bankacountscanner.remote.GoogleVisionService
import com.coveong.bankacountscanner.util.CoveongAccountParser
import com.example.coveong.models.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.coveong.bankacountscanner.util.Event
import com.example.coveong.models.Feature
import com.example.coveong.models.GoogleApiRequest
import com.example.coveong.models.ImageInfo
import com.example.coveong.models.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.ByteArrayOutputStream


class MainViewModel : ViewModel() {

    private lateinit var retrofit: Retrofit
    private lateinit var accountInfo: AccountInfo


    private val GET_TEXT_FROM_IMAGE = "TEXT_DETECTION"

    private var _onClickRecapture = MutableLiveData<Event<Unit>>()
    val onClickRecapture: LiveData<Event<Unit>> = _onClickRecapture

    private var _onClickCopyBankAccount = MutableLiveData<Event<String>>()
    val onClickCopyBankAccount: LiveData<Event<String>> = _onClickCopyBankAccount

    fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.GOOGLE_VISION_BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    fun getAccountInfo(image: Bitmap) {
        val googleVisionService: GoogleVisionService = retrofit.create(GoogleVisionService::class.java)

        val requests = makeRequestInfoWithImage(encodeBitmapImage(image))
        val googleApiRequest = GoogleApiRequest(requests)

        val call: Call<GoogleApiResponse> = googleVisionService.getImageText(BuildConfig.GOOGLE_VISION_API_KEY, googleApiRequest)

        call.enqueue(object : Callback<GoogleApiResponse> {
            override fun onResponse(call: Call<GoogleApiResponse>, response: Response<GoogleApiResponse>) {
                val googleApiResponse = response.body() as GoogleApiResponse
                val result = googleApiResponse.textAnnotations?.get(0)?.text?.get(0)?.description

                if (result != null) {
                    accountInfo = CoveongAccountParser.parseAccount(result)
                }
            }

            override fun onFailure(call: Call<GoogleApiResponse>, t: Throwable) {
                t.message?.let { Log.d("fail", it) }
            }
        })
    }

    private fun makeRequestInfoWithImage(image: String): List<Request> {
        val requests = Request(
            image = ImageInfo(image), features = Feature(type = GET_TEXT_FROM_IMAGE, maxResults = 1)
        )

        return listOf(requests)
    }

    private fun encodeBitmapImage(image: Bitmap): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP)
    }

    fun recaptureImage() {
        _onClickRecapture.postValue(Event(Unit))
    }

    fun copyBankAccount() {
        _onClickCopyBankAccount.postValue(Event("text"))  // FIXME: 나중에 실제 은행 계좌로 변경
    }

}

