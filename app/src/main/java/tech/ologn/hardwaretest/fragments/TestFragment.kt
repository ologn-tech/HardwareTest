package tech.ologn.hardwaretest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.TestResultStore
import tech.ologn.hardwaretest.adapter.FeatureAdapter
import tech.ologn.hardwaretest.databinding.FragmentTestBinding
import tech.ologn.hardwaretest.model.Feature
import tech.ologn.hardwaretest.model.FeatureResult

class TestFragment : Fragment() {

    lateinit var binding: FragmentTestBinding

    companion object{
        val ID_WIFI = 0
        val ID_Bluetooth = 1
        val ID_CHARGING = 2
        val ID_LOWBATTERY = 3
        val ID_CAMERA = 4
        val ID_MULTI_CAMERA = 5
        val ID_FLASH = 6
        val ID_MICROPHONE = 7
        val ID_SOUND = 8
        val ID_ACCELEROMETER = 9
        val ID_GYROSCOPE = 10
        val ID_LIGHT = 11
    }

    private val featureList = listOf(
        Feature(ID_WIFI, "Wifi", R.drawable.ic_wifi),
        Feature(ID_Bluetooth, "Bluetooth", R.drawable.ic_bluetooth),
        Feature(ID_CHARGING, "Charging", R.drawable.ic_charge),
        Feature(ID_LOWBATTERY, "Low Battery", R.drawable.ic_battery),
        Feature(ID_CAMERA, "Camera", R.drawable.ic_camera),
        Feature(ID_MULTI_CAMERA, "Multi Camera", R.drawable.ic_camera),
        Feature(ID_FLASH, "Flash", R.drawable.ic_flash),
        Feature(ID_MICROPHONE, "Microphone", R.drawable.ic_microphone),
        Feature(ID_SOUND, "Sound", R.drawable.ic_sound),
        Feature(ID_ACCELEROMETER, "Accelerometer", R.drawable.ic_sensor),
        Feature(ID_LIGHT, "Light", R.drawable.ic_light),
        Feature(ID_GYROSCOPE, "Gyroscope", R.drawable.ic_sensor),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTestBinding.inflate(inflater,container,false)
        binding.rvData.layoutManager = GridLayoutManager(requireContext(),6)

        loadData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val resultList: List<FeatureResult> = featureList.map {
            val message = TestResultStore.getResult(requireContext(), it.id)
            val timestamp = TestResultStore.getTimestamp(requireContext(), it.id)

            FeatureResult(it.id, it.name, it.icon, message!!, timestamp)
        }

        binding.rvData.adapter = FeatureAdapter(requireActivity(), resultList)
    }
}
