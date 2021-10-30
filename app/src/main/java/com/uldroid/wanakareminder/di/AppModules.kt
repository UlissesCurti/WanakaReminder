package com.uldroid.wanakareminder.di

import com.uldroid.wanakareminder.data.database.ProductDatabase
import com.uldroid.wanakareminder.data.repository.ProductRepository
import com.uldroid.wanakareminder.ui.viewmodel.ReminderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ReminderViewModel(get()) }
}

val daoModule = module {
    single { ProductDatabase.getInstance(androidContext()).productDao }
}

val repositoryModule = module {
    single { ProductRepository(get()) }
}
