package com.coveong.bankacountscanner

import com.coveong.bankacountscanner.ui.camera.CameraActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (preview_imageView.background == null) {
            // TODO: startActivityForResult로 변경해서 사진을 받아와야 함
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}
