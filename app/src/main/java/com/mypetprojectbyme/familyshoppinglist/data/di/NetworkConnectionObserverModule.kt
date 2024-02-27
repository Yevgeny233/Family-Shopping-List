package com.mypetprojectbyme.familyshoppinglist.data.di

import android.content.Context
import com.mypetprojectbyme.familyshoppinglist.domain.network.NetworkConnectionObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkConnectionObserverModule {

    @Provides
    @Singleton
    fun provideNetworkConnectionObserver(@ApplicationContext context: Context): NetworkConnectionObserver {
        return NetworkConnectionObserver(context)
    }

}