package tech.ologn.hardwaretest.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R

class CameraFragment : Fragment() {
    private val REQ_CAMERA = 100
    lateinit var root :View

    private val cameraManager by lazy {
        requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private  var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null

    private lateinit var textureView: TextureView
    
    private var physicalCamera = 0
    private var cameraNumbers = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_camera, container,false)
        val camItem: View = root.findViewById(R.id.camItem)
        val testIcon: ImageView = camItem.findViewById(R.id.test_icon)
        val iconName: TextView = camItem.findViewById(R.id.iconName)
        val txtDescription: TextView = camItem.findViewById(R.id.txtDescription)
        val btnClick: Button = camItem.findViewById(R.id.btnClick)
        val btnSkip: Button = camItem.findViewById(R.id.btnSkip)

        val fabSwitchCamera: View = root.findViewById(R.id.fabSwitchCamera)
        val btnConfirm: Button = root.findViewById(R.id.btnConfirm)
        textureView = root.findViewById(R.id.textureView)

        testIcon.setImageResource(R.drawable.ic_camera)
        iconName.text = "Camera"
        txtDescription.text = ""
        btnClick.text = "Check camera"

        btnClick.setOnClickListener {
            checkAndOpenCamera()
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(MultiCameraFragment())
        }
        textureView = root.findViewById(R.id.textureView)

        cameraNumbers = cameraManager.cameraIdList.size

        fabSwitchCamera.setOnClickListener{
            if (cameraNumbers > 1)
            {
                physicalCamera++
                if (physicalCamera >= cameraNumbers)
                    physicalCamera = 0
                cameraDevice?.close()
                startCameraPreview(physicalCamera)
            }
        }

        btnConfirm.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogConfirm("The camera works correctly"
                ,"The camera doesn't work correctly",requireActivity(), MultiCameraFragment()
                , { checkAndOpenCamera() } ,"Is the camera image good?", TestFragment.ID_CAMERA)
        }

        return root
    }

    private fun checkAndOpenCamera(){
        if (Build.VERSION.SDK_INT >=23){
            if (requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA) , REQ_CAMERA)
            }else{
                startCameraPreview(physicalCamera)
            }
        }else{
            startCameraPreview(physicalCamera)
        }
    }

    @Throws(CameraAccessException::class)
    private fun startCameraPreview(cameraId : Int) {
        // Ensure TextureViews are ready
        val texture1: SurfaceTexture = textureView.surfaceTexture ?: return

        // Set buffer sizes
        texture1.setDefaultBufferSize(640, 480)

        // Create separate Surface objects
        val surface1 = Surface(texture1)

        // Close old session before creating a new one
        if (cameraCaptureSession != null) {
            cameraCaptureSession!!.close()
            cameraCaptureSession = null
        }

        val captureStateCallback: CameraCaptureSession.StateCallback =
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraCaptureSession = session
                    val captureRequestBuilder: CaptureRequest.Builder
                    try {
                        captureRequestBuilder =
                            session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                        captureRequestBuilder.addTarget(surface1)
                        session.setRepeatingRequest(captureRequestBuilder.build(),
                            null, null
                        )
                    } catch (e: Exception) {
                        showFailDialog()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    showFailDialog()

                }
            }

        try {
            cameraManager.openCamera(cameraId.toString(),
                getMainExecutor(requireContext()),
                object : CameraDevice.StateCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        try {
                            camera.createCaptureSession(listOf(surface1), captureStateCallback, null)
                            root.findViewById<Button>(R.id.btnConfirm).visibility = View.VISIBLE
                            root.findViewById<FloatingActionButton>(R.id.fabSwitchCamera).visibility = if (cameraNumbers > 1) View.VISIBLE else View.GONE
                        } catch (e: Exception) {
                            showFailDialog()
                            throw RuntimeException(e)
                        }
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        camera.close()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        showFailDialog()
                        camera.close()
                    }
                })
        } catch (e: Exception) {
            showFailDialog()
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        if (cameraDevice != null) {
            cameraDevice!!.close()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (cameraDevice != null) {
            cameraDevice!!.close()
        }
    }

    private fun showFailDialog(){
        (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),MultiCameraFragment(),
            {},"Your device not Support this feature", featureId = TestFragment.ID_CAMERA)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startCameraPreview(physicalCamera)
            }else{
                Toast.makeText(requireContext(), "Allow the permission to use this feature", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
