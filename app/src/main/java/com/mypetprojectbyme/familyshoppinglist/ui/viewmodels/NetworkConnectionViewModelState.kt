package com.mypetprojectbyme.familyshoppinglist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mypetprojectbyme.familyshoppinglist.domain.network.InternetConnectionStatus
import com.mypetprojectbyme.familyshoppinglist.domain.network.NetworkConnectionObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkConnectionViewModelState @Inject constructor(
    private val networkConnectionObserver: NetworkConnectionObserver
) : ViewModel() {

    private var _connectionStatus: MutableStateFlow<InternetConnectionStatus>? =
        MutableStateFlow(InternetConnectionStatus.AVAILABLE)

    val connectionStatus: Flow<InternetConnectionStatus>? = _connectionStatus

    init {
        checkConnectionState()

    }

    private fun checkConnectionState() = viewModelScope.launch(Dispatchers.IO) {
        networkConnectionObserver.observe().collect { currentStatus ->
            _connectionStatus?.value = currentStatus
        }
    }

    override fun onCleared() {
        super.onCleared()
        _connectionStatus = null
        viewModelScope.cancel()
    }

}