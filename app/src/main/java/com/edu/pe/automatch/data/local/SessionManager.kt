package com.edu.pe.automatch.data.local

object SessionManager {
    var token: String? = null


    fun getCurrentUserToken(): String? {
        return token
    }
}