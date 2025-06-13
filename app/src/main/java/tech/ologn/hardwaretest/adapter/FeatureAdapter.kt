package tech.ologn.hardwaretest.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.MainActivity
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.TestResultStore
import tech.ologn.hardwaretest.databinding.FeatureItemBinding
import tech.ologn.hardwaretest.model.FeatureResult

class FeatureAdapter(
    private var activity: Activity,
    private var data:List<FeatureResult>
): RecyclerView.Adapter<FeatureAdapter.MyViewHolder>() {

    class MyViewHolder(var binding:FeatureItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FeatureItemBinding.inflate(LayoutInflater.from(activity) , parent ,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.name.text = data[position].name
        holder.binding.icon.setImageResource(data[position].iconRes)
        holder.binding.status.setImageResource(
            if (data[position].message.contains("Passed")) R.drawable.ic_pass
            else if (data[position].message.contains("Not tested")) R.drawable.ic_pending
            else R.drawable.ic_failed
        )
        holder.binding.message.text = data[position].message
        holder.binding.timestamp.text = data[position].timestamp

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

}
