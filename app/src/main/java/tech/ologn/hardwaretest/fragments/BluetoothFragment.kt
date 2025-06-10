package tech.ologn.hardwaretest.fragments

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentBluetoothBinding

class BluetoothFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentBluetoothBinding.inflate(inflater,container,false)
        binding.blueItem.testIcon.setImageResource(R.drawable.ic_bluetooth)
        binding.blueItem.iconName.text = "Bluetooth"
        binding.blueItem.txtDescription.text = "Turn on the Bluetooth Feature to check it"
        binding.blueItem.btnClick.text = "Check Bluetooth Work"
        binding.blueItem.btnClick.setOnClickListener {
            checkBluetooth()
        }
        binding.blueItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(ChargingFragment())
        }
        return binding.root
    }

    private fun checkBluetooth(){
        val bt = BluetoothAdapter.getDefaultAdapter()
        if (bt == null){
            (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),ChargingFragment(),
                { checkBluetooth() },"Your device not Support this feature", featureId = TestFragment.ID_Bluetooth)
        }else{
            if (bt.isEnabled){
                (requireActivity() as ActivityTests).alertDialogPass(requireActivity(),ChargingFragment()
                ,"The Bluetooth is working correctly", TestFragment.ID_Bluetooth)
            }else{
                (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),ChargingFragment(),
                    { checkBluetooth() },"The Bluetooth is not working correctly", featureId = TestFragment.ID_Bluetooth)
            }
        }
    }

}
