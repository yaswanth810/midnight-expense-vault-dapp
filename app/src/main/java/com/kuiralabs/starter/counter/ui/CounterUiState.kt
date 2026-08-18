package com.kuiralabs.starter.counter.ui

sealed interface CounterUiState {

    data object NotReady : CounterUiState

    data object ReadyToDeploy : CounterUiState

    data class Deployed(
        val address: String,
        val foodExpenses: Long? = null,
        val travelExpenses: Long? = null,
        val educationExpenses: Long? = null,
    ) : CounterUiState {

        val totalExpenses: Long?
            get() {
                val food = foodExpenses ?: return null
                val travel = travelExpenses ?: return null
                val education = educationExpenses ?: return null

                return food + travel + education
            }
    }
}