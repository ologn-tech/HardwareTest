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
import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.MainActivity
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentLightBinding

class LightFragment: Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    lateinit var binding : FragmentLightBinding

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
        binding = FragmentLightBinding.inflate(inflater,container,false)

        binding.lightItem.testIcon.setImageResource(R.drawable.ic_sensor)
        binding.lightItem.txtDescription.text = ""
        binding.lightItem.iconName.text = "Light"
        binding.lightItem.btnClick.text = "Confirm"
        binding.lightItem.btnClick.setOnClickListener{
            (requireActivity() as ActivityTests).alertDialogConfirm("The sensor works correctly"
                ,"The sensor doesn't work correctly",requireActivity(),WifiFragment()
                , {  } ,"Is the sensor is good", TestFragment.ID_LIGHT)
        }

        binding.lightItem.btnSkip.setOnClickListener {
            // Restart activity
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        textLightValue = binding.textLightValue

        if (!isSupported())
            (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),WifiFragment(),
                {  },"Your device not Support this feature", featureId = TestFragment.ID_LIGHT)

        return binding.root
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
