package tech.ologn.hardwaretest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.adapter.FeatureAdapter
import tech.ologn.hardwaretest.databinding.FragmentTestBinding
import tech.ologn.hardwaretest.model.Feature

class TestFragment : Fragment() {
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentTestBinding.inflate(inflater,container,false)
        val data = ArrayList<Feature>()
        data.add(Feature(ID_WIFI,"Wifi", R.drawable.ic_wifi))
        data.add(Feature(ID_Bluetooth,"Bluetooth", R.drawable.ic_bluetooth))
        data.add(Feature(ID_CHARGING,"Charging",R.drawable.ic_charge))
        data.add(Feature(ID_LOWBATTERY,"Low Battery",R.drawable.ic_battery))
        data.add(Feature(ID_CAMERA,"Camera", R.drawable.ic_camera))
        data.add(Feature(ID_MULTI_CAMERA,"Multi Camera", R.drawable.ic_camera))
        data.add(Feature(ID_FLASH,"Flash", R.drawable.ic_flash))
        data.add(Feature(ID_MICROPHONE,"Microphone", R.drawable.ic_microphone))
        data.add(Feature(ID_SOUND,"Sound",R.drawable.ic_sound))
        data.add(Feature(ID_ACCELEROMETER,"Accelerometer",R.drawable.ic_sensor))
        data.add(Feature(ID_GYROSCOPE,"Gyroscope",R.drawable.ic_sensor))
        data.add(Feature(ID_LIGHT,"Light",R.drawable.ic_light))

        binding.rvData.layoutManager = GridLayoutManager(requireContext(),6)

        val featureAdapter = FeatureAdapter(requireActivity(),data)
        binding.rvData.adapter = featureAdapter

        return binding.root
    }
}
