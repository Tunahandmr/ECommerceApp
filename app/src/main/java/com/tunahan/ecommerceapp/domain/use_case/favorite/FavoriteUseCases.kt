package com.tunahan.ecommerceapp.domain.use_case.favorite


data class FavoriteUseCases(
    val addFavoriteUseCase: AddFavoriteUseCase,
    val deleteByIdFavoriteUseCase: DeleteByIdFavoriteUseCase,
    val readAllFavoriteUseCase: ReadAllFavoriteUseCase
)
