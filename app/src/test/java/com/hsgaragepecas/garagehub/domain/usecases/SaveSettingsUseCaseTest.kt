package com.hsgaragepecas.garagehub.domain.usecases

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SaveSettingsUseCaseTest {

    private lateinit var saveSettingsUseCase: SaveSettingsUseCase

    @Before
    fun setUp() {
        saveSettingsUseCase = SaveSettingsUseCase()
    }

    @Test
    fun `invoke with valid data should return success`() {
        val result = saveSettingsUseCase(
            fantasyName = "Oficina do Zé",
            email = "email@example.com",
            cnpj = "11.444.777/0001-61", // Valid CNPJ
            landline = "(11) 4444-4444",
            whatsapp = "(11) 98888-8888",
            cep = "01001-000",
            number = "123",
            neighborhood = "Centro",
            city = "São Paulo",
            uf = "SP",
            complement = "Apto 101",
            logoPath = "logo.png"
        )
        assertTrue(result)
    }

    // Fantasy Name validation
    @Test
    fun `invoke with empty fantasy name should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    // Email validation
    @Test
    fun `invoke with invalid email should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "invalid-email", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    // CNPJ validation
    @Test
    fun `invoke with invalid CNPJ should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.111.111/1111-11", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    // WhatsApp validation
    @Test
    fun `invoke with invalid WhatsApp should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "11988888888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    // CEP validation
    @Test
    fun `invoke with empty CEP should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    // Number validation
    @Test
    fun `invoke with empty number should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "", neighborhood = "Centro", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    @Test
    fun `invoke with non-numeric number should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "abc", neighborhood = "Centro", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    // Neighborhood validation
    @Test
    fun `invoke with empty neighborhood should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "", city = "São Paulo", uf = "SP")
        assertFalse(result)
    }

    // City validation
    @Test
    fun `invoke with empty city should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "", uf = "SP")
        assertFalse(result)
    }

    // UF validation
    @Test
    fun `invoke with empty UF should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "")
        assertFalse(result)
    }

    // Optional fields
    @Test
    fun `invoke with empty optional fields should return success`() {
        val result = saveSettingsUseCase(
            fantasyName = "Oficina do Zé",
            email = "email@example.com",
            cnpj = "11.444.777/0001-61", // Valid CNPJ
            landline = "",
            whatsapp = "(11) 98888-8888",
            cep = "01001-000",
            number = "123",
            neighborhood = "Centro",
            city = "São Paulo",
            uf = "SP",
            complement = "",
            logoPath = null
        )
        assertTrue(result)
    }
    
    // Logo validation
    @Test
    fun `invoke with valid png logo should return success`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP", logoPath = "logo.png")
        assertTrue(result)
    }

    @Test
    fun `invoke with valid jpg logo should return success`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP", logoPath = "logo.jpg")
        assertTrue(result)
    }

    @Test
    fun `invoke with invalid logo extension should return failure`() {
        val result = saveSettingsUseCase(fantasyName = "Oficina", email = "email@example.com", cnpj = "11.444.777/0001-61", whatsapp = "(11) 98888-8888", cep = "01001-000", number = "123", neighborhood = "Centro", city = "São Paulo", uf = "SP", logoPath = "logo.gif")
        assertFalse(result)
    }
}
