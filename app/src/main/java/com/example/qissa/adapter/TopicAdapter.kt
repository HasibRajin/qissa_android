package com.example.qissa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qissa.databinding.TopicItemButtonLayoutBinding
import com.example.qissa.interfaces.BindableAdapter
import com.example.qissa.interfaces.SecondaryObjectListener
import com.example.qissa.models.DataXXXXXXXXXXXX

class TopicAdapter(
    var context: Context,
    private val listener: SecondaryObjectListener<DataXXXXXXXXXXXX>
) : ListAdapter<DataXXXXXXXXXXXX, TopicAdapter.ListViewHolder>(DIFF_CALLBACK),
    BindableAdapter<List<DataXXXXXXXXXXXX>> {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataXXXXXXXXXXXX>() {
            override fun areItemsTheSame(
                oldItem: DataXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXX
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXX
            ): Boolean {
                return oldItem == newItem
            }
        }
        var rowIndex = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(context, parent, listener)
    }
//    Log.d("loadComment", "findComments: getting comments done }")

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun setItems(data: List<DataXXXXXXXXXXXX>?) {
        submitList(data) {
            data?.apply {
                checkEmptiness()
            }
        }
    }

    private fun checkEmptiness() {
        if (itemCount > 0) {
            listener.hideSecObjEmptyView()
        } else {
            listener.showSecObjEmptyView()
        }
    }

    class ListViewHolder private constructor(
        var context: Context,
        private val binding: TopicItemButtonLayoutBinding,
        private val listener: SecondaryObjectListener<DataXXXXXXXXXXXX>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataXXXXXXXXXXXX) {
//            if(bindingAdapterPosition ==0){
//                binding.tvTopicName.text ="All"
//            }
            binding.topic = item
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                listener.onSecObjClick(bindingAdapterPosition, item)
                rowIndex = bindingAdapterPosition
            }
//            if (rowIndex == bindingAdapterPosition) {
//                binding.tvTopicName.setBackgroundResource(R.drawable.follow_button_bg)
//                binding.tvTopicName.setTextColor(Color.parseColor("#664F5D73"))
//            } else {
//                binding.tvTopicName.setBackgroundResource(R.drawable.follow_button_blue_bg)
//                binding.tvTopicName.setTextColor(Color.parseColor("#FFFFFF"))
//
//            }
        }

        companion object {
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: SecondaryObjectListener<DataXXXXXXXXXXXX>
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopicItemButtonLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener)
            }
        }
    }
}
