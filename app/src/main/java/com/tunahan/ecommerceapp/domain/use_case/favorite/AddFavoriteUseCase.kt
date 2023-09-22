package com.tunahan.ecommerceapp.domain.use_case.favorite

import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
) {
    suspend operator fun invoke(favorite: Favorite){
        localDatabaseRepository.addFavorite(favorite)
    }

}