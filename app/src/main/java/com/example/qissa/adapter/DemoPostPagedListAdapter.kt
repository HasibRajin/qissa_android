package com.example.qissa.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qissa.databinding.HomeItemLayoutBinding
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataX

class DemoPostPagedListAdapter(
    private val listener: OnObjectListInteractionListener<DataX>
) : PagingDataAdapter<DataX, DemoPostPagedListAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(
                oldItem: DataX,
                newItem: DataX
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataX,
                newItem: DataX
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ListViewHolder private constructor(
        private val binding: HomeItemLayoutBinding,
        private val listener: OnObjectListInteractionListener<DataX>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataX?) {
            item?.also { _item ->
                binding.post = _item
                binding.executePendingBindings()

                binding.root.setBackgroundColor(
                    Color.parseColor("#ffffff")
                )

                binding.root.setOnClickListener {
                    listener.onClick(bindingAdapterPosition, _item, true)
                }

                binding.root.setOnLongClickListener {
                    listener.onLongClick(bindingAdapterPosition, _item)
                    true
                }
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                listener: OnObjectListInteractionListener<DataX>
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(binding, listener)
            }
        }
    }
}
