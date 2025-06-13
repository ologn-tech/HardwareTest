package tech.ologn.hardwaretest.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentVibrationBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class LowBatteryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding =  FragmentVibrationBinding.inflate(inflater,container,false)
        binding.lowBatteryItem.testIcon.setImageResource(R.drawable.ic_battery)
        binding.lowBatteryItem.iconName.text = "Low Battery"
        binding.lowBatteryItem.txtDescription.text = "Click on button to check low battery"
        binding.lowBatteryItem.btnClick.text = "Simulation"
        binding.lowBatteryItem.btnClick.setOnClickListener {
            val exitCode = runShellCommand("dumpsys battery set status 3")
            if (exitCode == 0 ){
                runShellCommand("dumpsys battery set level 17")
                Handler().postDelayed(
                    {
                        (requireActivity() as ActivityTests).alertDialogConfirm("The LEDs behave as expected"
                            ,"The LEDs don't work correctly",requireActivity(), CameraFragment()
                            , {  } ,"Are the LEDs good", TestFragment.ID_LOWBATTERY)
                        runShellCommand("dumpsys battery reset")
                    }, 3000
                )
            } else {
                (requireActivity() as ActivityTests).alertDialogFail(requireActivity(), CameraFragment(),
                    {  },"Your device not Support this feature", featureId = TestFragment.ID_LOWBATTERY)
            }
        }
        binding.lowBatteryItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(CameraFragment())
        }
        return binding.root
    }

    private fun runShellCommand(command: String): Int {
        try {
            var error = false
            val process = Runtime.getRuntime().exec(command)

            val stdInput = BufferedReader(InputStreamReader(process.inputStream))
            val stdError = BufferedReader(InputStreamReader(process.errorStream))

            // Read standard output (if any)
            var line: String?
            while ((stdInput.readLine().also { line = it }) != null) {
                println(line)
            }

            // Read errors (if any)
            while ((stdError.readLine().also { line = it }) != null) {
                System.err.println(line)
                error = true
            }
            if (error)
                return -2

            return process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

}
