package com.elroid.currency.feature.converter

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.elroid.currency.core.model.Currency
import com.elroid.currency.feature.common.theme.QuickCurrencyTheme

@Composable
fun CurrencyListDialog(
    currencies: List<Currency>,
    onItemClicked: (String) -> Unit,
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 64.dp)
                .wrapContentHeight(align = Alignment.Bottom)
                .nestedScroll(rememberNestedScrollInteropConnection()),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn {
                items(
                    items = currencies,
                    key = { it.currencyCode }
                ) { currencyDescriptor ->
                    CurrencyRow(currencyDescriptor) {
                        onItemClicked(it)
                        onDismiss()
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyRow(
    currency: Currency,
    onItemClicked: (String) -> Unit,
) {
    TextButton(
        onClick = { onItemClicked(currency.currencyCode) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AsyncImage(
                model = currency.iconUrl,
                contentDescription = currency.currencyName,
            )
            Text(
                text = "${currency.currencyCode} - ${currency.currencyName}",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyRowPreview() {
    QuickCurrencyTheme {
        CurrencyRow(
            object : Currency {
                override val currencyCode: String = "GBP"
                override val currencyName: String = "Pound Sterling"
                override val countryName: String = "United Kingdom"
                override val iconUrl: String = "https://currencyfreaks.com/photos/flags/gbp.png"
            },
            {}
        )
    }
}