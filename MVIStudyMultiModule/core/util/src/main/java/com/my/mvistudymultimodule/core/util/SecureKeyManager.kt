package com.my.mvistudymultimodule.core.util

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES-GCM 암호화/복호화 유틸
 *
 * - Android Keystore(MasterKey)로 DEK를 안전하게 저장
 * - 랜덤 IV 사용 (12바이트, GCM 권장)
 * - Base64로 암호문 반환
 */
object SecureKeyManager {

    private const val PREF_NAME = "secure_key_prefs"
    private const val WRAPPED_DEK_KEY = "wrapped_dek" // MasterKey로 암호화된 DEK 저장 키
    private const val AES_MODE = "AES/GCM/NoPadding"
    private const val IV_SIZE = 12
    private val secureRandom = SecureRandom()

    /**
     * MasterKey 생성
     */
    private fun getMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    /**
     * EncryptedSharedPreferences 준비
     */
    private fun getEncryptedPrefs(context: Context): EncryptedSharedPreferences {
        val masterKey = getMasterKey(context)
        return EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    /**
     * DEK(AES-256) 생성
     */
    private fun generateDek(): SecretKey {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)
        return keyGen.generateKey()
    }

    /**
     * DEK를 MasterKey로 안전하게 저장
     */
    private fun saveWrappedDek(context: Context, dek: SecretKey) {
        val dekBytes = dek.encoded
        val wrapped = Base64.encodeToString(dekBytes, Base64.NO_WRAP)
        val prefs = getEncryptedPrefs(context)
        prefs.edit().putString(WRAPPED_DEK_KEY, wrapped).apply()
    }

    /**
     * 저장된 DEK 로드
     */
    private fun loadWrappedDek(context: Context): SecretKey? {
        val prefs = getEncryptedPrefs(context)
        val wrapped = prefs.getString(WRAPPED_DEK_KEY, null) ?: return null
        val dekBytes = Base64.decode(wrapped, Base64.NO_WRAP)
        return SecretKeySpec(dekBytes, "AES")
    }

    /**
     * DEK 확인 / 생성
     */
    private fun ensureDek(context: Context): SecretKey {
        val existing = loadWrappedDek(context)
        return existing ?: run {
            val dek = generateDek()
            saveWrappedDek(context, dek)
            dek
        }
    }

    // ---------------- 암호화 / 복호화 ----------------

    /**
     * 평문 -> 암호문(Base64)
     */
    fun encrypt(context: Context, plainText: String): String {
        val dek = ensureDek(context)
        val cipher = Cipher.getInstance(AES_MODE)
        val iv = ByteArray(IV_SIZE).also { secureRandom.nextBytes(it) }
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, dek, gcmSpec)
        val cipherBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val combined = ByteArray(iv.size + cipherBytes.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(cipherBytes, 0, combined, iv.size, cipherBytes.size)
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    /**
     * 암호문(Base64) -> 평문
     */
    fun decrypt(context: Context, base64Cipher: String): String {
        val dek = ensureDek(context)
        val combined = Base64.decode(base64Cipher, Base64.NO_WRAP)
        val iv = combined.copyOfRange(0, IV_SIZE)
        val cipherBytes = combined.copyOfRange(IV_SIZE, combined.size)
        val cipher = Cipher.getInstance(AES_MODE)
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, dek, gcmSpec)
        val plain = cipher.doFinal(cipherBytes)
        return String(plain, Charsets.UTF_8)
    }
}