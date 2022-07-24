package com.example.qissa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qissa.R
import com.example.qissa.databinding.QuestionItemLayoutBinding
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataXXXXXXXXXXXXXXX
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.AnswerViewModel

class QuestionAdapter(
    val context: Context,
    private val listener: OnObjectListInteractionListener<DataXXXXXXXXXXXXXXX>,
    private val adapter: AnswerAdapter,
    private val viewModel: AnswerViewModel,
    private val viewLifecycleOwner: LifecycleOwner

) : PagingDataAdapter<DataXXXXXXXXXXXXXXX, QuestionAdapter.ListViewHolder>(DIFF_CALLBACK) {
    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataXXXXXXXXXXXXXXX>() {
            override fun areItemsTheSame(
                oldItem: DataXXXXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXXXXX
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataXXXXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXXXXX
            ): Boolean {
                return oldItem == newItem
            }
        }
        var questionAdapterListener: QuestionAdapterListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(
            context,
            parent,
            listener,
            adapter,
            viewModel,
            viewLifecycleOwner
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ListViewHolder private constructor(
        val context: Context,
        private val binding: QuestionItemLayoutBinding,
        private val listener: OnObjectListInteractionListener<DataXXXXXXXXXXXXXXX>,
        private val adapter: AnswerAdapter,
        private val viewModel: AnswerViewModel,
        private val viewLifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataXXXXXXXXXXXXXXX?) {
            item?.also { _item ->
                initObserve(_item)
                binding.question = _item
                binding.executePendingBindings()
                binding.tvQuestionTitle.text = _item.title + "?"
                binding.tvAnswerCount.text = _item.meta.answersCount + " answers"

                binding.more.setOnClickListener {
                    if (_item.meta.answersCount == "0") {
                        Toast.makeText(
                            context,
                            "No answer available for this question",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (_item.flag) {
                            binding.recycleViewAnswer.gone()
                            binding.more.setImageResource(R.drawable.ic_arrow_drop_down)
                            rowIndex = -1
                            absIndex = -1
                            positon = -1
                            _item.flag = false
                        } else {
                            viewModel.items = null
                            viewModel.getAnswer(_item.id, null)
                            rowIndex = bindingAdapterPosition
                            absIndex = absoluteAdapterPosition
                            positon = position
                            binding.loadingView.visible()
                        }
                    }
                }
            }
        }

        private fun initObserve(item: DataXXXXXXXXXXXXXXX) {
            viewModel.items?.observe(
                viewLifecycleOwner
            ) { items ->
                if (items != null) {
                    if (rowIndex == bindingAdapterPosition && absIndex == absoluteAdapterPosition && positon == position) {
                        binding.recycleViewAnswer.visible()
                        item.flag = true
                        binding.more.setImageResource(R.drawable.ic_arrow_drop_up)
                        binding.recycleViewAnswer.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        binding.recycleViewAnswer.adapter = adapter
                        binding.viewModel = viewModel
                        binding.loadingView.gone()
                        binding.recycleViewAnswer.requestFocus()
                    } else {
                        binding.recycleViewAnswer.gone()
                        item.flag = false
                        binding.more.setImageResource(R.drawable.ic_arrow_drop_down)
                    }
                } else {
                }
            }
        }

        companion object {
            var rowIndex = -1
            var absIndex = -1
            var positon = -1
            fun from(
                context: Context,
                parent: ViewGroup,
                listener: OnObjectListInteractionListener<DataXXXXXXXXXXXXXXX>,
                adapter: AnswerAdapter,
                viewModel: AnswerViewModel,
                viewLifecycleOwner: LifecycleOwner
            ): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = QuestionItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(
                    context,
                    binding,
                    listener,
                    adapter,
                    viewModel,
                    viewLifecycleOwner
                )
            }
        }
    }

    interface QuestionAdapterListener {
        fun showAnswer(questionId: Int, binding: QuestionItemLayoutBinding)
    }
}
