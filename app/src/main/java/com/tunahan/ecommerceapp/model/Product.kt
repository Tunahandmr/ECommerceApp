package com.tunahan.ecommerceapp.model

data class Product(
    var documentUuid: String?,
    var downloadUrl: String?,
    var bookName: String?,
    var price: String?,
    var writer: String?,
    var publisher: String?,
    var pageCount: String?,
    var publicationYear: String?,
    var language: String?,
    var isFavorite: Boolean?
)
