package com.mypetprojectbyme.familyshoppinglist.domain.model


data class NoteModel(
    var nameNote: String? = null,
    var arrayPurchase: ArrayList<PurchaseModel>? = null,
    var checked: Boolean = false,
    var arrayUserEmail: ArrayList<String>? = null,
    var arrayListSubscribers: ArrayList<String>? = null
) 