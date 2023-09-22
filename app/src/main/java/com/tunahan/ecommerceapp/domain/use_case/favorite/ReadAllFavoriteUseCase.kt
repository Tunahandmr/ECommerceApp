package com.tunahan.ecommerceapp.domain.use_case.favorite

import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadAllFavoriteUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
) {
     operator fun invoke() : Flow<List<Favorite>> {
       return localDatabaseRepository.readAllFavorite()
    }

}