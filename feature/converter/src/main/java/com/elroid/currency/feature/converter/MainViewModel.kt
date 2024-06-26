package com.elroid.currency.feature.converter

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.elroid.currency.core.common.exception.isConnectivityError
import com.elroid.currency.core.domain.usecase.ConvertCurrency
import com.elroid.currency.core.model.Currency
import com.elroid.currency.core.model.CurrencyValue
import com.elroid.currency.core.model.repository.CurrencyRepository
import com.elroid.currency.core.model.repository.PrefsRepository
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import com.elroid.currency.feature.common.R as commonR
import com.elroid.currency.feature.converter.R as converterR

sealed class ListState
data object InitState : ListState()
data object LoadingState : ListState()
data class ListDataState(val currencyValues: List<CurrencyValue>, val timestamp: Long) : ListState()

enum class Error(@StringRes val errorStringId: Int) {
    NO_CONNECTION(converterR.string.err_connection),
    UNKNOWN(commonR.string.err_unknown)
}

@KoinViewModel
class MainViewModel(
    private val prefs: PrefsRepository,
    private val currencyRepository: CurrencyRepository,
    private val convertCurrency: ConvertCurrency,
) : ViewModel() {

    var currentBaseValue: CurrencyValue by mutableStateOf(CurrencyValue(0, prefs.getBaseCurrency()))
        private set

    var currentError: Error? by mutableStateOf(null)
        private set

    var currentListState: ListState by mutableStateOf(InitState)
        private set

    var currencyList: List<Currency> by mutableStateOf(emptyList())
        private set

    var showCurrencyList: Boolean by mutableStateOf(false)

    fun onAmountChanged(newAmount: String) {
        runAction("amountChanged") {
            val newAmountValue: Number = newAmount.toFloatOrNull() ?: 0
            val baseCurrency: String = currentBaseValue.currencyCode
            val newCurrencyValue = CurrencyValue(newAmountValue, baseCurrency)
            updateConversions(newCurrencyValue)
            currentBaseValue = newCurrencyValue
        }
    }

    fun onBaseCurrencyPressed() {
        showCurrencyList { code ->
            currentBaseValue = CurrencyValue(currentBaseValue.amount, code)
            prefs.setBaseCurrency(code)
            updateConversions(currentBaseValue)
        }
    }

    fun onAddCurrencyPressed() {
        showCurrencyList { code ->
            prefs.addSelectedCurrency(code)
            updateConversions(currentBaseValue)
        }
    }

    fun onDeleteCurrencyPressed(currencyCode: String) {
        runAction("deleteCurrency") {
            prefs.removeSelectedCurrency(currencyCode)
            updateConversions(currentBaseValue)
        }
    }

    fun onCurrencySelected(currencyCode: String) {
        runAction("currencySelected") {
            currencyAction(currencyCode)
        }
    }

    private suspend fun updateConversions(currencyValue: CurrencyValue) {
        currentListState = LoadingState
        val conversionResult = convertCurrency(currencyValue)
        currentListState = ListDataState(
            conversionResult.valueMap.values.sortedBy { it.currencyCode }, conversionResult.timestamp
        )
    }

    private var currencyAction: suspend (String) -> Unit = { }
    private fun showCurrencyList(action: suspend (String) -> Unit) {
        currencyAction = action
        runAction("showCurrencyList") {
            currencyList = currencyRepository.getCurrencyList()
            showCurrencyList = true
        }
    }

    /**
     * Convenience method to run actions with vm scope and error handling
     *
     * @param method name of the calling method, to assist with debugging
     * @param action suspend method to run on vm scope
     */
    private fun runAction(method: String, action: suspend () -> Unit) {
        currentError = null
        viewModelScope.launch {
            try {
                action()
            } catch (e: Exception) {
                Logger.w(e) { "Error in $method" }
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        val error: Error = when {
            e.isConnectivityError() -> Error.NO_CONNECTION
            else -> Error.UNKNOWN
        }
        currentError = error
    }
}