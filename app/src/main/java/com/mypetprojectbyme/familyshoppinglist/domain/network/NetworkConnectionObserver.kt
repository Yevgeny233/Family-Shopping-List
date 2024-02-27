package com.mypetprojectbyme.familyshoppinglist.domain.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectionObserver @Inject constructor(@ApplicationContext context: Context) :
    NetworkChangeListener {

    private val connectionManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<InternetConnectionStatus> {
        return callbackFlow {
            val callBack = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(InternetConnectionStatus.AVAILABLE) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(InternetConnectionStatus.LOSING) }

                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(InternetConnectionStatus.LOST) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(InternetConnectionStatus.UNAVAILABLE) }
                }
            }
            connectionManager.registerDefaultNetworkCallback(callBack)

            awaitClose {
                connectionManager.unregisterNetworkCallback(callBack)
            }
        }.distinctUntilChanged()
    }
}


