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
import tech.ologn.hardwaretest.databinding.FragmentGyroscopeBinding

class GyroScopeFragment: Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null

    lateinit var binding : FragmentGyroscopeBinding

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
        binding = FragmentGyroscopeBinding.inflate(inflater,container,false)

        binding.gyroItem.testIcon.setImageResource(R.drawable.ic_sensor)
        binding.gyroItem.txtDescription.text = ""
        binding.gyroItem.iconName.text = "Gyroscope"
        binding.gyroItem.btnClick.text = "Confirm"
        binding.gyroItem.btnClick.setOnClickListener{
            (requireActivity() as ActivityTests).alertDialogConfirm("The sensor works correctly"
                ,"The sensor doesn't work correctly",requireActivity(),LightFragment()
                , {  } ,"Is the sensor good?", TestFragment.ID_GYROSCOPE)
        }

        binding.gyroItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(LightFragment())
        }

        textX = binding.textX
        textY = binding.textY
        textZ = binding.textZ

        if (!isSupported())
            (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),LightFragment(),
                {  },"Your device not Support this feature", featureId = TestFragment.ID_GYROSCOPE)

        return binding.root
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
