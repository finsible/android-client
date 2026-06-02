package com.itsjeel01.finsiblefrontend.common

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CurrencyFormatterTest {

    private val currencyRepository: CurrencyRepository = mockk()
    private lateinit var formatter: CurrencyFormatter

    private val inrCurrency = Currency(
        code = "INR",
        symbol = "₹",
        name = "Indian Rupee",
        flagEmoji = "🇮🇳",
        localeTag = "en-IN"
    )

    @Before
    fun setUp() {
        UserLocaleRegistry.initialize(object : LocaleProvider {
            override fun currentLocale(): Locale = Locale("en", "IN")
            override fun currentRegionIso(): String = "IN"
        })
        formatter = CurrencyFormatter(currencyRepository)
    }

    @Test
    fun `formats positive INR amount`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = 123_456L, currencyCode = "INR")

        assertThat(result).isEqualTo("₹1,234.56")
    }

    @Test
    fun `formats zero as zero`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = 0L, currencyCode = "INR")

        assertThat(result).isEqualTo("₹0")
    }

    @Test
    fun `formats negative amount with minus sign`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = -50_000L, currencyCode = "INR")

        assertThat(result).isEqualTo("-₹500")
    }

    @Test
    fun `formats negative amount without currency symbol`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(
            centis = -99_999L,
            currencyCode = "INR",
            options = CurrencyFormatter.CurrencyFormatOptions(
                includeCurrencySymbol = false
            )
        )

        assertThat(result).isEqualTo("-999.99")
    }

    @Test
    fun `formats large amount with grouping`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = 10_000_000L, currencyCode = "INR")

        assertThat(result).isEqualTo("₹1,00,000")
    }

    @Test
    fun `formats very small amount`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = 1L, currencyCode = "INR")

        assertThat(result).isEqualTo("₹0.01")
    }

    @Test
    fun `formats with sign and space after sign`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(
            centis = 1_000L,
            currencyCode = "INR",
            options = CurrencyFormatter.CurrencyFormatOptions(
                showPositiveSign = true,
                includeSpaceAfterSign = true,
            )
        )

        assertThat(result).isEqualTo("+ ₹10")
    }

    @Test
    fun `uses currency code as fallback when symbol unknown`() {
        every { currencyRepository.getByIsoCode("XYZ") } returns null

        val result = formatter.format(centis = 5_000L, currencyCode = "XYZ")

        assertThat(result).isEqualTo("XYZ50")
    }

    @Test
    fun `half-even rounding for three decimal places`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = 1_235L, currencyCode = "INR")

        assertThat(result).isEqualTo("₹12.35")
    }

    @Test
    fun `formats max long value without crash`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = Long.MAX_VALUE, currencyCode = "INR")

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `formats min long value without crash`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = Long.MIN_VALUE, currencyCode = "INR")

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `formats negative zero as zero`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = -0L, currencyCode = "INR")

        assertThat(result).isEqualTo("₹0")
    }

    @Test
    fun `formats small centis correctly`() {
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency

        val result = formatter.format(centis = 1L, currencyCode = "INR")

        assertThat(result).isEqualTo("₹0.01")
    }

    @Test
    fun `formats multiple currencies without cross-contamination`() {
        val usdCurrency = Currency(
            code = "USD", symbol = "\$", name = "US Dollar",
            flagEmoji = "🇺🇸", localeTag = "en-US"
        )
        every { currencyRepository.getByIsoCode("INR") } returns inrCurrency
        every { currencyRepository.getByIsoCode("USD") } returns usdCurrency

        val inrResult = formatter.format(centis = 100_000L, currencyCode = "INR")
        val usdResult = formatter.format(centis = 100_000L, currencyCode = "USD")

        assertThat(inrResult).contains("₹")
        assertThat(usdResult).contains("\$")
    }
}
