package com.example.galleria.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.galleria.databinding.ItemDetailBinding
import com.example.galleria.databinding.ItemHomeImageBinding
import com.example.galleria.home.api.response.ImageItem
import com.example.galleria.utilities.Utilities

private class ImageItemDiffCallback : DiffUtil.ItemCallback<ImageItem>() {
    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem.largeImageURL == newItem.largeImageURL
    }

}

class DetailPagerAdapter :
    ListAdapter<ImageItem, DetailPagerAdapter.ImageViewHolder>(ImageItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            ItemDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    public override fun getItem(position: Int): ImageItem {
        return super.getItem(position)
    }

    class ImageViewHolder(val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: ImageItem) {
            binding.progressbar.isVisible = true
            Utilities.loadImageWithGlideCallback(binding.imageView, item.largeImageURL){

                binding.progressbar.isVisible = false
            }
        }
    }
}