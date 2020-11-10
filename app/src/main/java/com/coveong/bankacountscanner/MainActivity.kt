package com.coveong.bankacountscanner

import com.coveong.bankacountscanner.ui.camera.CameraActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.coveong.bankacountscanner.databinding.ActivityMainBinding
import com.coveong.bankacountscanner.ui.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initializeMainViewModel()
        settingContentView()
        requestPreviewImageIfNull()

    }

    private fun requestPreviewImageIfNull() {
        if (preview_imageView.background == null) {
            requestPreviewImage()
        }
    }

    private fun requestPreviewImage() {
        startActivityForResult(
            Intent(this, CameraActivity::class.java),
            REQUEST_CAMERA_PICTURE
        )
    }

    private fun initializeMainViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java).apply {
            initializeRetrofit()
            onClickRecapture.observe(
                this@MainActivity, Observer { event ->
                    event.getExtraIfNotHandled()?.let {
                        requestPreviewImage()
                    }
                }
            )
            onClickCopyBankAccount.observe(
                this@MainActivity, Observer { event ->
                    event.getExtraIfNotHandled()?.let { bankAccount ->
                        copyStringToClipboardAndShowToast(bankAccount)
                    }
                }
            )
        }
    }

    private fun settingContentView() {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            viewModel = mainViewModel
            lifecycleOwner = this@MainActivity
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

    private fun copyStringToClipboardAndShowToast(string: String) {
        val clipboard =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(string, string)
        clipboard.setPrimaryClip(clip)
        val message = resources.getString(R.string.bank_account_copied)
        Toast.makeText(
            this@MainActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val REQUEST_CAMERA_PICTURE = 0x1002
    }

}
