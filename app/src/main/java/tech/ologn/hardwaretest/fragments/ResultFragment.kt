package tech.ologn.hardwaretest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentResultBinding
import tech.ologn.hardwaretest.TestResultStore
import tech.ologn.hardwaretest.adapter.ResultAdapter
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_ACCELEROMETER
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_Bluetooth
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_CAMERA
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_CHARGING
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_FLASH
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_GYROSCOPE
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_HEADPHONE
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_LIGHT
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_MULTI_CAMERA
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_SOUND
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_VIBRATION
import tech.ologn.hardwaretest.fragments.TestFragment.Companion.ID_WIFI
import tech.ologn.hardwaretest.model.Feature
import tech.ologn.hardwaretest.model.FeatureResult

class ResultFragment: Fragment() {
    lateinit var binding: FragmentResultBinding

    private val featureList = listOf(
        Feature(ID_WIFI, "Wifi", R.drawable.ic_wifi),
        Feature(ID_Bluetooth, "Bluetooth", R.drawable.ic_bluetooth),
        Feature(ID_CHARGING, "Charging", R.drawable.ic_charge),
        Feature(ID_VIBRATION, "Vibration", R.drawable.ic_vibration),
        Feature(ID_CAMERA, "Camera", R.drawable.ic_camera),
        Feature(ID_MULTI_CAMERA, "Multi Camera", R.drawable.ic_camera),
        Feature(ID_FLASH, "Flash", R.drawable.ic_flash),
        Feature(ID_HEADPHONE, "Headphone", R.drawable.ic_headphones),
        Feature(ID_SOUND, "Sound", R.drawable.ic_sound),
        Feature(ID_ACCELEROMETER, "Accelerometer", R.drawable.ic_sensor),
        Feature(ID_LIGHT, "Light", R.drawable.ic_light),
        Feature(ID_GYROSCOPE, "Gyroscope", R.drawable.ic_sensor),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)

        binding.rvResults.layoutManager = GridLayoutManager(requireContext(), 3)
        loadData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData(){
        val resultList : List<FeatureResult> = featureList.map {
            val message = TestResultStore.getResult(requireContext(), it.id)
            FeatureResult(it.id, it.name, it.icon, message!!)
        }

        binding.rvResults.adapter = ResultAdapter(resultList )
    }

}
