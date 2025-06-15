package tech.ologn.hardwaretest.fragments


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R

class FlashFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_flash, container, false)

        val flashItem: View = root.findViewById(R.id.flashItem)
        val testIcon: ImageView = flashItem.findViewById(R.id.test_icon)
        val iconName: TextView = flashItem.findViewById(R.id.iconName)
        val txtDescription: TextView = flashItem.findViewById(R.id.txtDescription)
        val btnClick: Button = flashItem.findViewById(R.id.btnClick)
        val btnSkip: Button = flashItem.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_flash)
        iconName.text = "Flash"
        txtDescription.text = "Click the button below to turn on the flash"
        btnClick.text = "Flash"

        btnClick.setOnClickListener {
            flashOnAndConfirm()
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(MicrophoneFragment())
        }

        return root
    }

    private fun flashOn(){
        val hasCameraFlash = requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (hasCameraFlash){
            val cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                val cameraId = cameraManager.cameraIdList[0]
                cameraManager.setTorchMode(cameraId, true)
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Your device not supported", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "Your device not supported", Toast.LENGTH_SHORT).show()
        }
    }

    private fun flashOff(){
        val hasCameraFlash = requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (hasCameraFlash){
            val cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                val cameraId = cameraManager.cameraIdList[0]
                cameraManager.setTorchMode(cameraId, false)
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Your device not supported", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "Your device not supported", Toast.LENGTH_SHORT).show()
        }
    }
    private fun flashOnAndConfirm(){
        flashOn()
        Handler().postDelayed(
            {alertFlashConfirm("Flash is working correctly"
                ,"Flash isn't working correctly",requireActivity(),MicrophoneFragment()
                , { flashOnAndConfirm() },"Are you able to see flash light ?")
            },1000
        )
    }

    private fun alertFlashConfirm(
        questionPass: String,
        questionFail: String,
        activity: Activity,
        fragmentTo: Fragment,
        function: () -> Unit,
        textConfirm: String
    ) {
        val dialog = Dialog(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm, null)
        dialog.setContentView(view)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val txtQuestion: TextView = view.findViewById(R.id.txtQuestion)
        val btnYes: Button = view.findViewById(R.id.btnYes)
        val btnNo: Button = view.findViewById(R.id.btnNo)

        txtQuestion.text = textConfirm

        btnYes.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogPass(activity, fragmentTo, questionPass, featureId = TestFragment.ID_FLASH)
            flashOff()
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogFail(activity, fragmentTo, function, questionFail, featureId = TestFragment.ID_FLASH)
            flashOff()
            dialog.dismiss()
        }

        dialog.show()
    }
}
