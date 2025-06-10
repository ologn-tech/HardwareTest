package tech.ologn.hardwaretest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ologn.hardwaretest.databinding.InfoItemBinding
import tech.ologn.hardwaretest.model.Info

class InformationAdapter(var context: Context, private var data: ArrayList<Info>) :
    RecyclerView.Adapter<InformationAdapter.InfoViewHolder>() {

    class InfoViewHolder(var binding: InfoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val binding = InfoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return InfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.binding.txtIconName.text = data[position].name
        holder.binding.txtInfoValue.text = data[position].value
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
