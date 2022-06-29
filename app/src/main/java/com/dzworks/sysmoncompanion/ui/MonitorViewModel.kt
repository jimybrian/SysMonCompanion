package com.dzworks.sysmoncompanion.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dzworks.sysmoncompanion.connections.AppPreferences
import com.dzworks.sysmoncompanion.connections.SystemInfo
import com.microsoft.signalr.Action1
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
//import com.smartarmenia.dotnetcoresignalrclientjava.HubConnection
//import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
//import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
//import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnectionP2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel
    @Inject constructor(private val appPreferences: AppPreferences): ViewModel() {


//    lateinit var hubConnection: HubConnection
//        val hubUrl by lazy { appPreferences.url }
//    var keepConnAlive = false
//
//    var isConnectedLiveData = MutableLiveData<Boolean>()
//
//    private val _sysInfo: MutableLiveData<SystemInfo> = MutableLiveData()
//    val sysInfo: LiveData<SystemInfo> get() = _sysInfo
//
//    fun connectToHub() {
//        try {
//            hubConnection = WebSocketHubConnectionP2(hubUrl, "")
//            hubConnection.connect()
//            hubConnection.addListener(object : HubConnectionListener {
//                override fun onConnected() {
//                    isConnectedLiveData.postValue(true)
//                }
//
//                override fun onMessage(message: HubMessage?) {
//                    Timber.d(message.toString())
//                }
//
//                override fun onDisconnected() {
//                    isConnectedLiveData.postValue(false)
//                    when(keepConnAlive){
//                        true -> connectToHub()
//                    }
//                }
//
//                override fun onError(exception: Exception?) {
//                    isConnectedLiveData.postValue(false)
//                }
//            })
//        }
//        catch (e:Exception){
//            Timber.d(e)
//        }
//    }
//
//    fun disconnectFromHub(){
//        try{
//            when {
//                isConnectedLiveData.value != null -> if (isConnectedLiveData.value!!) {
//                    hubConnection.disconnect()
//                }
//            }
//        }catch (e:Exception){
//
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        try{
//            when {
//                isConnectedLiveData.value != null -> if (isConnectedLiveData.value!!) {
//                    hubConnection.disconnect()
//                }
//            }
//        }catch (e:Exception){
//
//        }
//    }

    var isConnectedLiveData = MutableLiveData<Boolean>()

    val hubUrl by lazy { appPreferences.url }

    var hubConnection: HubConnection? = null

    private val _sysInfo: MutableLiveData<SystemInfo> = MutableLiveData()
    val sysInfo: LiveData<SystemInfo> get() = _sysInfo

    private val viewModelJob = SupervisorJob()

    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun connectToHub() {
        uiScope.launch {
            try {
                hubConnection = HubConnectionBuilder.create(hubUrl).build()

                setupHubProxy()

                var startState = hubConnection?.start()

                isConnectedLiveData.value = true

                Timber.d(hubConnection?.connectionState?.toString())

                hubConnection?.onClosed {
                    isConnectedLiveData.value = false
                    Timber.d("Connection Closed")
                }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    fun disconnectFromHub(){
        uiScope.launch {
            hubConnection?.stop()
        }
    }


    private fun setupHubProxy(){
        hubConnection?.on("SendSysInfo", Action1<SystemInfo> { data ->
            Timber.d(data.toString())
            _sysInfo.postValue(data)
        }, SystemInfo::class.java)
    }

}
