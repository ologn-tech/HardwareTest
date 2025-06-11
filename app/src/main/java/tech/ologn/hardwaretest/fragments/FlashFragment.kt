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
import android.widget.Toast
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.DialogConfirmBinding

import tech.ologn.hardwaretest.databinding.FragmentFlashBinding

class FlashFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFlashBinding.inflate(inflater,container,false)
        binding.flashItem.testIcon.setImageResource(R.drawable.ic_flash)
        binding.flashItem.iconName.text = "Flash"
        binding.flashItem.txtDescription.text = "Click the button below to turn on the flash"
        binding.flashItem.btnClick.text = "Flash"
        binding.flashItem.btnClick.setOnClickListener {
                flashOnAndConfirm()
        }
        binding.flashItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(MicrophoneFragment())
        }
        return binding.root
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

    private fun alertFlashConfirm(questionPass:String, questionFail: String, activity: Activity, fragmentTo: Fragment, function: () -> (Unit), textConfirm:String){
        val dialogConfirmBinding = DialogConfirmBinding.inflate(layoutInflater)
        val dialog = Dialog(activity)
        dialog.setContentView(dialogConfirmBinding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogConfirmBinding.txtQuestion.text = textConfirm
        dialogConfirmBinding.btnYes.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogPass(activity,fragmentTo,questionPass, featureId = TestFragment.ID_FLASH)
            flashOff()
            dialog.dismiss()
        }
        dialogConfirmBinding.btnNo.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogFail(activity,fragmentTo,function,questionFail, featureId = TestFragment.ID_FLASH)
            flashOff()
            dialog.dismiss()
        }
        dialog.show()
    }
}
