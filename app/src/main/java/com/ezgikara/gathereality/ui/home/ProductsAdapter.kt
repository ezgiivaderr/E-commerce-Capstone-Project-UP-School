package com.ezgikara.gathereality.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezgikara.gathereality.data.model.response.ProductUI
import com.ezgikara.gathereality.databinding.ItemProductBinding

class ProductsAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onFavClick: (ProductUI) -> Unit
) : ListAdapter<ProductUI, ProductsAdapter.ProductViewHolder>(ProductDiffUtilCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onFavClick
        )
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))
    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onFavClick: (ProductUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title

                if (product.saleState) {
                    tvPrice.text = "${product.salePrice} $"
                    tvPrice.setTextColor(Color.parseColor("#d400ff"))
                } else {
                    tvPrice.text = "${product.price} $"
                }
                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }
                ivFav.setOnClickListener {
                    onFavClick(product)
                }
            }
        }
    }
    class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}