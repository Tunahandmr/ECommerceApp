package com.tunahan.ecommerceapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.use_case.favorite.DeleteByIdFavoriteUseCase
import com.tunahan.ecommerceapp.domain.use_case.favorite.ReadAllFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val deleteByIdFavoriteUseCase: DeleteByIdFavoriteUseCase,
    private val readAllFavoriteUseCase: ReadAllFavoriteUseCase
) : ViewModel() {
/*
    private val _readAllFavorite = MutableLiveData<List<Favorite>>()
    val readAllFavorite: LiveData<List<Favorite>> = _readAllFavorite
*/

    private val _readAllFavorite = MutableStateFlow(FavoriteState())
    val readAllFavorite: StateFlow<FavoriteState> = _readAllFavorite

    /*private val _favoriteIsEmpty = MutableLiveData<Boolean>()
    val favoriteIsEmpty: LiveData<Boolean> = _favoriteIsEmpty*/

    private val _favoriteIsEmpty = MutableStateFlow(FavoriteState())
    val favoriteIsEmpty: StateFlow<FavoriteState> = _favoriteIsEmpty

    init {
        readAllFavorite()
    }


    private fun favoriteIsEmpty(countryList: List<Favorite>) {
        _favoriteIsEmpty.value = FavoriteState(isLoading = countryList.isNullOrEmpty())

    }

    fun deleteByIdFavorite(favorite: Favorite) {
        viewModelScope.launch {
            deleteByIdFavoriteUseCase(favorite)
        }

    }

    private fun readAllFavorite() {
        viewModelScope.launch {
            readAllFavoriteUseCase().collect{
                _readAllFavorite.value = FavoriteState(favorites = it)
                favoriteIsEmpty(it)
            }
        }
    }

}