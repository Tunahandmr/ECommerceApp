package com.tunahan.ecommerceapp.domain.use_case.sign

data class SignUseCases(
    val checkCurrentUserUseCase: CheckCurrentUserUseCase,
    val currentUserUseCase: CheckCurrentUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val forgotPasswordUseCase: ForgotPasswordUseCase,
    val liveUseCase: LiveUseCase,
    val signInUseCase: SignInUseCase,
    val signOutUseCase: SignOutUseCase,
    val signUpUseCase: SignUpUseCase,
    val updatePasswordUseCase: UpdatePasswordUseCase
)
