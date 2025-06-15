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

class GyroScopeFragment: Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null

    lateinit var root: View

    private lateinit var textX: TextView
    private lateinit var textY: TextView
    private lateinit var textZ: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_gyroscope, container, false)

        val gyroItem: View = root.findViewById(R.id.gyroItem)
        val testIcon: ImageView = gyroItem.findViewById(R.id.test_icon)
        val txtDescription: TextView = gyroItem.findViewById(R.id.txtDescription)
        val iconName: TextView = gyroItem.findViewById(R.id.iconName)
        val btnClick: Button = gyroItem.findViewById(R.id.btnClick)
        val btnSkip: Button = gyroItem.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_sensor)
        txtDescription.text = ""
        iconName.text = "Gyroscope"
        btnClick.text = "Confirm"

        btnClick.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogConfirm(
                "The sensor works correctly",
                "The sensor doesn't work correctly",
                requireActivity(),
                LightFragment(),
                { },
                "Is the sensor good?",
                TestFragment.ID_GYROSCOPE
            )
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(LightFragment())
        }

        textX = root.findViewById(R.id.textX)
        textY = root.findViewById(R.id.textY)
        textZ = root.findViewById(R.id.textZ)

        if (!isSupported()) {
            (requireActivity() as ActivityTests).alertDialogFail(
                requireActivity(),
                LightFragment(),
                { },
                "Your device not Support this feature",
                featureId = TestFragment.ID_GYROSCOPE
            )
        }

        return root
    }

    private fun isSupported(): Boolean{
        return gyroscope != null
    }

    override fun onResume() {
        super.onResume()
        gyroscope?.also {
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
