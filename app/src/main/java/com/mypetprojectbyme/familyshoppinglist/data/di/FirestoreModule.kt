package com.mypetprojectbyme.familyshoppinglist.data.di

import com.mypetprojectbyme.familyshoppinglist.data.NoteFirestoreDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)

object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestoreModule(): NoteFirestoreDataSource {
        return NoteFirestoreDataSource()
    }

}