package com.dzworks.sysmoncompanion.connections

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application) : Context

    @Binds
    abstract fun bindSharedPreference(appPreferenceImpl: AppPreferencesImpl): AppPreferences
}