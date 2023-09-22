package com.tunahan.ecommerceapp.domain.model



data class Product(
    var documentUuid: String?,
    var downloadUrl: String?,
    var bookName: String?,
    var price: String?,
    var writer: String?,
    var pageCount: String?,
    var explanation: String?,
    var isFavorite: Boolean?
)
