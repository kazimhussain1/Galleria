package com.example.galleria.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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

class HomeRecyclerAdapter :
    ListAdapter<ImageItem, HomeRecyclerAdapter.ImageViewHolder>(ImageItemDiffCallback()) {

    public var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            ItemHomeImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

//    public override fun getItem(position: Int): ImageItem {
//        return super.getItem(position)
//    }

    inner class ImageViewHolder(val binding: ItemHomeImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

        fun bind(item: ImageItem) {

            Utilities.loadImageWithGlide(binding.imageView, item.previewURL)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}