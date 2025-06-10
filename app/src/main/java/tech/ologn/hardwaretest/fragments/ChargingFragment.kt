package tech.ologn.hardwaretest.fragments

import android.os.BatteryManager.BATTERY_STATUS_CHARGING
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.CheckReceiver
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentChargingBinding

class ChargingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentChargingBinding.inflate(inflater,container,false)
        binding.charItem.testIcon.setImageResource(R.drawable.ic_charge)
        binding.charItem.iconName.text = "Charging"
        binding.charItem.txtDescription.text = "Connect the charger to check"
        binding.charItem.btnClick.text = "Check Charging"
        binding.charItem.btnClick.setOnClickListener {
            checkCharging()
        }
        binding.charItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(VibrationFragment())
        }

        return binding.root
    }

    private fun checkCharging(){
        if ( CheckReceiver.isConnectedCharger  && BATTERY_STATUS_CHARGING == 2){
            (requireActivity() as ActivityTests).alertDialogPass(requireActivity(),VibrationFragment()
                ,"The Charging process works correctly", TestFragment.ID_CHARGING)
        }else{
            (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),VibrationFragment()
            ,{checkCharging()},"Make sure the charger is connected",TestFragment.ID_CHARGING )
        }
    }
}
