package com.coveong.bankacountscanner

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.coveong.bankacountscanner.databinding.ActivityMainBinding
import com.coveong.bankacountscanner.error.ExternalAppNotInstalledException
import com.coveong.bankacountscanner.ui.MainViewModel
import com.coveong.bankacountscanner.ui.camera.CameraActivity
import com.coveong.bankacountscanner.ui.camera.CameraRepository
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPreviewImage()
        setContentView(R.layout.activity_main)
        initializeMainViewModel()
        settingContentView()
        initializeViewListener()
        initializeProgressDialog()
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
            isLoading.observe(
                this@MainActivity, Observer<Boolean> { isLoading ->
                    if (isLoading) {
                        progressDialog.show()
                    } else {
                        progressDialog.dismiss()
                    }
                }
            )
            onClickCopyAccountInfo.observe(
                this@MainActivity, Observer { event ->
                    event.getExtraIfNotHandled()?.let { accountInfoString ->
                        copyStringToClipboardAndShowToast(accountInfoString)
                    }
                }
            )
            onClickShareIcon.observe(
                this@MainActivity, Observer { event ->
                    event.getExtraIfNotHandled()?.let { accountInfoString ->
                        openShareDialog(accountInfoString)
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

    private fun initializeViewListener() {
        recapture_button.setOnClickListener {
            requestPreviewImage()
        }
        kakaobank_logo_imageView.setOnClickListener {
            launchExternalApp(R.string.kakaobank_package_name)
        }
        toss_logo_imageView.setOnClickListener {
            launchExternalApp(R.string.toss_package_name)
        }
    }

    private fun initializeProgressDialog() {
        progressDialog = Dialog(this).apply {
            setContentView(R.layout.dialog_progress)
            setCancelable(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CAMERA_PICTURE -> when (resultCode) {
                CameraActivity.RESULT_CAMERA_IMAGE_RECEIVED -> {
                    val image = CameraRepository.takenPicture
                    image?.let {
                        setCameraImagePreview(it)
                        mainViewModel.getAccountInfo(it)
                        CameraRepository.takenPicture = null
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

    private fun launchExternalApp(packageNameId: Int) {
        val intent = packageManager.getLaunchIntentForPackage(getString(packageNameId))
        if (intent == null) {
            throw ExternalAppNotInstalledException("앱이 설치되지 않았습니다.") // FIXME: 에러 메세지 변경
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun openShareDialog(textToShare: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {
        private const val REQUEST_CAMERA_PICTURE = 0x1002
    }

}
