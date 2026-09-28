package com.movieapp.core.data.local.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class DatabasePassphraseProvider(private val context: Context) {

    fun getPassphrase(): ByteArray {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val wrapped = prefs.getString(KEY_WRAPPED_PASSPHRASE, null)
        val iv = prefs.getString(KEY_IV, null)

        if (wrapped != null && iv != null) {
            return decrypt(
                ciphertext = Base64.decode(wrapped, Base64.NO_WRAP),
                iv = Base64.decode(iv, Base64.NO_WRAP)
            )
        }

        val generated = ByteArray(KEY_SIZE_BYTES).also { SecureRandom().nextBytes(it) }
        val (ciphertext, ivBytes) = encrypt(generated)
        prefs.edit {
            putString(KEY_WRAPPED_PASSPHRASE, Base64.encodeToString(ciphertext, Base64.NO_WRAP))
            putString(KEY_IV, Base64.encodeToString(ivBytes, Base64.NO_WRAP))
        }
        return generated
    }

    private fun encrypt(plain: ByteArray): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKeystoreKey())
        return cipher.doFinal(plain) to cipher.iv
    }

    private fun decrypt(ciphertext: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateKeystoreKey(),
            GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        )
        return cipher.doFinal(ciphertext)
    }

    private fun getOrCreateKeystoreKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (keyStore.getKey(KEYSTORE_KEY_ALIAS, null) as? SecretKey)?.let { return it }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            KEYSTORE_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEYSTORE_KEY_ALIAS = "movie_db_passphrase_wrap_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val PREFS_NAME = "movie_db_secure_prefs"
        private const val KEY_WRAPPED_PASSPHRASE = "db_passphrase_wrapped"
        private const val KEY_IV = "db_passphrase_iv"
        private const val KEY_SIZE_BYTES = 32
    }
}
