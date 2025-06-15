package tech.ologn.hardwaretest

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.fragments.TestFragment
import tech.ologn.hardwaretest.fragments.*

class ActivityTests : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tests)

        when (intent.getIntExtra("id", -1)) {
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

    fun swipeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    fun alertDialogPass(activity: Activity, fragment: Fragment? = null, txtDesc: String, featureId: Int) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_pass)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val txtDescView = dialog.findViewById<TextView>(R.id.txtDesc)
        val btnNext = dialog.findViewById<Button>(R.id.btnNext)
        txtDescView.text = txtDesc
        btnNext.setOnClickListener {
            fragment?.let { swipeFragment(it) } ?: activity.finish()
            dialog.dismiss()
        }

        TestResultStore.saveResult(context = this, featureId = featureId, message = "Passed")
        dialog.show()
    }

    fun alertDialogFail(activity: Activity, fragmentTo: Fragment? = null, function: () -> Unit, txtDesc: String, featureId: Int) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_fail)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val txtDescView = dialog.findViewById<TextView>(R.id.txtDecription)
        val btnNextTest = dialog.findViewById<Button>(R.id.btnNextTest)
        val btnRetry = dialog.findViewById<Button>(R.id.btnRetry)
        txtDescView.text = txtDesc

        btnNextTest.setOnClickListener {
            fragmentTo?.let { swipeFragment(it) } ?: activity.finish()
            dialog.dismiss()
        }

        btnRetry.setOnClickListener {
            function()
            dialog.dismiss()
        }

        TestResultStore.saveResult(context = this, featureId = featureId, message = "Failed")
        dialog.show()
    }

    fun alertDialogConfirm(
        questionPass: String,
        questionFail: String,
        activity: Activity,
        fragmentTo: Fragment? = null,
        function: () -> Unit,
        textConfirm: String,
        featureId: Int
    ) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val txtQuestion = dialog.findViewById<TextView>(R.id.txtQuestion)
        val btnYes = dialog.findViewById<Button>(R.id.btnYes)
        val btnNo = dialog.findViewById<Button>(R.id.btnNo)
        txtQuestion.text = textConfirm

        btnYes.setOnClickListener {
            alertDialogPass(activity, fragmentTo, questionPass, featureId)
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            alertDialogFail(activity, fragmentTo, function, questionFail, featureId)
            dialog.dismiss()
        }

        dialog.show()
    }
}

