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
import com.example.qissa.interfaces.SecondaryObjectListener
import com.example.qissa.models.DataXXXXXXXXXXX

class FollowingAdapter(
    val context: Context,
    private val listener: SecondaryObjectListener<DataXXXXXXXXXXX>
) : PagingDataAdapter<DataXXXXXXXXXXX, FollowingAdapter.ListViewHolder>(DIFF_CALLBACK) {
    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataXXXXXXXXXXX>() {
            override fun areItemsTheSame(
                oldItem: DataXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXX
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXX
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
        private val listener: SecondaryObjectListener<DataXXXXXXXXXXX>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataXXXXXXXXXXX?) {
            item?.also { _item ->
                if (_item.user.profilePic == "null"
                ) {
                    Glide.with(context).load(R.drawable.ic_circle_icons_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImageView)
                } else {
                    Glide.with(context).load(
                        _item.user.profilePic
                    )
                        .placeholder(R.drawable.ic_circle_icons_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImageView)
                }
                if (bindingAdapterPosition < 9) binding.countTextView.text =
                    "0${bindingAdapterPosition + 1}"
                else binding.countTextView.text = (bindingAdapterPosition + 1).toString()

                binding.tvUserProfileNameID.text = _item.user.name

                binding.tvEmail.text = _item.user.email
                binding.textViewFollowerCount.text =
                    "${_item.user.meta.followerCount} followers"

                binding.root.setOnClickListener {
                    listener.onSecObjClick(bindingAdapterPosition, _item)
                }
            }
        }

        companion object {
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: SecondaryObjectListener<DataXXXXXXXXXXX>,
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FollowerItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener)
            }
        }
    }
}
