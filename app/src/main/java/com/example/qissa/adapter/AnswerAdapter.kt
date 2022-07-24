package com.example.qissa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qissa.databinding.AnswerItemLayoutBinding
import com.example.qissa.interfaces.BindableAdapter
import com.example.qissa.interfaces.SecondaryObjectListener
import com.example.qissa.models.DataXXXXXXXXXXXXXXXX

class AnswerAdapter(
    var context: Context,
    private val listener: SecondaryObjectListener<DataXXXXXXXXXXXXXXXX>
) : ListAdapter<DataXXXXXXXXXXXXXXXX, AnswerAdapter.ListViewHolder>(DIFF_CALLBACK),
    BindableAdapter<List<DataXXXXXXXXXXXXXXXX>> {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataXXXXXXXXXXXXXXXX>() {
            override fun areItemsTheSame(
                oldItem: DataXXXXXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXXXXXX
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataXXXXXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXXXXXX
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(context, parent, listener)
    }
//    Log.d("loadComment", "findComments: getting comments done }")

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun setItems(data: List<DataXXXXXXXXXXXXXXXX>?) {
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
        private val binding: AnswerItemLayoutBinding,
        private val listener: SecondaryObjectListener<DataXXXXXXXXXXXXXXXX>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataXXXXXXXXXXXXXXXX) {
            binding.answer = item
            binding.executePendingBindings()
            binding.textViewClap.text = item.meta.clapsCount
            binding.textViewClap.setOnClickListener {
                //                listener.onSecObjClick(bindingAdapterPosition,item)
                Toast.makeText(context, "clap clicked", Toast.LENGTH_SHORT).show()
            }
        }

        companion object {
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: SecondaryObjectListener<DataXXXXXXXXXXXXXXXX>
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AnswerItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener)
            }
        }
    }
}
