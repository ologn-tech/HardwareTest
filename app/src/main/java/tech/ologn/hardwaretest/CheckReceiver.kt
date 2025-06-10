package tech.ologn.hardwaretest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CheckReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            Intent.ACTION_POWER_CONNECTED -> {
                CheckReceiver.Companion.isConnectedCharger = true
            }
            Intent.ACTION_POWER_DISCONNECTED ->{
                CheckReceiver.Companion.isConnectedCharger = false
            }
            Intent.ACTION_HEADSET_PLUG ->{
                CheckReceiver.Companion.isConnectedHead =  intent.getIntExtra("state",0) == 1
            }
        }
    }
    companion object{
        var isConnectedHead:Boolean = false
        var  isConnectedCharger = false
        fun getStatusOfHeadset():Boolean{
            return CheckReceiver.Companion.isConnectedHead
        }
        fun getPowerConnected():Boolean{
            return CheckReceiver.Companion.isConnectedCharger
        }
    }

}
