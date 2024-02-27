package com.mypetprojectbyme.familyshoppinglist.data.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.mypetprojectbyme.familyshoppinglist.data.FirebaseAuthGoogle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)

object FirebaseAuthGoogleModule {

    @Provides
    @ActivityScoped
    fun provideFirebaseAuthGoogle(
        @ApplicationContext context: Context,
        fragmentActivity: FragmentActivity
    ): FirebaseAuthGoogle {
        return FirebaseAuthGoogle(context, fragmentActivity)
    }

}
