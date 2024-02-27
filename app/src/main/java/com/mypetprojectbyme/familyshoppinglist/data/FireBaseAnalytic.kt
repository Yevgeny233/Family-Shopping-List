package com.mypetprojectbyme.familyshoppinglist.data

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Singleton

@Singleton
class FireBaseAnalytic {

    private var fireBaseAnalytics: FirebaseAnalytics? = null

    private val id = "appId"
    private val name = "appTitle"

    init {
        fireBaseAnalytics = Firebase.analytics

        fireBaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, id)
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "FamilyShopApp")
        }
    }
}

