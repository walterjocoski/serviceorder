package com.rfpiscinas.serviceorder.util

import java.security.MessageDigest

object PasswordUtils {
    /**
     * Retorna o SHA-256 da senha em hexadecimal minúsculo.
     * Simples e suficiente para um app offline.
     */
    fun hash(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verify(password: String, hash: String): Boolean = hash(password) == hash
}
