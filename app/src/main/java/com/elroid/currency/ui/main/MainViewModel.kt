package com.elroid.currency.ui.main

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.elroid.currency.R
import com.elroid.currency.data.exception.isConnectivityError
import com.elroid.currency.data.model.CurrencyValue
import com.elroid.currency.domain.usecase.ConvertCurrency
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

sealed class ListState
data object LoadingState : ListState()
data class ListDataState(val currencyValues: List<CurrencyValue>) : ListState()

enum class Error(@StringRes val errorStringId: Int) {
    NO_CONNECTION(R.string.err_connection),
    UNKNOWN(R.string.err_unknown)
}

@KoinViewModel
class MainViewModel(
    private val convertCurrency: ConvertCurrency
) : ViewModel() {

    var currentBaseValue: CurrencyValue by mutableStateOf(CurrencyValue(0, "GBP"))
        private set

    var currentError: Error? by mutableStateOf(null)
        private set

    var currentListState: ListState by mutableStateOf(LoadingState)

    fun onAmountChanged(newAmount: String) {
        Logger.v { "onAmountChanged(newAmount:$newAmount)" }
        currentListState = LoadingState
        currentError = null // reset error on new input
        viewModelScope.launch {
            try {
                val newAmountValue: Number = newAmount.toFloatOrNull() ?: 0
                val baseCurrency: String = currentBaseValue.currencyCode
                val mapOfConvertedCurrencyValues = convertCurrency(CurrencyValue(newAmountValue, baseCurrency))
                currentListState = ListDataState(mapOfConvertedCurrencyValues.values.sorted())
                currentBaseValue = currentBaseValue.copy(amount = newAmountValue)
            } catch (e: Exception) {
                Logger.w(e) { "Error converting value:$newAmount" }
                val error: Error = when {
                    e.isConnectivityError() -> Error.NO_CONNECTION
                    else -> Error.UNKNOWN
                }
                currentError = error
            }
        }
    }

    fun onBaseCurrencyPressed() {
        Logger.v { "onBaseCurrencyPressed()" }
    }

    fun onAddCurrencyPressed() {
        Logger.v { "onAddCurrencyPressed()" }
    }

    fun onDeleteCurrencyPressed(currencyCode: String) {
        Logger.v { "onDeleteCurrencyPressed(currencyCode:$currencyCode)" }
    }
}