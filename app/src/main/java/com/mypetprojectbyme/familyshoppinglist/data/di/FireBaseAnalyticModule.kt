package com.mypetprojectbyme.familyshoppinglist.data.di

import com.mypetprojectbyme.familyshoppinglist.data.FireBaseAnalytic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object  FireBaseAnalyticModule {

    @Provides
    @Singleton
     fun provideFireBaseAnalytic(): FireBaseAnalytic{
        return FireBaseAnalytic()
    }
}