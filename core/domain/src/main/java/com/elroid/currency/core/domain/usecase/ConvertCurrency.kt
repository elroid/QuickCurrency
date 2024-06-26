package com.elroid.currency.core.domain.usecase

import com.elroid.currency.core.model.CurrencyValue
import com.elroid.currency.core.model.repository.PrefsRepository
import com.elroid.currency.core.model.repository.RatesRepository
import org.koin.core.annotation.Factory
import java.net.UnknownHostException

data class ConversionResult(
    val valueMap: Map<String, CurrencyValue>,
    val timestamp: Long
)

@Factory
class ConvertCurrency(
    private val ratesRepository: RatesRepository,
    private val prefsRepository: PrefsRepository,
) {
    /**
     * Takes the given currency Value and converts it to all selected target currencies (omitting the base currency
     * if present)
     * @param value the value to convert
     * @throws IllegalArgumentException if no data is found for the target or base currency
     * @throws UnknownHostException if there is a connection error
     */
    @Throws(IllegalArgumentException::class, UnknownHostException::class)
    suspend operator fun invoke(value: CurrencyValue): ConversionResult {
        val rates = ratesRepository.getLatestCurrencyRates()
        val fromRate: Double? = rates.ratesMap[value.currencyCode]?.toDouble()
        requireNotNull(fromRate) { "Unable to retrieve rate ${value.currencyCode} from rate results" }
        val amountInBaseCurrency: Double = value.amount.toDouble() / fromRate

        val mapOfCurrencyValues = mutableMapOf<String, CurrencyValue>()
        val selectedCurrencies = prefsRepository.getSelectedCurrencies()
        selectedCurrencies.filter { it != value.currencyCode }.forEach { selCurrency ->
            val toRate: Double? = rates.ratesMap[selCurrency]?.toDouble()
            requireNotNull(toRate) { "Unable to retrieve rate $selCurrency from rate results" }
            val convertedAmount = amountInBaseCurrency * toRate
            mapOfCurrencyValues[selCurrency] = CurrencyValue(convertedAmount, selCurrency)
        }
        return ConversionResult(mapOfCurrencyValues, rates.timestamp)
    }
}