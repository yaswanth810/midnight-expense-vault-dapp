package com.kuiralabs.starter.counter.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuiralabs.starter.counter.data.ContractAddressStore
import com.kuiralabs.starter.counter.data.ExpenseVaultContract
import com.midnight.kuira.core.compact.ContractCallStage
import com.midnight.kuira.core.network.MidnightNetwork
import com.midnight.kuira.sdk.MidnightSdk
import com.midnight.kuira.sdk.walletruntime.MidnightSdkProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sdkProvider: MidnightSdkProvider,
    private val addressStore: ContractAddressStore,
) : ViewModel() {

    private val _state =
        MutableStateFlow<CounterUiState>(CounterUiState.NotReady)

    val state: StateFlow<CounterUiState> =
        _state.asStateFlow()

    private val _busy =
        MutableStateFlow(false)

    val busy: StateFlow<Boolean> =
        _busy.asStateFlow()

    private val _callStage =
        MutableStateFlow<ContractCallStage?>(null)

    val callStage: StateFlow<ContractCallStage?> =
        _callStage.asStateFlow()

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()

    val selectedNetwork: StateFlow<MidnightNetwork>
        get() = sdkProvider.selectedNetwork

    fun selectNetwork(network: MidnightNetwork) {
        sdkProvider.selectNetwork(network)
    }

    init {
        viewModelScope.launch {
            sdkProvider.sdk
                .combine(sdkProvider.selectedNetwork) { sdk, network ->
                    sdk to network
                }
                .collect { (sdk, network) ->
                    recomputeState(sdk, network)
                }
        }
    }

    private fun recomputeState(
        sdk: MidnightSdk?,
        network: MidnightNetwork,
    ) {
        val persisted = addressStore.get(network)

        _state.value = when {
            sdk == null ->
                CounterUiState.NotReady

            persisted == null ->
                CounterUiState.ReadyToDeploy

            else ->
                CounterUiState.Deployed(
                    address = persisted
                )
        }

        if (sdk != null && persisted != null) {
            refreshBalances(sdk, persisted)
        }
    }

    fun deploy() {
        val sdk =
            sdkProvider.sdk.value
                ?: return

        val network =
            sdkProvider.selectedNetwork.value

        runAction {

            val address =
                ExpenseVaultContract.deploy(
                    context = context,
                    sdk = sdk,
                ) {
                    _callStage.value = it
                }

            addressStore.put(
                network,
                address
            )

            _state.value =
                CounterUiState.Deployed(
                    address = address
                )

            refreshBalances(
                sdk = sdk,
                address = address
            )
        }
    }

    fun disconnect() {
        val network =
            sdkProvider.selectedNetwork.value

        addressStore.clear(network)

        _state.value =
            if (sdkProvider.sdk.value == null) {
                CounterUiState.NotReady
            } else {
                CounterUiState.ReadyToDeploy
            }
    }

    fun addFoodExpense() {
        val sdk =
            sdkProvider.sdk.value
                ?: return

        val address =
            deployedAddress()
                ?: return

        runAction {

            ExpenseVaultContract.addFoodExpense(
                context = context,
                sdk = sdk,
                address = address,
            ) {
                _callStage.value = it
            }

            refreshBalances(
                sdk,
                address
            )
        }
    }

    fun addTravelExpense() {
        val sdk =
            sdkProvider.sdk.value
                ?: return

        val address =
            deployedAddress()
                ?: return

        runAction {

            ExpenseVaultContract.addTravelExpense(
                context = context,
                sdk = sdk,
                address = address,
            ) {
                _callStage.value = it
            }

            refreshBalances(
                sdk,
                address
            )
        }
    }

    fun addEducationExpense() {
        val sdk =
            sdkProvider.sdk.value
                ?: return

        val address =
            deployedAddress()
                ?: return

        runAction {

            ExpenseVaultContract.addEducationExpense(
                context = context,
                sdk = sdk,
                address = address,
            ) {
                _callStage.value = it
            }

            refreshBalances(
                sdk,
                address
            )
        }
    }

    private fun deployedAddress(): String? =
        (state.value as? CounterUiState.Deployed)
            ?.address

    private fun refreshBalances(
        sdk: MidnightSdk,
        address: String,
    ) {
        viewModelScope.launch {

            try {

                val handle =
                    ExpenseVaultContract.buildReadHandle(
                        context = context,
                        sdk = sdk,
                        address = address
                    )

                val food =
                    ExpenseVaultContract.readFoodExpenses(handle)

                val travel =
                    ExpenseVaultContract.readTravelExpenses(handle)

                val education =
                    ExpenseVaultContract.readEducationExpenses(handle)

                _state.updateDeployed(address) {
                    copy(
                        foodExpenses = food,
                        travelExpenses = travel,
                        educationExpenses = education
                    )
                }

            } catch (_: Throwable) {
                // Background ledger reads are non-fatal.
            }
        }
    }

    private fun runAction(
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {

            _busy.value = true
            _error.value = null

            try {
                block()
            } catch (t: Throwable) {

                _error.value =
                    t.message
                        ?: t::class.simpleName
                        ?: "Unknown error"

            } finally {

                _busy.value = false
                _callStage.value = null
            }
        }
    }

    private fun MutableStateFlow<CounterUiState>.updateDeployed(
        address: String,
        transform: CounterUiState.Deployed.() -> CounterUiState.Deployed,
    ) {
        value =
            when (val current = value) {

                is CounterUiState.Deployed ->
                    if (current.address == address) {
                        current.transform()
                    } else {
                        current
                    }

                else ->
                    value
            }
    }
}
