package tech.ologn.hardwaretest

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.databinding.ActivityTestsBinding
import tech.ologn.hardwaretest.databinding.DialogConfirmBinding
import tech.ologn.hardwaretest.databinding.DialogFailBinding
import tech.ologn.hardwaretest.databinding.DialogPassBinding
import tech.ologn.hardwaretest.fragments.TestFragment
import tech.ologn.hardwaretest.fragments.*

class ActivityTests : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when(intent.getIntExtra("id",-1)){
            TestFragment.ID_WIFI -> swipeFragment(WifiFragment())
            TestFragment.ID_Bluetooth -> swipeFragment(BluetoothFragment())
            TestFragment.ID_MICROPHONE -> swipeFragment(MicrophoneFragment())
            TestFragment.ID_CAMERA -> swipeFragment(CameraFragment())
            TestFragment.ID_MULTI_CAMERA -> swipeFragment(MultiCameraFragment())
            TestFragment.ID_LOWBATTERY -> swipeFragment(LowBatteryFragment())
            TestFragment.ID_CHARGING -> swipeFragment(ChargingFragment())
            TestFragment.ID_SOUND -> swipeFragment(SoundFragment())
            TestFragment.ID_FLASH -> swipeFragment(FlashFragment())
            TestFragment.ID_ACCELEROMETER -> swipeFragment(AccelerometerFragment())
            TestFragment.ID_GYROSCOPE -> swipeFragment(GyroScopeFragment())
            TestFragment.ID_LIGHT -> swipeFragment(LightFragment())
        }
    }

    fun swipeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container,fragment)
            .commit()
    }

    fun alertDialogPass(activity: Activity,fragment: Fragment? =null,txtDesc:String, featureId: Int){
        val dialogBinding = DialogPassBinding.inflate(layoutInflater)
        val dialog = Dialog(activity)
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogBinding.txtDesc.text = txtDesc
        dialogBinding.btnNext.setOnClickListener {
            if (fragment == null) {
                activity.finish()
                return@setOnClickListener
            }
            swipeFragment(fragment)
            dialog.dismiss()
        }
        TestResultStore.saveResult(
            context = this,
            featureId = featureId,
            message = "Passed"
        )
        dialog.show()
    }

    fun alertDialogFail(activity: Activity , fragmentTo: Fragment? =null,function: () -> (Unit) ,txtDesc: String, featureId: Int){
        val dialogBinding = DialogFailBinding.inflate(layoutInflater)
        val dialog = Dialog(activity)
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogBinding.txtDecription.text = txtDesc
        dialogBinding.btnNextTest.setOnClickListener {
            if (fragmentTo == null) {
                activity.finish()
                return@setOnClickListener
            }
            swipeFragment(fragmentTo)
            dialog.dismiss()
        }
        dialogBinding.btnRetry.setOnClickListener {
            function()
            dialog.dismiss()
        }
        TestResultStore.saveResult(
            context = this,
            featureId = featureId,
            message = "Failed"
        )
        dialog.show()
    }
    fun alertDialogConfirm(questionPass:String ,questionFail: String, activity: Activity, fragmentTo: Fragment? =null,function: () -> (Unit), textConfirm:String, featureId: Int){
        val dialogConfirmBinding = DialogConfirmBinding.inflate(layoutInflater)
        val dialog = Dialog(activity)
        dialog.setContentView(dialogConfirmBinding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogConfirmBinding.txtQuestion.text = textConfirm
        dialogConfirmBinding.btnYes.setOnClickListener {
            alertDialogPass(activity,fragmentTo,questionPass, featureId =  featureId)
            dialog.dismiss()
        }
        dialogConfirmBinding.btnNo.setOnClickListener {
            alertDialogFail(activity,fragmentTo,function,questionFail, featureId = featureId)
            dialog.dismiss()
        }
        dialog.show()
    }

}
