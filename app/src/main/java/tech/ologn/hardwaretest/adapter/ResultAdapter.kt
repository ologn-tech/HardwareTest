package tech.ologn.hardwaretest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.ItemResultBinding
import tech.ologn.hardwaretest.model.FeatureResult

class ResultAdapter(private val results: List<FeatureResult>) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: FeatureResult) {
            binding.icon.setImageResource(result.iconRes)
            binding.name.text = result.name
            binding.status.setImageResource(
                if (result.message.contains("Passed")) R.drawable.ic_pass
                else if (result.message.contains("Not tested")) R.drawable.ic_pending
                else R.drawable.ic_failed
            )
            binding.message.text = result.message
            binding.timestamp.text = "Time: ${result.timestamp}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size
}
