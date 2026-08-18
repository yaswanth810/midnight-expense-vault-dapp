package com.kuiralabs.starter.counter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.midnight.kuira.dapp.PanelBar

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val selectedNetwork by viewModel.selectedNetwork.collectAsState()
    val busy by viewModel.busy.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        Text(
            text = "Midnight Expense Vault",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = "Track your expenses privately on Midnight.",
            style = MaterialTheme.typography.bodyLarge,
        )

        when (val current = state) {

            CounterUiState.NotReady -> {
                Text(
                    text = "Complete the wallet setup above to continue.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            CounterUiState.ReadyToDeploy -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = "Expense Vault",
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Text(
                            text = "Deploy your private expense vault to Midnight.",
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Button(
                            onClick = viewModel::deploy,
                            enabled = !busy,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = if (busy) {
                                    "Deploying..."
                                } else {
                                    "Deploy Expense Vault"
                                }
                            )
                        }
                    }
                }
            }

            is CounterUiState.Deployed -> {

                TotalExpenseCard(
                    total = current.totalExpenses,
                    busy = busy,
                )

                Text(
                    text = "Expense Categories",
                    style = MaterialTheme.typography.titleLarge,
                )

                ExpenseCard(
                    title = "Food",
                    emoji = "🍔",
                    value = current.foodExpenses,
                    onAdd = viewModel::addFoodExpense,
                    enabled = !busy,
                )

                ExpenseCard(
                    title = "Travel",
                    emoji = "✈️",
                    value = current.travelExpenses,
                    onAdd = viewModel::addTravelExpense,
                    enabled = !busy,
                )

                ExpenseCard(
                    title = "Education",
                    emoji = "🎓",
                    value = current.educationExpenses,
                    onAdd = viewModel::addEducationExpense,
                    enabled = !busy,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Contract",
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Text(
                            text = current.address,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                OutlinedButton(
                    onClick = viewModel::disconnect,
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Disconnect Contract")
                }
            }
        }

        if (error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }

        PanelBar(
            floating = false,
            network = selectedNetwork,
            onNetworkChange = viewModel::selectNetwork,
        )
    }
}

@Composable
private fun TotalExpenseCard(
    total: Long?,
    busy: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "Total Expenses",
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = total?.toString() ?: "—",
                style = MaterialTheme.typography.displaySmall,
            )

            Text(
                text = if (busy) {
                    "Transaction in progress..."
                } else {
                    "Expense entries recorded"
                },
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ExpenseCard(
    title: String,
    emoji: String,
    value: Long?,
    onAdd: () -> Unit,
    enabled: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "$emoji  $title",
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = value?.toString() ?: "—",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Button(
                onClick = onAdd,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (enabled) {
                        "Add $title Expense"
                    } else {
                        "Processing..."
                    }
                )
            }
        }
    }
}