package com.example.galleria.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galleria.home.api.response.ImageItem

class DetailViewModel : ViewModel() {

    private val mImageData: MutableLiveData<List<ImageItem>> = MutableLiveData()

    val imageData: LiveData<List<ImageItem>>
        get() = mImageData


    fun setImageData(data: List<ImageItem>?) {
        if (data != null)
            mImageData.value = data
    }

}