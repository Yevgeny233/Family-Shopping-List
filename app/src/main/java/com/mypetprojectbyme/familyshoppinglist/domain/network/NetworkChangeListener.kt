package com.mypetprojectbyme.familyshoppinglist.domain.network

import kotlinx.coroutines.flow.Flow


interface NetworkChangeListener {

    fun observe(): Flow<InternetConnectionStatus>


}


