package tech.ologn.hardwaretest.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.MainActivity
import tech.ologn.hardwaretest.TestResultStore
import tech.ologn.hardwaretest.databinding.FeatureItemBinding
import tech.ologn.hardwaretest.model.Feature

class FeatureAdapter(private var activity: Activity, private var data:ArrayList<Feature>):RecyclerView.Adapter<FeatureAdapter.MyViewHolder>() {

    class MyViewHolder(var binding:FeatureItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FeatureItemBinding.inflate(LayoutInflater.from(activity) , parent ,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.txtName.text = data[position].name
        holder.binding.icon.setImageResource(data[position].icon)
        holder.binding.root.setCardBackgroundColor(getRandomColor(position))
        holder.binding.root.setOnClickListener{
            if (TestResultStore.getTesterName(activity.applicationContext).isEmpty()) {
                (activity as MainActivity).showEditNameDialog()
                return@setOnClickListener
            }
            val i = Intent(activity, ActivityTests::class.java)
            i.putExtra("id",data[position].id)
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
       return data.size
    }


    private fun getRandomColor(position: Int):Int{
        when {
            position %5 == 0 -> {
                return Color.rgb(90,151,116)
            }
            position %5 == 1 -> {
                return Color.rgb(90,94,151)
            }
            position %5 == 2 -> {
                return Color.rgb(151,90,125)
            }
            position %5 == 3 -> {
                return Color.rgb(151,147,90)
            }
            position %5 == 4 ->{
                return Color.rgb(151,116,90)
            }
        }
        return Color.rgb(0,0,0)
    }



}
