package com.example.snipeagame.utils

class RegexConstants {
    companion object {
        const val NAME = "^[a-zA-Z]{2,30}$"
        const val PHONE = "^07[0-9]{8}$"
        const val EMAIL = "[A-Za-z0-9._]+@[A-Za-z0-9._]+\\.[A-Za-z]{2,}"
        const val PASSWORD = """^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-])[A-Za-z\d!@#$%^&*()_+{}\[\]:;<>,.?~\\/-]{8,}$"""
    }
}