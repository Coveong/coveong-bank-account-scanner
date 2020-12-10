package com.coveong.bankacountscanner.ui.main


import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.coveong.bankacountscanner.BuildConfig
import com.coveong.bankacountscanner.remote.GoogleVisionService
import com.coveong.bankacountscanner.util.CoveongAccountParser
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.coveong.bankacountscanner.error.CameraException
import com.coveong.bankacountscanner.error.HttpBadResponseException
import com.coveong.bankacountscanner.error.handleError
import com.coveong.bankacountscanner.models.*
import com.coveong.bankacountscanner.util.Event
import retrofit2.*
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.ByteArrayOutputStream


class MainViewModel : ViewModel() {

    private lateinit var retrofit: Retrofit

    private var _onClickCopyAccountInfo = MutableLiveData<Event<String>>()
    val onClickCopyAccountInfo: LiveData<Event<String>> = _onClickCopyAccountInfo

    private var _onClickShareIcon = MutableLiveData<Event<String>>()
    val onClickShareIcon: LiveData<Event<String>> = _onClickShareIcon

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _onError = MutableLiveData<Event<Throwable>>()
    val onError: LiveData<Event<Throwable>> = _onError

    var accountInfo = MutableLiveData<AccountInfo>(AccountInfo("", ""))

    fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.GOOGLE_VISION_BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    fun getAccountInfo(image: Bitmap) {
        _isLoading.value = true

        val googleVisionService: GoogleVisionService = retrofit.create(GoogleVisionService::class.java)

        val requests = makeRequestInfoWithImage(encodeBitmapImage(image))
        val googleApiRequest = GoogleApiRequest(requests)

        val call: Call<GoogleApiResponse> = googleVisionService.getImageText(BuildConfig.GOOGLE_VISION_API_KEY, googleApiRequest)

        call.enqueue(object : Callback<GoogleApiResponse> {
            override fun onResponse(call: Call<GoogleApiResponse>, response: Response<GoogleApiResponse>) {
                val googleApiResponse = response.body()
                val result = googleApiResponse?.textAnnotations?.get(0)?.text?.get(0)?.description

                if (result != null) {
                    accountInfo.postValue(CoveongAccountParser.parseAccount(result))
                }
                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<GoogleApiResponse>, t: Throwable) {
                _onError.postValue(Event(t))
                _isLoading.postValue(false)
            }
        })
    }

    private fun makeRequestInfoWithImage(image: String): List<Request> {
        val requests = Request(
            image = ImageInfo(image),
            features = Feature(type = GET_TEXT_FROM_IMAGE, maxResults = 1),
            imageContext = ImageContext(languageHints = listOf(
                KOREAN_LANGUAGE
            ))
        )

        return listOf(requests)
    }

    private fun encodeBitmapImage(image: Bitmap): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP)
    }

    fun onClickCopyAccountInfo() {
        _onClickCopyAccountInfo.postValue(Event(accountInfo.value.toString()))
    }

    fun onClickShareIcon() {
        _onClickShareIcon.postValue(Event(accountInfo.value.toString()))
    }

    companion object {
        private const val GET_TEXT_FROM_IMAGE = "DOCUMENT_TEXT_DETECTION" // 이미지에서 텍스트 인식(필기체 위주)
        private const val KOREAN_LANGUAGE = "ko-t-i0-handwrit" // 한국어 필기
    }

}
