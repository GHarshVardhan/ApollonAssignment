package com.example.apollon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apollon.R
import com.example.apollon.data.Result
import com.example.apollon.databinding.ShortenedLinkItemBinding

class ShortenedLinksAdapter constructor(private val itemInterface: ItemInterface? = null) :
    ListAdapter<Result, ShortenedLinksAdapter.ShortenedLinksViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortenedLinksViewHolder {
        val itemBinding =
            ShortenedLinkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShortenedLinksViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ShortenedLinksViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ShortenedLinksViewHolder(private val itemViewBinding: ShortenedLinkItemBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(result: Result?) {

            itemViewBinding.unsortenedUrl.text = result!!.original_link
            itemViewBinding.shortenedUrl.text = result.short_link
            itemViewBinding.delete.setOnClickListener {
                itemInterface!!.onDelete(
                    result
                )
            }
            itemViewBinding.copy.setOnClickListener {
                it.setBackgroundResource(R.drawable.rounded_button_pressed)
                itemViewBinding.copy.text= it.context.getString(R.string.copied)
                itemInterface!!.onCopy(
                    result.short_link
                )
            }
        }
    }

    interface ItemInterface {
        fun onDelete(result: Result)
        fun onCopy(link: String?)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Result> =
            object : DiffUtil.ItemCallback<Result>() {
                override fun areItemsTheSame(
                    oldResult: Result, newResult: Result
                ): Boolean {
                    return oldResult.code == newResult.code
                }

                override fun areContentsTheSame(
                    oldResult: Result, newResult: Result
                ): Boolean {
                    return oldResult == newResult
                }
            }
    }
}