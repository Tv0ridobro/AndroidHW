package itmo.android.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Size
import android.view.*
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val myRequestId = 1
    private lateinit var tv: TextureView
    private var executor: Executor = Executors.newSingleThreadExecutor()
    //private var imageCapture: ImageCapture? = null

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                myRequestId
            )
        } else {
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.surface)

    }

    private fun start() {
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY) // MAX_QUALITY
        }.build()
        val imageAnalysisConfig = ImageAnalysisConfig.Builder().apply {
            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE // ACQUIRE_NEXT_IMAGE
            )
        }.build()
        val imageCapture = ImageCapture(imageCaptureConfig)
        val analysis = ImageAnalysis(imageAnalysisConfig)
        val previewConfig = PreviewConfig.Builder()
            .setTargetResolution(Size(tv.width, tv.height))
            .build()
        val preview = Preview(previewConfig)
        preview.setOnPreviewOutputUpdateListener {
            val parent = tv.parent as ViewGroup
            parent.removeView(tv)
            parent.addView(tv, 0)
            tv.surfaceTexture = it.surfaceTexture
        }
        camera_button.setOnClickListener {
            val fileName = System.currentTimeMillis().toString()
            val fileFormat = ".png"
            val imageFile = createTempFile(
                fileName,
                fileFormat,
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
            imageCapture!!.takePicture(imageFile, executor,
                object : ImageCapture.OnImageSavedListener {
                    override fun onError(
                        imageCaptureError: ImageCapture.ImageCaptureError,
                        message: String,
                        exc: Throwable?
                    ) {
                    }

                    override fun onImageSaved(file: File) {}
                })
        }
        CameraX.bindToLifecycle(this, preview, imageCapture, analysis)
    }

    override fun onStop() {
        super.onStop()
        CameraX.unbindAll()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            myRequestId -> {
                if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast
                        .makeText(
                            this@MainActivity,
                            "No permission granted",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                return
            }
        }
    }
}
