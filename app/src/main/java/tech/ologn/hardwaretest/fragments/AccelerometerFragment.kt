package tech.ologn.hardwaretest.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R

class AccelerometerFragment: Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private lateinit var textX: TextView
    private lateinit var textY: TextView
    private lateinit var textZ: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_accelerometer, container, false)

        val testIcon: ImageView = root.findViewById(R.id.test_icon)
        val txtDescription: TextView = root.findViewById(R.id.txtDescription)
        val iconName: TextView = root.findViewById(R.id.iconName)
        val btnClick: Button = root.findViewById(R.id.btnClick)
        val btnSkip: Button = root.findViewById(R.id.btnSkip)

        textX = root.findViewById(R.id.textX)
        textY = root.findViewById(R.id.textY)
        textZ = root.findViewById(R.id.textZ)

        testIcon.setImageResource(R.drawable.ic_sensor)
        txtDescription.text = ""
        iconName.text = "Accelerometer"
        btnClick.text = "Confirm"

        btnClick.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogConfirm(
                "The sensor works correctly",
                "The sensor doesn't work correctly",
                requireActivity(),
                GyroScopeFragment(),
                {},
                "Is the sensor good?",
                TestFragment.ID_ACCELEROMETER
            )
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(GyroScopeFragment())
        }

        if (!isSupported()) {
            (requireActivity() as ActivityTests).alertDialogFail(
                requireActivity(),
                GyroScopeFragment(),
                {},
                "Your device not Support this feature",
                featureId = TestFragment.ID_ACCELEROMETER
            )
        }

        return root
    }


    private fun isSupported(): Boolean{
        return accelerometer != null
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            textX.text = "X: %.3f".format(x)
            textY.text = "Y: %.3f".format(y)
            textZ.text = "Z: %.3f".format(z)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

}
