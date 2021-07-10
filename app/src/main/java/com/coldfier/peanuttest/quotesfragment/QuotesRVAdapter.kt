package com.coldfier.peanuttest.quotesfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coldfier.peanuttest.databinding.ItemQuoteSignalBinding
import com.coldfier.peanuttest.repository.QuoteInfoItem

class QuotesRVAdapter: ListAdapter<QuoteInfoItem, QuotesViewHolder>(QuotesItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        return QuotesViewHolder(
            ItemQuoteSignalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}

class QuotesItemCallback: DiffUtil.ItemCallback<QuoteInfoItem>() {
    override fun areItemsTheSame(oldItem: QuoteInfoItem, newItem: QuoteInfoItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: QuoteInfoItem, newItem: QuoteInfoItem): Boolean {
        return oldItem == newItem
    }
}

class QuotesViewHolder(private var binding: ItemQuoteSignalBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: QuoteInfoItem) {
        binding.quoteInfoItem = item
    }
}