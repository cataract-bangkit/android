package com.cataract.detection.connection.model

import java.util.Date

class UserModel {
    data class Fillable(
        val id: String?,
        val sub: String?,
        val name: String?,
        val email: String?,
        val phone: String?,
        val issuedAt: Date?,
        val expiresAt: Date?
    )
}