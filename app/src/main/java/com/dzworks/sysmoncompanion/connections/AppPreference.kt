package com.dzworks.sysmoncompanion.connections

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject


interface AppPreferences {
    var url: String?
}

class AppPreferencesImpl @Inject constructor(private val context: Context) : AppPreferences {

    var preference: SharedPreferences
    var editor: SharedPreferences.Editor

    init {

        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        preference = EncryptedSharedPreferences.create(
            "com.dzworks.sysmoncompanion_preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = preference.edit()
    }

    override var url: String?
        get() = preference.getString(PREF_URL, "http://192.168.100.79:82/SysInfoHub?userName=Android")//""http://localhost:82/SysInfoHub")
        set(value) { editor.putString(PREF_URL, value).apply() }

    companion object {
        const val PREF_URL = "PREF_URL"
    }

}