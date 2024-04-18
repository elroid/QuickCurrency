package com.elroid.currency.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elroid.currency.R
import com.elroid.currency.data.model.CurrencyValue
import com.elroid.currency.ui.common.format
import com.elroid.currency.ui.common.symbol
import com.elroid.currency.ui.theme.QuickCurrencyTheme

@Composable
fun CurrencyGroup(
    baseCurrencyValue: CurrencyValue,
    listState: ListState,
    onClickBaseCurrency: () -> Unit,
    onClickDeleteCurrency: (String) -> Unit,
    onCurrencyValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = baseCurrencyValue.symbol,
                fontSize = 24.sp,
                softWrap = false,
                maxLines = 1,
            )
            CurrencyEdit(
                value = baseCurrencyValue,
                onCurrencyValueChange = { onCurrencyValueChange(it) },
                modifier = Modifier.weight(9.0f)
            )
            Button(
                onClick = { onClickBaseCurrency() },
                modifier = Modifier.weight(4.0f)
            ) {
                Text(
                    baseCurrencyValue.currencyCode,
                    softWrap = false,
                    maxLines = 1,
                )
            }
        }
        when (listState) {
            InitState -> {
                FullWidthText(text = "")
            }

            LoadingState -> {
                FullWidthText(text = stringResource(id = R.string.loading))
            }

            is ListDataState -> {
                val list = listState.currencyValues
                if (list.isEmpty()) {
                    FullWidthText(text = stringResource(R.string.empty))
                }
                list.forEach { currencyValue ->
                    ConvertedCurrencyRow(
                        currencyValue,
                        onClickDeleteCurrency = { onClickDeleteCurrency(currencyValue.currencyCode) },
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencyEdit(
    value: CurrencyValue,
    onCurrencyValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currencyValue by rememberSaveable {
        mutableStateOf(value.amount.takeIf { it.toFloat() > 0 }?.toString() ?: "")
    }

    TextField(
        value = currencyValue,
        onValueChange = {
            currencyValue = it
            onCurrencyValueChange(it)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        label = { Text(stringResource(R.string.enter_amount)) },
        textStyle = TextStyle.Default.copy(fontSize = 28.sp),
        modifier = modifier
    )
}

@Composable
fun FullWidthText(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun ConvertedCurrencyRow(
    currencyValue: CurrencyValue,
    onClickDeleteCurrency: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = currencyValue.format(), fontSize = 24.sp, modifier = Modifier.padding(horizontal = 16.dp))
        OutlinedButton(onClick = { onClickDeleteCurrency(currencyValue.currencyCode) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.remove_currency))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyGroupPreview() {
    QuickCurrencyTheme {
        CurrencyGroup(
            CurrencyValue(123.45, "GBP"),
            ListDataState(
                listOf(
                    CurrencyValue(123, "EUR"),
                    CurrencyValue(345, "USD"),
                    CurrencyValue(567890, "JPY"),
                )
            ), {}, {}, {}, modifier = Modifier.width(360.dp)
        )
    }
}