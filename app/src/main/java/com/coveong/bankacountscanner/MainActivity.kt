package com.coveong.bankacountscanner

import com.coveong.bankacountscanner.ui.camera.CameraActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.pm.PackageManager
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
        settingPermissionForTest()
        settingMainViewModel()
        callApiForTest()
        settingContentView()

        if (preview_imageView.background == null) {
            // TODO: startActivityForResult로 변경해서 사진을 받아와야 함
            startActivity(Intent(this, CameraActivity::class.java))
        }

    }

    private fun settingPermissionForTest() { // FIXME: 테스트 할 시에 삭제해야하는 메소드
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
        }
    }

    private fun settingMainViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java).apply {
            initializeRetrofit()
        }
    }

    private fun callApiForTest() { // FIXME: 테스트 할 시에 삭제해야하는 메소드
        mainViewModel.getAccountInfo()
    }

    private fun settingContentView() {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            viewModel = mainViewModel
        }
    }


}
