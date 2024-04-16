package com.elroid.currency.domain.usecase

import com.elroid.currency.data.model.CurrencyValue
import com.elroid.currency.data.repository.DataRepository
import org.koin.core.annotation.Factory

@Factory
class ConvertCurrency(
    private val dataRepository: DataRepository
) {
    @Throws(IllegalArgumentException::class)
    suspend operator fun invoke(value: CurrencyValue): Map<String, CurrencyValue> {
        val rateResult = dataRepository.getLatestCurrencyRates()
        val fromRate: Double? = rateResult.ratesMap[value.currencyCode]?.toDouble()
        requireNotNull(fromRate) { "Unable to retrieve rate ${value.currencyCode} from rate results" }
        val amountInBaseCurrency: Double = value.amount.toDouble() / fromRate

        val mapOfCurrencyValues = mutableMapOf<String, CurrencyValue>()
        val selectedCurrencies = dataRepository.getSelectedCurrencies()
        selectedCurrencies.forEach { selCurrency ->
            val toRate: Double? = rateResult.ratesMap[selCurrency]?.toDouble()
            requireNotNull(toRate) { "Unable to retrieve rate $selCurrency from rate results" }
            val convertedAmount = amountInBaseCurrency * toRate
            mapOfCurrencyValues[selCurrency] = CurrencyValue(convertedAmount, selCurrency)
        }
        return mapOfCurrencyValues
    }
}