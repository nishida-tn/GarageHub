package com.hsgaragepecas.garagehub.domain.usecases

import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor() {

    operator fun invoke(
        fantasyName: String,
        email: String,
        cnpj: String,
        landline: String? = null,
        whatsapp: String,
        cep: String,
        number: String,
        neighborhood: String,
        city: String,
        uf: String,
        complement: String? = null,
        logoPath: String? = null
    ): Boolean {
        if (fantasyName.isBlank()) return false
        if (!isEmailValid(email)) return false
        if (!isCnpjValid(cnpj)) return false
        if (!isWhatsappValid(whatsapp)) return false
        if (cep.isBlank()) return false
        if (number.isBlank() || !number.isNumeric()) return false
        if (neighborhood.isBlank()) return false
        if (city.isBlank()) return false
        if (uf.isBlank()) return false
        if (!isLogoValid(logoPath)) return false

        // All validations passed
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && Regex(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        ).matches(email)
    }

    private fun String.isNumeric() = this.matches(Regex("\\d+"))

    private fun isWhatsappValid(whatsapp: String): Boolean {
        val pattern = Regex("^\\(\\d{2}\\)\\s9\\d{4}-\\d{4}\$")
        return pattern.matches(whatsapp)
    }

    private fun isCnpjValid(cnpj: String): Boolean {
        val numbers = cnpj.filter { it.isDigit() }
        if (numbers.length != 14) return false
        if (numbers.all { it == numbers[0] }) return false

        try {
            var sum = 0
            var weight = 5
            (0..11).forEach {
                sum += numbers[it].digitToInt() * weight
                weight = if (weight == 2) 9 else weight - 1
            }
            var digit = 11 - (sum % 11)
            if (digit > 9) digit = 0
            if (numbers[12].digitToInt() != digit) return false

            sum = 0
            weight = 6
            (0..12).forEach {
                sum += numbers[it].digitToInt() * weight
                weight = if (weight == 2) 9 else weight - 1
            }
            digit = 11 - (sum % 11)
            if (digit > 9) digit = 0
            if (numbers[13].digitToInt() != digit) return false

            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun isLogoValid(logoPath: String?): Boolean {
        if (logoPath.isNullOrBlank()) {
            return true // Logo is optional
        }
        val lowercasedPath = logoPath.lowercase()
        return lowercasedPath.endsWith(".png") || lowercasedPath.endsWith(".jpg")
    }
}
