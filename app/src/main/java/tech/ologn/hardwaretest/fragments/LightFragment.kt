package tech.ologn.hardwaretest.fragments

import android.content.Context
import android.content.Intent
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
import tech.ologn.hardwaretest.MainActivity
import tech.ologn.hardwaretest.R

class LightFragment: Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    lateinit var root : View

    private lateinit var textLightValue: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_light, container, false)

        val lightItem: View = root.findViewById(R.id.lightItem)
        val testIcon: ImageView = lightItem.findViewById(R.id.test_icon)
        val txtDescription: TextView = lightItem.findViewById(R.id.txtDescription)
        val iconName: TextView = lightItem.findViewById(R.id.iconName)
        val btnClick: Button = lightItem.findViewById(R.id.btnClick)
        val btnSkip: Button = lightItem.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_sensor)
        txtDescription.text = ""
        iconName.text = "Light"
        btnClick.text = "Confirm"

        btnClick.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogConfirm(
                "The sensor works correctly",
                "The sensor doesn't work correctly",
                requireActivity(),
                null,
                { },
                "Is the sensor good?",
                TestFragment.ID_LIGHT
            )
        }

        btnSkip.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        textLightValue = root.findViewById(R.id.textLightValue)

        if (!isSupported()) {
            (requireActivity() as ActivityTests).alertDialogFail(
                requireActivity(),
                null,
                { },
                "Your device not Support this feature",
                featureId = TestFragment.ID_LIGHT
            )
        }

        return root
    }

    private fun isSupported(): Boolean{
        return lightSensor != null
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val lux = it.values[0]
            textLightValue.text = "Light: %.3f lx".format(lux)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

}
