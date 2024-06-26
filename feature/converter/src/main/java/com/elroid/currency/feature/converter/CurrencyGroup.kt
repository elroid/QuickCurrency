package com.elroid.currency.feature.converter

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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elroid.currency.core.model.CurrencyValue
import com.elroid.currency.feature.common.utilities.format
import com.elroid.currency.feature.common.utilities.symbol
import com.elroid.currency.feature.common.theme.QuickCurrencyTheme
import com.elroid.currency.feature.common.utilities.timeAgo
import com.elroid.currency.feature.common.R as commonR
import com.elroid.currency.feature.converter.R as converterR

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
                FullWidthText(text = stringResource(id = commonR.string.loading))
            }

            is ListDataState -> {
                val list = listState.currencyValues
                if (list.isEmpty()) {
                    FullWidthText(text = stringResource(commonR.string.empty))
                }
                list.forEach { currencyValue ->
                    ConvertedCurrencyRow(
                        currencyValue,
                        onClickDeleteCurrency = { onClickDeleteCurrency(currencyValue.currencyCode) },
                        modifier = modifier
                    )
                }
                FullWidthText(
                    text = "Rates last updated: ${listState.timestamp.timeAgo()}",
                    MaterialTheme.typography.bodySmall.fontSize
                )
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
        label = { Text(stringResource(converterR.string.enter_amount)) },
        textStyle = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}

@Composable
fun FullWidthText(text: String, fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = text, fontSize = fontSize)
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
        Text(
            text = currencyValue.format(),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        OutlinedButton(onClick = { onClickDeleteCurrency(currencyValue.currencyCode) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(converterR.string.remove_currency)
            )
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
                ), 1713433409000L
            ), {}, {}, {}, modifier = Modifier.width(360.dp)
        )
    }
}