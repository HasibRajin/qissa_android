package com.example.qissa.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.R
import com.example.qissa.databinding.SearchPostitemLayoutBinding
import com.example.qissa.interfaces.BindableAdapter
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.Post
import com.example.qissa.utils.CommonFunction
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import kotlin.properties.Delegates

class SearchPostAdapter(
    var context: Context,
    private val listener: OnObjectListInteractionListener<Post>
) : ListAdapter<Post, SearchPostAdapter.ListViewHolder>(DIFF_CALLBACK),
    BindableAdapter<List<Post>> {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
        var commentShowListener: StoriesAdapter.CommentShowListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(context, parent, listener)
    }
//    Log.d("loadComment", "findComments: getting comments done }")

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun setItems(data: List<Post>?) {
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
        private val binding: SearchPostitemLayoutBinding,
        private val listener: OnObjectListInteractionListener<Post>
    ) : RecyclerView.ViewHolder(binding.root) {
        private var isLiked by Delegates.notNull<Boolean>()
        private var reactionCount: Int = 0
        private val colorDark = "#2c3e50"
        private val colorLight = "#C4C4C4"
        fun bind(item: Post) {
            binding.post = item
            binding.executePendingBindings()

            isLiked = false

            binding.tvUserProfileNameID.text = item.user.name
            if (item.user.profilePic == null
            ) {
                Glide.with(context).load(R.drawable.ic_circle_icons_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.userImageView)
            } else {
                Glide.with(context).load(item.user.profilePic)
                    .placeholder(R.drawable.ic_circle_icons_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.userImageView)
            }

//            if (item.image == "null" || item.image == "" || item.image == null) {
//                binding.imageview.visibility = View.GONE
//            } else {
//                binding.imageview.visible()
//                Glide.with(context).load(_item.image)
//                    .placeholder(R.drawable.ic_circle_icons_profile)
//                    .centerCrop()
//                    .into(binding.imageview)
//            }

            if (item.title == "null" || item.title == "" || item.title == null
            ) {
                binding.tvPostTitle.gone()
            } else {
                binding.tvPostTitle.visible()
                binding.tvPostTitle.text = item.title
            }

            reactionCount = item.meta.reactionsCount.toInt()
            val time: String = item.createdAt.substring(0, 10)
            binding.tvPostTime.text = time
            if (item.reactions.isNotEmpty()) {
                CommonFunction.setIconAndColor(
                    binding.textViewLike,
                    R.drawable.ic_like,
                    colorDark
                )
                isLiked = true
            } else {
                isLiked = false
                CommonFunction.setIconAndColor(
                    binding.textViewLike,
                    R.drawable.ic_like_full,
                    colorLight
                )
            }

            binding.textviewComment.setOnClickListener {
                commentShowListener?.showComments(item.id)
            }

            binding.textViewLike.setOnLongClickListener {
                listener.onLongClick(bindingAdapterPosition, item)
                true
            }

            binding.textViewLike.clickWithDebounce {
                // Do anything you want
                Handler(Looper.getMainLooper()).postDelayed({
                    // Your Code
                    listener.onClick(bindingAdapterPosition, item, isLiked)
                }, 2500)
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
                listener: OnObjectListInteractionListener<Post>
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchPostitemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(context, binding, listener)
            }
        }
    }

    interface CommentShowListener {
        fun showComments(postId: Int)
    }
}
