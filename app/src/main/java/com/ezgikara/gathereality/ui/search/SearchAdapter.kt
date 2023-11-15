package com.ezgikara.gathereality.ui.search

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.ezgikara.gathereality.data.model.response.ProductUI
import com.ezgikara.gathereality.databinding.ItemSearchProductBinding



class SearchAdapter(
    private val onProductClick: (Int) -> Unit
): ListAdapter<ProductUI, SearchAdapter.SearchViewHolder>(SearchDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) = holder.bind(getItem(position))

    class SearchViewHolder(
        private val binding: ItemSearchProductBinding,
        private val onProductClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle2.text = product.title
                if(product.saleState) {
                    tvPrice2.text = "${product.price} $"
                    tvPrice2.paintFlags = tvPrice2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvPrice2.paintFlags = 0
                }
                tvSaleprice2.text = "${product.salePrice} $"

                ratingBar2.rating = (product.rate).toFloat()

                Glide.with(ivSearch).load(product.imageOne).into(ivSearch)

                root.setOnClickListener {
                    onProductClick(product.id)
                }
            }
        }
    }

    class SearchDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}