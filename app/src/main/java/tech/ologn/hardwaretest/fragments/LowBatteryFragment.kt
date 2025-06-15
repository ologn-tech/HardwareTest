package tech.ologn.hardwaretest.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import java.io.BufferedReader
import java.io.InputStreamReader

class LowBatteryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_vibration, container, false)

        val lowBatteryItem: View = root.findViewById(R.id.lowBatteryItem)
        val testIcon: ImageView = lowBatteryItem.findViewById(R.id.test_icon)
        val iconName: TextView = lowBatteryItem.findViewById(R.id.iconName)
        val txtDescription: TextView = lowBatteryItem.findViewById(R.id.txtDescription)
        val btnClick: Button = lowBatteryItem.findViewById(R.id.btnClick)
        val btnSkip: Button = lowBatteryItem.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_battery)
        iconName.text = "Low Battery"
        txtDescription.text = "Click on button to check low battery"
        btnClick.text = "Simulation"

        btnClick.setOnClickListener {
            val exitCode = runShellCommand("dumpsys battery set status 3")
            if (exitCode == 0) {
                runShellCommand("dumpsys battery set level 17")
                Handler().postDelayed({
                    (requireActivity() as ActivityTests).alertDialogConfirm(
                        "The LEDs behave as expected",
                        "The LEDs don't work correctly",
                        requireActivity(),
                        CameraFragment(),
                        { },
                        "Are the LEDs good?",
                        TestFragment.ID_LOWBATTERY
                    )
                    runShellCommand("dumpsys battery reset")
                }, 3000)
            } else {
                (requireActivity() as ActivityTests).alertDialogFail(
                    requireActivity(),
                    CameraFragment(),
                    { },
                    "Your device not Support this feature",
                    featureId = TestFragment.ID_LOWBATTERY
                )
            }
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(CameraFragment())
        }

        return root
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
