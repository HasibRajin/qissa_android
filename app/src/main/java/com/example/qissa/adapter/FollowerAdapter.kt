package com.example.qissa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.R
import com.example.qissa.databinding.FollowerItemLayoutBinding
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataXXXXXXXXX

class FollowerAdapter(
    val context: Context,
    private val listener: OnObjectListInteractionListener<DataXXXXXXXXX>
) : PagingDataAdapter<DataXXXXXXXXX, FollowerAdapter.ListViewHolder>(DIFF_CALLBACK) {
    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataXXXXXXXXX>() {
            override fun areItemsTheSame(
                oldItem: DataXXXXXXXXX,
                newItem: DataXXXXXXXXX
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataXXXXXXXXX,
                newItem: DataXXXXXXXXX
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(context, parent, listener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ListViewHolder private constructor(
        val context: Context,
        private val binding: FollowerItemLayoutBinding,
        private val listener: OnObjectListInteractionListener<DataXXXXXXXXX>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataXXXXXXXXX?) {
            item?.also { _item ->
                if (_item.followers[0].profilePic == "null"
                ) {
                    Glide.with(context).load(R.drawable.ic_circle_icons_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImageView)
                } else {
                    Glide.with(context).load(
                        _item.followers[0].profilePic
                    )
                        .placeholder(R.drawable.ic_circle_icons_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImageView)
                }
                if (bindingAdapterPosition < 9) binding.countTextView.text =
                    "0${bindingAdapterPosition + 1}"
                else binding.countTextView.text = (bindingAdapterPosition + 1).toString()

                binding.tvUserProfileNameID.text = _item.followers[0].name

                binding.tvEmail.text = _item.followers[0].email
                binding.textViewFollowerCount.text =
                    "${_item.followers[0].meta.followerCount} followers"

                binding.root.setOnClickListener {
                    listener.onClick(bindingAdapterPosition, _item, true)
                }
            }
        }

        companion object {
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: OnObjectListInteractionListener<DataXXXXXXXXX>,
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FollowerItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener)
            }
        }
    }
}
