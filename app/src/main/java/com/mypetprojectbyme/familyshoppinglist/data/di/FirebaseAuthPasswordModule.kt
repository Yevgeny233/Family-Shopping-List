package com.mypetprojectbyme.familyshoppinglist.data.di

import android.content.Context
import com.mypetprojectbyme.familyshoppinglist.data.FirebaseAuthPassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object FirebaseAuthPasswordModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthPassword(@ApplicationContext context: Context): FirebaseAuthPassword {
        return FirebaseAuthPassword(context)
    }

}