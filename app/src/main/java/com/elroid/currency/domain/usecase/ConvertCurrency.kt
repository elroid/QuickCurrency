package com.elroid.currency.domain.usecase

import com.elroid.currency.data.model.CurrencyValue
import com.elroid.currency.data.repository.DataRepository
import org.koin.core.annotation.Factory
import java.net.UnknownHostException

@Factory
class ConvertCurrency(
    private val dataRepository: DataRepository
) {
    /**
     * Takes the given currency Value and converts it to all selected target currencies (omitting the base currency
     * if present)
     * @param value the value to convert
     * @throws IllegalArgumentException if no data is found for the target or base currency
     * @throws UnknownHostException if there is a connection error
     */
    @Throws(IllegalArgumentException::class, UnknownHostException::class)
    suspend operator fun invoke(value: CurrencyValue): Map<String, CurrencyValue> {
        val rateResult = dataRepository.getLatestCurrencyRates()
        val fromRate: Double? = rateResult.ratesMap[value.currencyCode]?.toDouble()
        requireNotNull(fromRate) { "Unable to retrieve rate ${value.currencyCode} from rate results" }
        val amountInBaseCurrency: Double = value.amount.toDouble() / fromRate

        val mapOfCurrencyValues = mutableMapOf<String, CurrencyValue>()
        val selectedCurrencies = dataRepository.getSelectedCurrencies()
        selectedCurrencies.filter { it != value.currencyCode }.forEach { selCurrency ->
            val toRate: Double? = rateResult.ratesMap[selCurrency]?.toDouble()
            requireNotNull(toRate) { "Unable to retrieve rate $selCurrency from rate results" }
            val convertedAmount = amountInBaseCurrency * toRate
            mapOfCurrencyValues[selCurrency] = CurrencyValue(convertedAmount, selCurrency)
        }
        return mapOfCurrencyValues
    }
}