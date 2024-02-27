package com.mypetprojectbyme.familyshoppinglist.domain.model

import android.net.Uri

data class UserOfAppModel(
    val name: String?,
    val email: String?,
    val photoUrl: Uri?,
    val uniId: String
)