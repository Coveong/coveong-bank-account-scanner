package com.coveong.bankacountscanner.custom

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.core.app.ActivityCompat

class CameraHandler(
    private val context: Context,
    private val manager: CameraManager,
    private val cameraPreview: TextureView,
    private val requirePermission: () -> Unit
) {

    private var cameraDevice: CameraDevice? = null
    private lateinit var previewSize: Size
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    private val surfaceTextureListener: TextureView.SurfaceTextureListener = object :
        TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(
            surface: SurfaceTexture,
            width: Int,
            height: Int
        ) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture,
            width: Int,
            height: Int
        ) {
            // Transform you image captured size according to the surface width and height
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            initializeCameraDeviceAndCreateCameraPreview(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    fun initializeView() {
        cameraPreview.surfaceTextureListener = surfaceTextureListener
    }

    fun openCamera() {
        try {
            val cameraId =  manager.cameraIdList[0]
            val cameraCharacter =
                manager.getCameraCharacteristics(cameraId)
            val map =
                cameraCharacter.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: return
            previewSize = map.getOutputSizes(SurfaceTexture::class.java)[0]

            if (
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requirePermission.invoke()
            } else {
                manager.openCamera(cameraId, stateCallback, null)
            }
        } catch (e: CameraAccessException) {
            // TODO 에러 처리하기
        }
    }

    fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
    }

    private fun initializeCameraDeviceAndCreateCameraPreview(cameraDevice: CameraDevice) {
        this.cameraDevice = cameraDevice
        val texture = cameraPreview.surfaceTexture!!
        texture.setDefaultBufferSize(previewSize.width, previewSize.height)
        val textureViewSurface = Surface(texture)
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                addTarget(textureViewSurface)
                set(
                    CaptureRequest.CONTROL_MODE,
                    CameraMetadata.CONTROL_MODE_AUTO
                )
            }
            cameraDevice.createCaptureSession(
                listOf(textureViewSurface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        this@CameraHandler.cameraCaptureSession = cameraCaptureSession
                        updatePreview()
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        // TODO 에러 처리하기
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            // TODO 에러 처리하기
        }
    }

    private fun updatePreview() {
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null)
        } catch (e: CameraAccessException) {
            // TODO 에러 처리하기
        }
    }
}
