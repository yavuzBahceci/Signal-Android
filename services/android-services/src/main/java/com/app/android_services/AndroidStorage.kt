package com.app.android_services

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.app.signal.domain.service.AppStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.KeyStore
import javax.inject.Inject

private enum class Key {
    PreviousSearches
}

data class AndroidStorage @Inject constructor(
    @ApplicationContext private val ctx: Context,
    private val jsonAdapter: Json
) : AppStorage {

    override var previousSearches: List<String>
        get() = preferences.get<List<String>>(jsonAdapter, Key.PreviousSearches) ?: emptyList()
        set(value) {
            preferences.save(jsonAdapter, Key.PreviousSearches, value)
        }

    override fun observePreviousSearches(): Flow<List<String>> {
        return resolveFlow(Key.PreviousSearches, emptyList())
    }

    private val preferences: SharedPreferences
    private val changes: MutableSharedFlow<Key>
    private val listener: SharedPreferences.OnSharedPreferenceChangeListener

    private val flows = mutableMapOf<Key, Flow<*>>()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        preferences = try {
            createSharedPreferences(ctx)
        } catch (_: Throwable) {
            deleteSharedPreferences(ctx)
            createSharedPreferences(ctx)
        }

        changes = MutableSharedFlow(replay = 0, extraBufferCapacity = 1)

        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            scope.launch {
                changes.emit(Key.valueOf(key))
            }
        }

        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    private inline fun <reified T> resolveFlow(key: Key, initialValue: T?): Flow<T> {
        val cached = flows[key]

        if (cached != null) {
            return cached.map { it as T }
        }

        val flow = observe<T>(key)
            .onStart {
                if (initialValue != null) {
                    emit(initialValue)
                }
            }
            .shareIn(scope, SharingStarted.Lazily, 1)

        flows[key] = flow

        return flow
    }

    private inline fun <reified T> observe(key: Key): Flow<T> {
        return changes
            .filter {
                it == key
            }
            .map {
                when (it) {
                    Key.PreviousSearches -> previousSearches as T
                }
            }
    }

    override fun reset() {
        preferences
            .edit()
            .clear()
            .apply()
    }

    private fun createSharedPreferences(ctx: Context): SharedPreferences {
        val masterKey = MasterKey
            .Builder(ctx)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            ctx,
            DATA_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun deleteSharedPreferences(ctx: Context) {
        try {
            val sharedPrefsFile = File("${ctx.filesDir.parent}/shared_prefs/$DATA_FILE.xml")

            // Delete the encrypted prefs file
            if (sharedPrefsFile.exists()) {
                sharedPrefsFile.delete()
            }

            // Delete the master key
            KeyStore.getInstance(KEYSTORE_PROVIDER).also {
                it.load(null)
                it.deleteEntry(MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            }
        } catch (_: Throwable) {
        }
    }

    companion object {
        private const val PACKAGE_ID = "com.app.signal"
        private const val DATA_FILE = "${PACKAGE_ID}_storage.data"

        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"

    }
}


private inline fun <reified T> SharedPreferences.get(adapter: Json, key: Key): T? {
    val value = getString(key.name, null) ?: return null
    return adapter.decodeFromString(value)
}

private inline fun <reified T> SharedPreferences.save(adapter: Json, key: Key, value: T?) {
    val editor = edit()

    if (value == null) {
        editor.remove(key.name)
    } else {
        editor.putString(key.name, adapter.encodeToString(value))
    }

    editor.apply()
}