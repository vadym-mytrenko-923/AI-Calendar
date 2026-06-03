package com.agents.app.demo.data.local

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.crypto.tink.Aead
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val AAD = "storage_v1".toByteArray()

private fun Aead.encryptToBase64(plainText: String, associatedData: ByteArray): String {
    val cipher = encrypt(plainText.toByteArray(Charsets.UTF_8), associatedData)
    return Base64.encodeToString(cipher, Base64.NO_WRAP)
}

private fun Aead.decryptOrNull(base64Cipher: String, associatedData: ByteArray): String? = runCatching {
    val cipherBytes = Base64.decode(base64Cipher, Base64.NO_WRAP)
    val clear = decrypt(cipherBytes, associatedData)
    String(clear, Charsets.UTF_8)
}.getOrNull()

suspend fun DataStore<Preferences>.putValue(key: Preferences.Key<String>, encryption: Aead, value: String) {
    edit { it[key] = encryption.encryptToBase64(value, AAD) }
}

fun DataStore<Preferences>.getFlowValue(key: Preferences.Key<String>, encryption: Aead): Flow<String?> = data.map {
    it[key]?.let { token -> encryption.decryptOrNull(token, AAD) }
}

suspend fun DataStore<Preferences>.getValueOnce(
    key: Preferences.Key<String>,
    encryption: Aead
): String? {
    val prefs = data.firstOrNull() ?: return null
    val enc = prefs[key] ?: return null
    return encryption.decryptOrNull(enc, AAD)
}

suspend fun <T> DataStore<Preferences>.putJsonValue(key: Preferences.Key<String>, encryption: Aead, gson: Gson, value: T) {
    putValue(key, encryption, gson.toJson(value))
}

suspend fun <T> DataStore<Preferences>.getJsonValue(
    key: Preferences.Key<String>,
    encryption: Aead,
    gson: Gson,
    clazz: Class<T>
): T? {
    val json = getValueOnce(key, encryption) ?: return null
    return gson.fromJson(json, clazz)
}
