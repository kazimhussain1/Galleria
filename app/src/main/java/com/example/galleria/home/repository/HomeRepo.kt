package com.example.galleria.home.repository

import com.example.galleria.common.http.ApiClient
import com.example.galleria.common.http.RetrofitCallback
import com.example.galleria.home.api.HomeApi
import com.example.galleria.home.api.response.ImageQueryResponse
import com.example.galleria.home.contracts.HomeContracts

class HomeRepo(private val viewModel: HomeContracts.ViewModel) {

    private val homeApi: HomeApi = ApiClient.getInstance().createService(HomeApi::class.java)


    fun getImages(queryString: String) {

        homeApi.getImages(queryString).enqueue(
            RetrofitCallback<ImageQueryResponse>(
                resultCallback = {
                    viewModel.onImageQuerySuccess(it)
                },
                errorCallback = {
                    viewModel.onImageQueryError(it)
                })
        )
    }



    fun getImagesPaginated(queryString: String, pageNo: Int) {
        homeApi.getImagesPaginated(queryString, pageNo).enqueue(
            RetrofitCallback<ImageQueryResponse>(
                resultCallback = {
                    viewModel.onImageQueryPaginatedSuccess(it)
                },
                errorCallback = {
                    viewModel.onImageQueryPaginatedError(it)
                })
        )
    }

}