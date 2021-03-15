package com.example.galleria.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galleria.home.api.response.ImageItem

class DetailViewModel : ViewModel() {

    private val mImageData: MutableLiveData<List<ImageItem>> = MutableLiveData()
    private val mPosition: MutableLiveData<Int> = MutableLiveData()


    val imageData: LiveData<List<ImageItem>>
        get() = mImageData

    val position: LiveData<Int>
        get() = mPosition


    fun setImageData(data: List<ImageItem>?) {
        if (data != null)
            mImageData.value = data
    }

    fun setPosition(position: Int) {
        mPosition.value = position
    }

    fun setPositionIfNull(position: Int) {

        if (mPosition.value == null)
            mPosition.value = position
    }

}