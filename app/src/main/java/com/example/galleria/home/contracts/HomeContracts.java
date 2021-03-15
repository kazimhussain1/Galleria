package com.example.galleria.home.contracts;

import com.example.galleria.home.api.response.ImageQueryResponse;

import org.jetbrains.annotations.NotNull;

public interface HomeContracts {

    interface ViewModel{
        void onImageQuerySuccess(@NotNull ImageQueryResponse response);
        void onImageQueryError(@NotNull String error);

        void onImageQueryPaginatedSuccess(@NotNull ImageQueryResponse response);
        void onImageQueryPaginatedError(@NotNull String error);
    }
}
