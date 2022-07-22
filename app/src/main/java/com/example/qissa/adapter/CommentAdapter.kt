package com.example.qissa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.R
import com.example.qissa.databinding.CommentsItemLayoutBinding
import com.example.qissa.interfaces.BindableAdapter
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataXXX

class CommentAdapter(
    var context: Context,
    private val listener: OnObjectListInteractionListener<DataXXX>
) : ListAdapter<DataXXX, CommentAdapter.ListViewHolder>(DIFF_CALLBACK),
    BindableAdapter<List<DataXXX>> {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataXXX>() {
            override fun areItemsTheSame(oldItem: DataXXX, newItem: DataXXX): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataXXX, newItem: DataXXX): Boolean {
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

    override fun setItems(data: List<DataXXX>?) {
        submitList(data) {
            data?.apply {
                checkEmptiness()
            }
        }
    }

    private fun checkEmptiness() {
        if (itemCount > 0) {
            listener.hideEmptyView()
        } else {
            listener.showEmptyView()
        }
    }

    class ListViewHolder private constructor(
        var context: Context,
        private val binding: CommentsItemLayoutBinding,
        private val listener: OnObjectListInteractionListener<DataXXX>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataXXX) {
            binding.comment = item
            binding.executePendingBindings()
            if (item.user.profile_pic == String()
            ) {
                Glide.with(context).load(R.drawable.ic_circle_icons_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            } else {
                Glide.with(context).load(item.user.profile_pic)
                    .placeholder(R.drawable.ic_circle_icons_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            }

//            binding.tvUserProfileNameID.text = item.user.name
            val time: String = item.created_at.substring(0, 10)
            binding.tvPostTime.text = time
//            binding.tvCommentsDetails.text = item.comment_text
            binding.textViewClap.setOnClickListener {
                listener.onClick(bindingAdapterPosition, item, true)
            }
            binding.textViewReply.setOnClickListener { }
        }

        companion object {
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: OnObjectListInteractionListener<DataXXX>
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentsItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener)
            }
        }
    }
}
