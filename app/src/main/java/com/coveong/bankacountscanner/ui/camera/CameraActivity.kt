package com.coveong.bankacountscanner.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.coveong.bankacountscanner.R
import com.coveong.bankacountscanner.custom.CameraHandler
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {

    private lateinit var cameraHandler: CameraHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val requestPermission = {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
        cameraHandler = CameraHandler(
            context = this,
            manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager,
            cameraPreview = camera_preview,
            requirePermission = requestPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showPermissionNotCheckedToastAndFinish()
            }
        }
    }

    private fun showPermissionNotCheckedToastAndFinish() {
        Toast.makeText(
            this,
            getString(R.string.permission_not_checked_toast_label),
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (camera_preview.isAvailable) {
            cameraHandler.openCamera()
        } else {
            cameraHandler.initializeView()
        }
    }

    override fun onPause() {
        cameraHandler.closeCamera()
        super.onPause()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 0x1001
    }
}
