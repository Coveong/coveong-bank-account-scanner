package com.coveong.bankacountscanner

import com.coveong.bankacountscanner.ui.camera.CameraActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.coveong.bankacountscanner.databinding.ActivityMainBinding
import com.coveong.bankacountscanner.ui.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        settingMainViewModel()
        settingContentView()
        requestPreviewImageIfNull()

    }

    private fun requestPreviewImageIfNull() {
        if (preview_imageView.background == null) {
            startActivityForResult(
                Intent(this, CameraActivity::class.java),
                REQUEST_CAMERA_PICTURE
            )
        }
    }

    private fun settingMainViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java).apply {
            initializeRetrofit()
        }
    }

    private fun settingContentView() {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            viewModel = mainViewModel
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CAMERA_PICTURE -> when (resultCode) {
                CameraActivity.RESULT_CAMERA_IMAGE_RECEIVED -> {
                    val image = data?.getParcelableExtra("image") as? Bitmap
                    image?.let {
                        setCameraImagePreview(it)
                        mainViewModel.getAccountInfo(it)
                    }
                }
            }
        }
    }

    private fun setCameraImagePreview(image: Bitmap) {
        preview_imageView.background = BitmapDrawable(resources, image)
    }

    companion object {
        private const val REQUEST_CAMERA_PICTURE = 0x1002
    }

}
