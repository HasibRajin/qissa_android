package com.example.qissa.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.R
import com.example.qissa.databinding.StoriesItemLayoutBinding
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.Data
import com.example.qissa.models.DataX
import com.example.qissa.utils.CommonFunction
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import kotlin.properties.Delegates

class StoriesAdapter(
    val context: Context,
    private val listener: OnObjectListInteractionListener<DataX>,
    private val userInfo: Data,
) : PagingDataAdapter<DataX, StoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {
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
        var commentShowListener: CommentShowListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(context, parent, listener, userInfo)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ListViewHolder private constructor(
        val context: Context,
        private val binding: StoriesItemLayoutBinding,
        private val listener: OnObjectListInteractionListener<DataX>,
        private val userInfo: Data
    ) : RecyclerView.ViewHolder(binding.root) {
        private var isLiked by Delegates.notNull<Boolean>()
        private var reactionCount: Int = 0
        private val colorDark = "#2c3e50"
        private val colorLight = "#C4C4C4"

        fun bind(item: DataX?) {
            item?.also { _item ->
                isLiked = false
                binding.post = _item
                binding.executePendingBindings()

                binding.tvUserProfileNameID.text = userInfo.name
                if (userInfo?.profilePic == "null"
                ) {
                    Glide.with(context).load(R.drawable.ic_circle_icons_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.userImageView)
                } else {
                    Glide.with(context).load(userInfo?.profilePic)
                        .placeholder(R.drawable.ic_circle_icons_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.userImageView)
                }

                if (_item.image == "null" || _item.image == "" || _item.image == null) {
                    binding.imageview.visibility = View.GONE
                } else {
                    binding.imageview.visible()
                    Glide.with(context).load(_item.image)
                        .placeholder(R.drawable.ic_circle_icons_profile)
                        .centerCrop()
                        .into(binding.imageview)
                }

                if (_item.title == "null" || _item.title == "" || _item.title == null
                ) {
                    binding.tvPostTitle.gone()
                } else {
                    binding.tvPostTitle.visible()
                    binding.tvPostTitle.text = item.title
                }

                reactionCount = _item.meta.reactions_count.toInt()
                val time: String = _item.created_at.substring(0, 10)
                binding.tvPostTime.text = time
                if (_item.reactions.isNotEmpty()) {
                    CommonFunction.setIconAndColor(
                        binding.textViewLike,
                        R.drawable.ic_like,
                        colorDark
                    )
                    isLiked = true
                } else {
                    isLiked = false
                    binding.textViewLike.text = reactionCount.toString()
                    CommonFunction.setIconAndColor(
                        binding.textViewLike,
                        R.drawable.ic_like_full,
                        colorLight
                    )
                }

                binding.textviewComment.setOnClickListener {
                    commentShowListener?.showComments(_item.id)
                }

                binding.textViewLike.setOnLongClickListener {
                    listener.onLongClick(bindingAdapterPosition, _item)
                    true
                }

                binding.textViewLike.clickWithDebounce {
                    // Do anything you want
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Your Code
                        listener.onClick(bindingAdapterPosition, _item, isLiked)
                    }, 2500)
                }
            }
        }

        private fun View.clickWithDebounce(debounceTime: Long = 2500L, action: () -> Unit) {
            this.setOnClickListener(object : View.OnClickListener {
                private var lastClickTime: Long = 0
                override fun onClick(v: View) {
                    if (isLiked) {
                        isLiked = false
                        reactionCount--
                        binding.textViewLike.text = reactionCount.toString()
                        CommonFunction.setIconAndColor(
                            binding.textViewLike,
                            R.drawable.ic_like_full,
                            colorLight
                        )
                    } else {
                        isLiked = true
                        reactionCount++
                        binding.textViewLike.text = reactionCount.toString()
                        CommonFunction.setIconAndColor(
                            binding.textViewLike,
                            R.drawable.ic_like,
                            colorDark
                        )
                    }

                    if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                    else action()

                    lastClickTime = SystemClock.elapsedRealtime()
                }
            })
        }

        companion object {
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: OnObjectListInteractionListener<DataX>,
                userInfo: Data
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StoriesItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener, userInfo)
            }
        }
    }

    interface CommentShowListener {
        fun showComments(postId: Int)
    }
}
