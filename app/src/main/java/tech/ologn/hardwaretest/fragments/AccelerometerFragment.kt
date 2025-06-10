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
import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentAccelerometerBinding

class AccelerometerFragment: Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    lateinit var binding : FragmentAccelerometerBinding

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
        binding = FragmentAccelerometerBinding.inflate(inflater,container,false)

        binding.accelItem.testIcon.setImageResource(R.drawable.ic_sensor)
        binding.accelItem.txtDescription.text = ""
        binding.accelItem.iconName.text = "Accelerometer"
        binding.accelItem.btnClick.text = "Confirm"
        binding.accelItem.btnClick.setOnClickListener{
            (requireActivity() as ActivityTests).alertDialogConfirm("The sensor works correctly"
                ,"The sensor doesn't work correctly",requireActivity(),GyroScopeFragment()
                , {  } ,"Is the sensor is good", TestFragment.ID_ACCELEROMETER)
        }


        binding.accelItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(GyroScopeFragment())
        }

        textX = binding.textX
        textY = binding.textY
        textZ = binding.textZ

        if (!isSupported())
            (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),GyroScopeFragment(),
                {  },"Your device not Support this feature", featureId = TestFragment.ID_ACCELEROMETER)

        return binding.root
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
