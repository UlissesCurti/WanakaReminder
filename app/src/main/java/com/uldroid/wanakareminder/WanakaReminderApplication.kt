package com.uldroid.wanakareminder

import android.app.Application
import com.uldroid.wanakareminder.di.daoModule
import com.uldroid.wanakareminder.di.repositoryModule
import com.uldroid.wanakareminder.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val TAG = "WanakaReminder"

class WanakaReminderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WanakaReminderApplication)
            modules(viewModelModule)
            modules(repositoryModule)
            modules(daoModule)
        }
    }
}