package com.hsgaragepecas.garagehub.di

import com.hsgaragepecas.garagehub.ui.account.login.LoginViewModel
import com.hsgaragepecas.garagehub.ui.account.login.LoginViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindLoginViewModel(
        loginViewModelImpl: LoginViewModelImpl
    ): LoginViewModel
}
