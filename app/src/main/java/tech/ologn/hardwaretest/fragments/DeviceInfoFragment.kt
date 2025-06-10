package tech.ologn.hardwaretest.fragments


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import tech.ologn.hardwaretest.databinding.FragmentDeviceInfoBinding
import tech.ologn.hardwaretest.adapter.InformationAdapter
import tech.ologn.hardwaretest.model.Info
import java.util.concurrent.TimeUnit

class DeviceInfoFragment : Fragment() {
    lateinit var binding: FragmentDeviceInfoBinding
    private val data = ArrayList<Info>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDeviceInfo()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeviceInfoBinding.inflate(inflater,container,false)

        val adapter = InformationAdapter(requireContext(),data)
        binding.rvDeviceInfo.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDeviceInfo.adapter = adapter

        return binding.root
    }

    private fun getDeviceInfo(){
        data.add(Info("Model: " , Build.MODEL))
        data.add(Info("Id: " , Build.ID))
        data.add(Info("Manufacture: " , Build.MANUFACTURER))
        data.add(Info("Brand: " , Build.BRAND))
        data.add(Info("Type: " , Build.TYPE))
        data.add(Info("User: " , Build.USER))
        data.add(Info("Base: " , Build.VERSION_CODES.BASE.toString()))
        data.add(Info("Incremental: " , Build.VERSION.INCREMENTAL))
        data.add(Info("SDK: " , Build.VERSION.SDK))
        data.add(Info("Board: " , Build.BOARD))
        data.add(Info("Host: " , Build.HOST))
        data.add(Info("Fingerprint: " , Build.FINGERPRINT))
        data.add(Info("Release: " , Build.VERSION.RELEASE))
        data.add(Info("Display: " , Build.DISPLAY))
        data.add(Info("Product: " , Build.PRODUCT))
        data.add(Info("BootLoader: " , Build.BOOTLOADER))
        data.add(Info("Time : " ,TimeUnit.MILLISECONDS.toDays(Build.TIME).toString() ))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            data.add(Info("SOC_Model: " , Build.SOC_MODEL))
        }
    }

}
