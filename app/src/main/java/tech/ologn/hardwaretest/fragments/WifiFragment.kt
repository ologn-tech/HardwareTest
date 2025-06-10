package tech.ologn.hardwaretest.fragments


import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentWifiBinding

class WifiFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentWifiBinding.inflate(inflater,container,false)
        binding.item.testIcon.setImageResource(R.drawable.ic_wifi)
        binding.item.iconName.text = "Wifi"
        binding.item.txtDescription.text = "Wifi Connection test is automatically run."
        binding.item.btnClick.text = "Check Wifi Connection"
        checkAndAlert()
        binding.item.btnClick.setOnClickListener {
            checkAndAlert()
        }
        binding.item.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(BluetoothFragment())
        }
        return binding.root
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
