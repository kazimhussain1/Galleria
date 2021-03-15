package com.example.galleria.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galleria.common.ContextService
import com.example.galleria.home.api.response.ImageItem
import com.example.galleria.home.api.response.ImageQueryResponse
import com.example.galleria.home.contracts.HomeContracts
import com.example.galleria.home.repository.HomeRepo
import com.example.galleria.utilities.Utilities

class HomeViewModel : ViewModel(), HomeContracts.ViewModel {

    private var pageNo: Int = 0
    private var searchQuery: String = ""
    private val homeRepo: HomeRepo = HomeRepo(this)

    private val mProgressBarVisible: MutableLiveData<Boolean> = MutableLiveData();
    private val mSearchVisible: MutableLiveData<Boolean> = MutableLiveData()
    private val mImageItemResult: MutableLiveData<List<ImageItem>> = MutableLiveData()
    private val mError: MutableLiveData<String> = MutableLiveData()

    val progressBarVisible: LiveData<Boolean>
        get() = mProgressBarVisible

    val searchVisible: LiveData<Boolean>
        get() = mSearchVisible

    val imageItemResult: LiveData<List<ImageItem>>
        get() = mImageItemResult

    val error: LiveData<String>
        get() = mError


    fun getImages(searchQuery: String) {
        pageNo = 1
        this.searchQuery = searchQuery
        homeRepo.getImages(searchQuery)

        mProgressBarVisible.value = false
    }

    override fun onImageQueryPaginatedError(error: String) {
        mError.value = error
        mProgressBarVisible.value = false
    }

    override fun onImageQueryPaginatedSuccess(response: ImageQueryResponse) {
        val oldData = mImageItemResult.value?.toMutableList()
        if (oldData != null) {
            oldData.addAll(response.imageItems)
            mImageItemResult.value = oldData
        }
        else
            mImageItemResult.value = response.imageItems

        mProgressBarVisible.value = false
    }

    override fun onImageQueryError(error: String) {

        mError.value = error
        mProgressBarVisible.value = false
    }

    override fun onImageQuerySuccess(response: ImageQueryResponse) {
        mImageItemResult.value = response.imageItems.toMutableList()
        mProgressBarVisible.value = false
    }


    fun setSearchVisible(isSearchVisible: Boolean) {
        mSearchVisible.value = isSearchVisible
    }

    fun toggleSearchVisible() {
        if (mSearchVisible.value != null) {
            mSearchVisible.value = !mSearchVisible.value!!
        } else {
            mSearchVisible.value = true
        }
    }

    fun getImagesNextPage() {
        if (pageNo > 0) {
            Utilities.showToast(ContextService.getInstance().context, "new Data")
            homeRepo.getImagesPaginated(searchQuery, ++pageNo)

            mProgressBarVisible.value = true
        }
    }

}