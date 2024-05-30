package com.elroid.currency.feature.converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.elroid.currency.feature.common.theme.QuickCurrencyTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import com.elroid.currency.feature.common.R as commonR

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity(), KoinComponent {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            QuickCurrencyTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            title = {
                                Text(text = stringResource(commonR.string.app_name))
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { viewModel.onAddCurrencyPressed() }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.add_currency)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .safeDrawingPadding()
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CurrencyGroup(
                            baseCurrencyValue = viewModel.currentBaseValue,
                            listState = viewModel.currentListState,
                            onClickBaseCurrency = { viewModel.onBaseCurrencyPressed() },
                            onClickDeleteCurrency = { viewModel.onDeleteCurrencyPressed(it) },
                            onCurrencyValueChange = { viewModel.onAmountChanged(it) },
                        )
                        if (viewModel.showCurrencyList) {
                            CurrencyListDialog(
                                currencies = viewModel.currencyList,
                                onItemClicked = { viewModel.onCurrencySelected(it) },
                                onDismiss = { viewModel.showCurrencyList = false }
                            )
                        }
                    }

                    // Error handling
                    val scope = rememberCoroutineScope()
                    val errorMessage:String? = viewModel.currentError?.let {
                        stringResource(id = it.errorStringId)
                    }
                    LaunchedEffect(errorMessage) {
                        errorMessage?.let {
                            scope.launch { snackBarHostState.showSnackbar(it) }
                        }
                    }
                }
            }
        }
    }
}
