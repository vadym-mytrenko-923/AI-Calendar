package com.agents.app.demo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.agents.app.demo.data.local.storage.app.AppStorage
import com.agents.app.demo.data.local.storage.app.AppStorageImpl
import com.agents.app.demo.data.local.storage.user.UserStorage
import com.agents.app.demo.data.local.storage.user.UserStorageImpl
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

private const val COMMON_STORAGE_NAME = "AppStorage"
private const val APP_STORAGE_NAME = "$COMMON_STORAGE_NAME.App"
private const val USER_STORAGE_NAME = "$COMMON_STORAGE_NAME.User"

private const val MASTER_KEY_ALIAS = "ds_master_key"
private const val KEYSET_PREF_FILE = "ds_keyset_prefs"
private const val KEYSET_NAME = "ds_keyset"

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {
    @Provides
    @Singleton
    @Named(APP_STORAGE_NAME)
    fun provideAppDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        produceFile = { context.preferencesDataStoreFile(APP_STORAGE_NAME) }
    )

    @Provides
    @Singleton
    @Named(USER_STORAGE_NAME)
    fun provideUserDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        produceFile = { context.preferencesDataStoreFile(USER_STORAGE_NAME) }
    )

    @Provides
    @Singleton
    fun provideAead(@ApplicationContext context: Context): Aead {
        AeadConfig.register()
        val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, KEYSET_PREF_FILE)
            .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
            .withMasterKeyUri("android-keystore://$MASTER_KEY_ALIAS")
            .build()
            .keysetHandle

        return keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }

    @Provides
    @Singleton
    fun provideUserStorage(
        @Named(USER_STORAGE_NAME) dataStore: DataStore<Preferences>,
        encryption: Aead,
        gson: Gson
    ): UserStorage = UserStorageImpl(dataStore, encryption, gson)

    @Provides
    @Singleton
    fun provideAppStorage(
        @Named(APP_STORAGE_NAME) dataStore: DataStore<Preferences>,
        encryption: Aead,
        gson: Gson
    ): AppStorage = AppStorageImpl(dataStore, encryption, gson)
}
