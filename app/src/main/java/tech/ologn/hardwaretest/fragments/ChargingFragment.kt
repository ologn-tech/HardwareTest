package tech.ologn.hardwaretest.fragments

import android.os.BatteryManager.BATTERY_STATUS_CHARGING
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.CheckReceiver
import tech.ologn.hardwaretest.R

class ChargingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_charging, container, false)

        val charItem: View = root.findViewById(R.id.charItem)
        val testIcon: ImageView = charItem.findViewById(R.id.test_icon)
        val iconName: TextView = charItem.findViewById(R.id.iconName)
        val txtDescription: TextView = charItem.findViewById(R.id.txtDescription)
        val btnClick: Button = charItem.findViewById(R.id.btnClick)
        val btnSkip: Button = charItem.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_charge)
        iconName.text = "Charging"
        txtDescription.text = "Connect the charger to check"
        btnClick.text = "Check Charging"

        btnClick.setOnClickListener {
            checkCharging()
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(LowBatteryFragment())
        }

        return root
    }

    private fun checkCharging(){
        if ( CheckReceiver.isConnectedCharger  && BATTERY_STATUS_CHARGING == 2){
            (requireActivity() as ActivityTests).alertDialogPass(requireActivity(),LowBatteryFragment()
                ,"The Charging process works correctly", TestFragment.ID_CHARGING)
        }else{
            (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),LowBatteryFragment()
            ,{checkCharging()},"Make sure the charger is connected",TestFragment.ID_CHARGING )
        }
    }
}
