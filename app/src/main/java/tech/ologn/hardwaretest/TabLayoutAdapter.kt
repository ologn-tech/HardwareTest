package tech.ologn.hardwaretest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import tech.ologn.hardwaretest.fragments.ResultFragment
import tech.ologn.hardwaretest.fragments.TestFragment

class TabLayoutAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when(position){
            0 -> fragment = TestFragment()
//            2 -> fragment = DeviceInfoFragment()
            1 -> fragment = ResultFragment()
        }
        return fragment
    }
}
