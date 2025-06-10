package tech.ologn.hardwaretest.fragments

import android.content.Context
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentVibrationBinding

class VibrationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding =  FragmentVibrationBinding.inflate(inflater,container,false)
        binding.vibItem.testIcon.setImageResource(R.drawable.ic_vibration)
        binding.vibItem.iconName.text = "Vibration"
        binding.vibItem.txtDescription.text = "Click on button to check vibration"
        binding.vibItem.btnClick.text = "Check Vibration"
        binding.vibItem.btnClick.setOnClickListener {
            vibrate()
        }
        binding.vibItem.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(CameraFragment())
        }
        return binding.root
    }

    private fun vibrate(){
        val v = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >=26){
            v.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
        }else {
            v.vibrate(1500)
        }
        Handler().postDelayed(
            {(requireActivity() as ActivityTests).alertDialogConfirm("Vibration works well",
                "Vibration doesn't work well"
                ,requireActivity(),CameraFragment() , { vibrate() } ,"Were you able to feel the device vibrate", featureId = TestFragment.ID_VIBRATION)
            },
            1000
        )
    }

}
