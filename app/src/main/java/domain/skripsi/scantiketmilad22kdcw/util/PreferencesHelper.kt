package domain.skripsi.scantiketmilad22kdcw.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    companion object {
        const val PREF_STATUS_TICKET = "STATUS_TICKET"
        const val PREF_CATEGORY_TICKET = "CATEGORY_TICKET"
        const val PREF_IS_LOGIN = "LOGIN"
    }

    private val prefName = "PREFS_NAME"
    private var sharedPref: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun put(key: String, value: Set<String>) = editor.putStringSet(key, value).apply()
    fun getStringSet(key: String): Set<String>? = sharedPref.getStringSet(key, null)

    fun put(key: String, value: String) = editor.putString(key, value).apply()
    fun getString(key: String): String? = sharedPref.getString(key, null)
    fun put(key: String, value: Boolean) = editor.putBoolean(key, value).apply()
    fun getBoolean(key: String): Boolean = sharedPref.getBoolean(key, false)
    fun logout() = editor.clear().apply()

}