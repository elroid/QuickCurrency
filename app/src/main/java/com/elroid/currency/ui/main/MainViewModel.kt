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
import com.elroid.currency.data.model.Currency
import com.elroid.currency.data.model.CurrencyValue
import com.elroid.currency.data.repository.DataRepository
import com.elroid.currency.domain.usecase.ConvertCurrency
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

sealed class ListState
data object InitState : ListState()
data object LoadingState : ListState()
data class ListDataState(val currencyValues: List<CurrencyValue>, val timestamp: Long) : ListState()

enum class Error(@StringRes val errorStringId: Int) {
    NO_CONNECTION(R.string.err_connection),
    UNKNOWN(R.string.err_unknown)
}

@KoinViewModel
class MainViewModel(
    private val dataRepository: DataRepository,
    private val convertCurrency: ConvertCurrency,
) : ViewModel() {

    var currentBaseValue: CurrencyValue by mutableStateOf(CurrencyValue(0, dataRepository.getBaseCurrency()))
        private set

    var currentError: Error? by mutableStateOf(null)
        private set

    var currentListState: ListState by mutableStateOf(InitState)
        private set

    var showCurrencyList: Boolean by mutableStateOf(false)

    var currencyList: List<Currency> by mutableStateOf(emptyList())
        private set


    fun onAmountChanged(newAmount: String) {
        Logger.v { "onAmountChanged(newAmount:$newAmount)" }
        currentError = null // reset error on new input
        viewModelScope.launch {
            try {
                val newAmountValue: Number = newAmount.toFloatOrNull() ?: 0
                val baseCurrency: String = currentBaseValue.currencyCode
                val newCurrencyValue = CurrencyValue(newAmountValue, baseCurrency)
                updateConversions(newCurrencyValue)
                currentBaseValue = newCurrencyValue
            } catch (e: Exception) {
                Logger.w(e) { "Error converting user-entered value:$newAmount" }
                handleError(e)
            }
        }
    }

    fun onBaseCurrencyPressed() {
        Logger.v { "onBaseCurrencyPressed()" }
        showCurrencyList { code ->
            viewModelScope.launch {
                currentBaseValue = CurrencyValue(currentBaseValue.amount, code)
                dataRepository.setBaseCurrency(code)
                updateConversions(currentBaseValue)
            }
        }
    }

    fun onAddCurrencyPressed() {
        Logger.v { "onAddCurrencyPressed()" }
        showCurrencyList { code ->
            viewModelScope.launch {
                dataRepository.addSelectedCurrency(code)
                updateConversions(currentBaseValue)
            }
        }
    }

    fun onDeleteCurrencyPressed(currencyCode: String) {
        Logger.v { "onDeleteCurrencyPressed(currencyCode:$currencyCode)" }
        viewModelScope.launch {
            dataRepository.removeSelectedCurrency(currencyCode)
            updateConversions(currentBaseValue)
        }
    }

    fun onCurrencySelected(currencyCode: String) {
        Logger.v { "onCurrencySelected(currencyCode:$currencyCode)" }
        currencyAction(currencyCode)
    }

    private fun updateConversionsFromBase() {
        viewModelScope.launch {
            updateConversions(currentBaseValue)
        }
    }

    private suspend fun updateConversions(currencyValue: CurrencyValue) {
        try {
            currentListState = LoadingState
            val conversionResult = convertCurrency(currencyValue)
            currentListState = ListDataState(
                conversionResult.valueMap.values.sortedBy { it.currencyCode }, conversionResult.timestamp
            )
        } catch (e: Exception) {
            Logger.w(e) { "Error converting value:$currencyValue" }
            handleError(e)
        }
    }

    private fun handleError(e: Exception) {
        val error: Error = when {
            e.isConnectivityError() -> Error.NO_CONNECTION
            else -> Error.UNKNOWN
        }
        currentError = error
    }

    private var currencyAction: (String) -> Unit = { Logger.i { "currency selected: $it" } }
    private fun showCurrencyList(action: (String) -> Unit) {
        currencyAction = action
        viewModelScope.launch {
            currencyList = dataRepository.getCurrencyList()
            showCurrencyList = true
        }
    }
}