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
import com.example.qissa.databinding.FollowerItemLayoutBinding
import com.example.qissa.interfaces.BindableAdapter
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.UserXXXXX
import com.example.qissa.utils.extendFunctions.gone

class SearchUserAdapter(
    var context: Context,
    private val listener: OnObjectListInteractionListener<UserXXXXX>
) : ListAdapter<UserXXXXX, SearchUserAdapter.ListViewHolder>(DIFF_CALLBACK),
    BindableAdapter<List<UserXXXXX>> {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserXXXXX>() {
            override fun areItemsTheSame(oldItem: UserXXXXX, newItem: UserXXXXX): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserXXXXX, newItem: UserXXXXX): Boolean {
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

    override fun setItems(data: List<UserXXXXX>?) {
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
        private val binding: FollowerItemLayoutBinding,
        private val listener: OnObjectListInteractionListener<UserXXXXX>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserXXXXX) {

            if (item.profilePic == String()
            ) {
                Glide.with(context).load(R.drawable.ic_circle_icons_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            } else {
                Glide.with(context).load(item.profilePic)
                    .placeholder(R.drawable.ic_circle_icons_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            }
            binding.tvUserProfileNameID.text = item.name
            binding.textViewFollowerCount.gone()
            binding.tvEmail.text = item.email
            if (bindingAdapterPosition < 9) binding.countTextView.text =
                "0${bindingAdapterPosition + 1}"
            else binding.countTextView.text = (bindingAdapterPosition + 1).toString()
            binding.root.setOnClickListener {
                listener.onClick(bindingAdapterPosition, item, true)
            }
        }

        companion object {
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: OnObjectListInteractionListener<UserXXXXX>
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FollowerItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener)
            }
        }
    }
}
