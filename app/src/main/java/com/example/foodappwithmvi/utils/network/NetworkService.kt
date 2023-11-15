package com.example.foodappwithmvi.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkService @Inject constructor(val cm:ConnectivityManager,val request:NetworkRequest):ConnectionStatus {







    override fun observe(): Flow<ConnectionStatus.Status> {
        return callbackFlow<ConnectionStatus.Status> {

            val callBack= object :ConnectivityManager.NetworkCallback(){

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

                launch { send(ConnectionStatus.Status.Available) }


                }


                override fun onUnavailable() {
                    super.onUnavailable()


                    launch { send(ConnectionStatus.Status.UnAvailable) }
                }


                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)

                launch { send(ConnectionStatus.Status.Losing) }

                }


                override fun onLost(network: Network) {
                    super.onLost(network)


               launch { send(ConnectionStatus.Status.Lost) }

                }








            }

            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N){

                cm.registerDefaultNetworkCallback(callBack)




            }else{

                cm.registerNetworkCallback(request,callBack)
            }

                awaitClose{

                    cm.unregisterNetworkCallback(callBack)
                }




        }.distinctUntilChanged()
    }


}