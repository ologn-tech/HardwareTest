package tech.ologn.hardwaretest.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.MainActivity
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.TestResultStore
import tech.ologn.hardwaretest.model.FeatureResult

class FeatureAdapter(
    private var activity: Activity,
    private var data:List<FeatureResult>
): RecyclerView.Adapter<FeatureAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val status: ImageView = itemView.findViewById(R.id.status)
        val message: TextView = itemView.findViewById(R.id.message)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.feature_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.icon.setImageResource(item.iconRes)
        holder.status.setImageResource(
            when {
                item.message.contains("Passed") -> R.drawable.ic_pass
                item.message.contains("Not tested") -> R.drawable.ic_pending
                else -> R.drawable.ic_failed
            }
        )
        holder.message.text = item.message
        holder.timestamp.text = item.timestamp

        holder.itemView.setOnClickListener {
            if (TestResultStore.getTesterName(activity.applicationContext).isEmpty()) {
                (activity as MainActivity).showEditNameDialog()
                return@setOnClickListener
            }
            val intent = Intent(activity, ActivityTests::class.java)
            intent.putExtra("id", item.id)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = data.size

}
