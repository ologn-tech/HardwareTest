package tech.ologn.hardwaretest.fragments

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R

class WifiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_wifi, container, false)

        val testIcon: ImageView = root.findViewById(R.id.test_icon)
        val iconName: TextView = root.findViewById(R.id.iconName)
        val txtDescription: TextView = root.findViewById(R.id.txtDescription)
        val btnClick: Button = root.findViewById(R.id.btnClick)
        val btnSkip: Button = root.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_wifi)
        iconName.text = "Wifi"
        txtDescription.text = "Wifi Connection test is automatically run."
        btnClick.text = "Check Wifi Connection"

        checkAndAlert()

        btnClick.setOnClickListener {
            checkAndAlert()
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(BluetoothFragment())
        }

        return root
    }


    private fun checkWifi(): Boolean {
        var isConnected = false
        val connectivityManager = requireActivity().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            isConnected = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                activeNetworkInfo?.run {
                    isConnected = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }
                }
            }
        }
        return isConnected
    }

    private fun checkAndAlert(){
        if (checkWifi()){
            (requireActivity() as ActivityTests).alertDialogPass(requireActivity(),BluetoothFragment()
                ,"The Wifi is working correctly", featureId = TestFragment.ID_WIFI)
        }else {
            (requireActivity() as ActivityTests).alertDialogFail(requireActivity(),BluetoothFragment()
                , { checkAndAlert() },"The Wifi is not working correctly", featureId = TestFragment.ID_WIFI)
        }
    }


}
