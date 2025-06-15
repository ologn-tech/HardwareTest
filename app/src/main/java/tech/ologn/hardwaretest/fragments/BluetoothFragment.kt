package tech.ologn.hardwaretest.fragments

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R

class BluetoothFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_bluetooth, container, false)

        val blueItem: View = root.findViewById(R.id.blueItem)

        val testIcon: ImageView = blueItem.findViewById(R.id.test_icon)
        val iconName: TextView = blueItem.findViewById(R.id.iconName)
        val txtDescription: TextView = blueItem.findViewById(R.id.txtDescription)
        val btnClick: Button = blueItem.findViewById(R.id.btnClick)
        val btnSkip: Button = blueItem.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_bluetooth)
        iconName.text = "Bluetooth"
        txtDescription.text = "Turn on the Bluetooth Feature to check it"
        btnClick.text = "Check Bluetooth Work"

        btnClick.setOnClickListener {
            checkBluetooth()
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(ChargingFragment())
        }

        return root
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
