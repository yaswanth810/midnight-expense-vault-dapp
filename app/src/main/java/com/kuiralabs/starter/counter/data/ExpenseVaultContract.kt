package com.kuiralabs.starter.counter.data

import android.content.Context
import com.midnight.kuira.contract.generated.ExpenseVaultContract as GeneratedExpenseVault
import com.midnight.kuira.core.compact.ContractCallStage
import com.midnight.kuira.core.compact.MidnightContract
import com.midnight.kuira.core.compact.proving.ProvingKeyManager
import com.midnight.kuira.sdk.MidnightSdk

internal object ExpenseVaultContract {

    private const val NAME =
        GeneratedExpenseVault.CONTRACT_ALIAS

    private const val CIRCUIT_FOOD =
        "addFoodExpense"

    private const val CIRCUIT_TRAVEL =
        "addTravelExpense"

    private const val CIRCUIT_EDUCATION =
        "addEducationExpense"

    private const val LEDGER_FOOD =
        "foodExpenses"

    private const val LEDGER_TRAVEL =
        "travelExpenses"

    private const val LEDGER_EDUCATION =
        "educationExpenses"

    private const val CONTRACT_JS_ASSET =
        GeneratedExpenseVault.RUNTIME_ASSET

    private const val KEYS_DIR =
        GeneratedExpenseVault.KEYS_ASSET_DIR

    private fun loadVerifierKeys(
        context: Context
    ): Map<String, ByteArray> {

        val circuits = listOf(
            CIRCUIT_FOOD,
            CIRCUIT_TRAVEL,
            CIRCUIT_EDUCATION
        )

        return circuits.associateWith { circuit ->
            context.assets
                .open("$KEYS_DIR/$circuit.verifier")
                .use { it.readBytes() }
        }
    }

    private fun installProvingKeys(
        context: Context
    ) {
        ProvingKeyManager(context)
            .installCircuitKeysFromAssets(KEYS_DIR)
    }

    private fun buildHandle(
        context: Context,
        sdk: MidnightSdk,
        address: String?,
        forWrite: Boolean,
    ): MidnightContract =
        MidnightContract.create(sdk.config) {

            name = NAME

            contractJs =
                context.assets.open(CONTRACT_JS_ASSET)

            if (address != null) {
                this.address = address
            }

            if (forWrite) {
                coinPublicKey = sdk.coinPublicKey
                circuitVerifierKeys = loadVerifierKeys(context)
            }
        }

    suspend fun deploy(
        context: Context,
        sdk: MidnightSdk,
        onProgress: (suspend (ContractCallStage) -> Unit)? = null,
    ): String {

        installProvingKeys(context)

        val handle = buildHandle(
            context = context,
            sdk = sdk,
            address = null,
            forWrite = true
        )

        return handle
            .deploy(onProgress = onProgress)
            .contractAddress
    }

    suspend fun addFoodExpense(
        context: Context,
        sdk: MidnightSdk,
        address: String,
        onProgress: (suspend (ContractCallStage) -> Unit)? = null,
    ) {

        installProvingKeys(context)

        val handle = buildHandle(
            context = context,
            sdk = sdk,
            address = address,
            forWrite = true
        )

        GeneratedExpenseVault(handle)
            .addFoodExpense(
                onProgress = onProgress
            )
    }

    suspend fun addTravelExpense(
        context: Context,
        sdk: MidnightSdk,
        address: String,
        onProgress: (suspend (ContractCallStage) -> Unit)? = null,
    ) {

        installProvingKeys(context)

        val handle = buildHandle(
            context = context,
            sdk = sdk,
            address = address,
            forWrite = true
        )

        GeneratedExpenseVault(handle)
            .addTravelExpense(
                onProgress = onProgress
            )
    }

    suspend fun addEducationExpense(
        context: Context,
        sdk: MidnightSdk,
        address: String,
        onProgress: (suspend (ContractCallStage) -> Unit)? = null,
    ) {

        installProvingKeys(context)

        val handle = buildHandle(
            context = context,
            sdk = sdk,
            address = address,
            forWrite = true
        )

        GeneratedExpenseVault(handle)
            .addEducationExpense(
                onProgress = onProgress
            )
    }

    fun buildReadHandle(
        context: Context,
        sdk: MidnightSdk,
        address: String
    ): MidnightContract =
        buildHandle(
            context = context,
            sdk = sdk,
            address = address,
            forWrite = false
        )

    suspend fun readFoodExpenses(
        handle: MidnightContract
    ): Long =
        handle
            .ledger()
            .getUint64(LEDGER_FOOD)

    suspend fun readTravelExpenses(
        handle: MidnightContract
    ): Long =
        handle
            .ledger()
            .getUint64(LEDGER_TRAVEL)

    suspend fun readEducationExpenses(
        handle: MidnightContract
    ): Long =
        handle
            .ledger()
            .getUint64(LEDGER_EDUCATION)

    fun observeLedger(
        handle: MidnightContract
    ) = handle.observeLedger()
}