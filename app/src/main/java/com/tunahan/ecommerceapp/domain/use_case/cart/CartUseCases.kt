package com.tunahan.ecommerceapp.domain.use_case.cart

data class CartUseCases(
    val addCartUseCase: AddCartUseCase,
    val deleteAllCartUseCase: DeleteAllCartUseCase,
    val deleteByIdCartUseCase: DeleteByIdCartUseCase,
    val readCartUseCase: ReadCartUseCase,
    val updateByIdCartUseCase: UpdateByIdCartUseCase
)
