package com.dzworks.sysmoncompanion.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.dzworks.sysmoncompanion.connections.AppPreferences
import com.dzworks.sysmoncompanion.connections.SystemInfo
import com.microsoft.signalr.Action
import com.microsoft.signalr.Action1
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
//import com.smartarmenia.dotnetcoresignalrclientjava.HubConnection
//import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
//import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
//import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnectionP2
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel
    @Inject constructor(private val appPreferences: AppPreferences): ViewModel() {

    var savedUrl: MutableLiveData<String> = MutableLiveData()

    var isConnectedLiveData = MutableLiveData<Boolean>()

    val hubUrl by lazy { appPreferences.url }

    var hubConnection: HubConnection? = null

    private val _sysInfo: MutableLiveData<SystemInfo> = MutableLiveData()
    val sysInfo: LiveData<SystemInfo> get() = _sysInfo

    private val viewModelJob = SupervisorJob()

    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        savedUrl.postValue(hubUrl)
    }


    fun connectToHub() {
        uiScope.launch {
            try {
                hubConnection = HubConnectionBuilder.create(hubUrl).build()

                setupHubProxy()

                val connectionComplete = hubConnection?.start()

                connectionComplete?.subscribeWith(object: CompletableObserver {
                    override fun onSubscribe(d: Disposable) = Unit

                    override fun onComplete() {
                        isConnectedLiveData.postValue(true)
                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }
                })

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
            isConnectedLiveData.value = false
        }
    }


    private fun setupHubProxy(){
        hubConnection?.on("SendSysInfo", Action1<SystemInfo> { data ->
            Timber.d(data.toString())
            _sysInfo.postValue(data)
        }, SystemInfo::class.java)
    }

}
