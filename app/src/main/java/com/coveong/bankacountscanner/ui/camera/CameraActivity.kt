package com.coveong.bankacountscanner.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.coveong.bankacountscanner.R
import kotlinx.android.synthetic.main.activity_camera.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class CameraActivity : AppCompatActivity() {

    private var cameraDevice: CameraDevice? = null
    private lateinit var previewSize: Size
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    private val manager by lazy { getSystemService(Context.CAMERA_SERVICE) as CameraManager }

    private val requestPermission = {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    private fun initializeView() {
        camera_preview.surfaceTextureListener = surfaceTextureListener
        camera_take_picture_button.setOnClickListener { takePicture() }
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
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission.invoke()
            } else {
                manager.openCamera(cameraId, stateCallback, null)
            }
        } catch (e: CameraAccessException) {
            // TODO 에러 처리하기
        }
    }

    private fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
    }

    private fun initializeCameraDeviceAndCreateCameraPreview(cameraDevice: CameraDevice) {
        this.cameraDevice = cameraDevice
        val texture = camera_preview.surfaceTexture!!
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
                        this@CameraActivity.cameraCaptureSession = cameraCaptureSession
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

    private fun takePicture() {
        try {
            val characteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
            val jpegSizes: Array<Size> = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                .getOutputSizes(ImageFormat.JPEG)

            val width = jpegSizes[0].width
            val height = jpegSizes[0].height

            val imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 2)

            val outputSurface = ArrayList<Surface>(2)
            outputSurface.add(imageReader.surface)
            outputSurface.add(Surface(camera_preview.surfaceTexture!!))

            val captureBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(imageReader.surface)

            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            val readerListener = ImageReader.OnImageAvailableListener {
                var image : Image? = null
                try {
                    image = imageReader.acquireLatestImage()

                    val buffer = image!!.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)
                    val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)

                    val rotateMatrix = Matrix()
                    rotateMatrix.postRotate(90F)
                    val rotatedBitmap: Bitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.width, bitmap.height, rotateMatrix, false)

                    val ratioX = rotatedBitmap.width.toFloat() / camera_preview.width.toFloat()
                    val ratioY = rotatedBitmap.height.toFloat() / camera_preview.height.toFloat()

                    val x1: Int = camera_preview_cropped.left
                    val y1: Int = camera_preview_cropped.top

                    val x2: Int = camera_preview_cropped.width
                    val y2: Int = camera_preview_cropped.height

                    val cropStartX = (x1 * ratioX).roundToInt()
                    val cropStartY = (y1 * ratioY).roundToInt()

                    val cropWidthX = (x2 * ratioX).roundToInt()
                    val cropHeightY = (y2 * ratioY).roundToInt()

                    if (cropStartX + cropWidthX > rotatedBitmap.width || cropStartY +
                        cropHeightY > rotatedBitmap.height
                    ) {
                        throw RuntimeException()
                    }
                    val croppedBitmap = Bitmap.createBitmap(
                        rotatedBitmap,
                        cropStartX,
                        cropStartY,
                        cropWidthX,
                        cropHeightY
                    )
                } catch (e: RuntimeException) {
                    // TODO 에러 처리하기
                } finally {
                    image?.close()
                }
            }

            imageReader.setOnImageAvailableListener(readerListener, null)

            val captureListener = object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                    super.onCaptureCompleted(session, request, result)
                    Log.d("leah", "사진 찍힘!!!")
                }
            }

            // outputSurface 에 위에서 만든 captureListener 를 달아, 캡쳐(사진 찍기) 해주고 나서 카메라 미리보기 세션을 재시작한다
            cameraDevice!!.createCaptureSession(outputSurface, object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {}

                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, null)
                    } catch (e: CameraAccessException) {
                        // TODO 에러 처리하기
                    }
                }
            }, null)
        } catch (e: CameraAccessException) {
            // TODO 에러 처리하기
        }
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
            openCamera()
        } else {
            initializeView()
        }
    }

    override fun onPause() {
        closeCamera()
        super.onPause()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 0x1001
    }
}
