package tech.ologn.hardwaretest.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentMultiCameraBinding

class MultiCameraFragment : Fragment() {
    private val REQ_CAMERA = 100
    lateinit var binding :FragmentMultiCameraBinding

    private val cameraManager by lazy {
        requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private  var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null

    private lateinit var textureView1: TextureView
    private lateinit var textureView2: TextureView

    private var physicalCameraId1: String? = null
    private var physicalCameraId2: String? = null

    private val backgroundHandler: Handler? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMultiCameraBinding.inflate(inflater,container,false)
        binding.multiCamItem.testIcon.setImageResource(R.drawable.ic_camera)
        binding.multiCamItem.iconName.text = "Multi Camera"
        binding.multiCamItem.txtDescription.text = "Please click buttton to check your camera"
        binding.multiCamItem.btnClick.text = "Check camera"
        binding.multiCamItem.btnClick.setOnClickListener {
            checkAndOpenCamera()
        }
        binding.multiCamItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(FlashFragment())
        }

        textureView1 = binding.textureView1
        textureView2 = binding.textureView2

        binding.btnConfirm.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogConfirm("The camera works correctly"
                ,"The camera doesn't work correctly",requireActivity(),FlashFragment()
                , { checkAndOpenCamera() } ,"Is the camera image is good", TestFragment.ID_MULTI_CAMERA)
        }

        return binding.root
    }

    private fun openCamera(
        logicalCameraId : String,
        sessionConfig: SessionConfiguration,
    ) {
        try {
            cameraManager.openCamera(logicalCameraId,
                getMainExecutor(requireContext()),
                object : CameraDevice.StateCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        try {
                            // Check if session configuration is supported
                            if (cameraDevice!!.isSessionConfigurationSupported(sessionConfig)) {
                                println( "Session configuration is supported for $logicalCameraId.")
                                camera.createCaptureSession(sessionConfig)
                            } else {
                                println( "Session configuration is NOT supported for $logicalCameraId..")
                            }
                        } catch (e: CameraAccessException) {
                            println( "Config $logicalCameraId $e")
                        }
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        println( "Camera onDisconnected")
                        camera.close()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        println( "Camera onError $error")
                        camera.close()
                        cameraDevice = null
                    }
                })
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    @Throws(CameraAccessException::class)
    private fun startCameraPreview(cameraId : String, enabledRun :Boolean, callback: (Boolean, String?) -> Unit) {
        // Ensure TextureViews are ready
        val texture1: SurfaceTexture? = textureView1.surfaceTexture
        val texture2: SurfaceTexture? = textureView2.surfaceTexture

        if (texture1 == null || texture2 == null) {
            println( "TextureViews are not ready!")
            return
        }

        // Set buffer sizes
        texture1.setDefaultBufferSize(640, 480)
        texture2.setDefaultBufferSize(640, 480)

        // Create separate Surface objects
        val surface1 = Surface(texture1)
        val surface2 = Surface(texture2)

        // Close old session before creating a new one
        if (cameraCaptureSession != null) {
            cameraCaptureSession!!.close()
            cameraCaptureSession = null
        }

        // Unique output configurations
        val outputConfigs: MutableList<OutputConfiguration> = ArrayList()

        // Assign separate physical cameras if available
        if (physicalCameraId1 != null) {
            val config1 = OutputConfiguration(surface1)
            config1.setPhysicalCameraId(physicalCameraId1)
            outputConfigs.add(config1)
        }

        if (physicalCameraId2 != null) {
            val config2 = OutputConfiguration(surface2)
            config2.setPhysicalCameraId(physicalCameraId2)
            outputConfigs.add(config2)
        }

        val captureStateCallback: CameraCaptureSession.StateCallback =
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraCaptureSession = session
                    val captureRequestBuilder: CaptureRequest.Builder
                    println( "Camera Configuration Done")
                    try {
                        captureRequestBuilder =
                            session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                        captureRequestBuilder.addTarget(surface1)
                        captureRequestBuilder.addTarget(surface2)
                        session.setRepeatingRequest(captureRequestBuilder.build(),
                            object : CaptureCallback() {
                                override fun onCaptureCompleted(
                                    session: CameraCaptureSession,
                                    request: CaptureRequest,
                                    result: TotalCaptureResult
                                ) {
                                    super.onCaptureCompleted(session, request, result)
                                }

                                override fun onCaptureFailed(
                                    session: CameraCaptureSession,
                                    request: CaptureRequest,
                                    failure: CaptureFailure
                                ) {
                                    super.onCaptureFailed(session, request, failure)
                                }
                            }, backgroundHandler
                        )
                    } catch (e: CameraAccessException) {
                        println( "Failed to start preview $e")
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    println( "Camera Configuration Failed")
                }
            }

        // Create session configuration
        val sessionConfig = SessionConfiguration(
            SessionConfiguration.SESSION_REGULAR,
            outputConfigs,
            getMainExecutor(requireContext()),
            captureStateCallback
        )
        if (enabledRun)
        {
            openCamera(cameraId,sessionConfig)
            callback(true,cameraId)
            return
        }

        try {
            cameraManager.openCamera(cameraId,
                getMainExecutor(requireContext()),
                object : CameraDevice.StateCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        try {
                            // Check if session configuration is supported
                            if (cameraDevice!!.isSessionConfigurationSupported(sessionConfig)) {
//                                println( "Session configuration is supported for $cameraId.")
                                callback(true, cameraId)
                            } else {
//                                println( "Session configuration is NOT supported for $cameraId..")
                            }
                        } catch (e: Exception) {
                            callback(false,null)
//                            println( "Config $cameraId $e")
                        }
                    }

                    override fun onDisconnected(camera: CameraDevice) {
//                        println( "Camera onDisconnected")
                        callback(false,null)
                        camera.close()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
//                        println( "Camera onError $error")
                        callback(false,null)
                        camera.close()
                        cameraDevice = null
                    }
                })
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        callback(false,null)

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraDevice?.close()
    }

    private fun checkAndOpenCamera(){
        if (Build.VERSION.SDK_INT >=23){
            if (requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA) , REQ_CAMERA)
            }else{
                checkMultiCameraEnabled()
            }
        }else{
            checkMultiCameraEnabled()
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkMultiCameraEnabled() : Boolean{
        binding.multiCamItem.btnClick.isEnabled = false
        binding.multiCamItem.btnSkip.isEnabled = false
        val cameraList = cameraManager.cameraIdList
        if (cameraList.size<2)
            return false
        var okfdgdfg = false
        var multiCamera = 11
        for (index in 2..10)
            try {
                val characteristics = cameraManager.getCameraCharacteristics(index.toString())
                val capabilities =
                    characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                if (capabilities != null && capabilities.contains(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA))
                    startCameraPreview(cameraId = index.toString(), enabledRun = false) { success, cameraId ->
                        if (success && cameraId != null) {
                            val physicalCameras = characteristics.physicalCameraIds
                            physicalCameraId1 = physicalCameras.first()
                            physicalCameraId2 = physicalCameras.last()
                            okfdgdfg = true
                            if (cameraId.toInt()  < multiCamera)
                                multiCamera = cameraId.toInt()
                        }
                        cameraDevice?.close()
                    }
            } catch (_:Exception){}
        Handler(Looper.getMainLooper()).postDelayed({
            cameraDevice?.close()
            if (okfdgdfg)
                startCameraPreview(multiCamera.toString(), enabledRun = true) {
                        status, _ ->
                    if (status){
                        binding.btnConfirm.visibility = View.VISIBLE
                    }
                        }
            else{
                Handler().postDelayed(
                    {(requireActivity() as ActivityTests).alertDialogFail(requireActivity(),MultiCameraFragment(),
                        {binding.multiCamItem.btnClick.isEnabled = true },"Your device not Support this feature", featureId = TestFragment.ID_MULTI_CAMERA)
                    }
                    , 500
                )
            }
            binding.multiCamItem.btnSkip.isEnabled = true
        },5000)
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                checkMultiCameraEnabled()
            }else{
                Toast.makeText(requireContext(), "Allow the permission to use this feature", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
