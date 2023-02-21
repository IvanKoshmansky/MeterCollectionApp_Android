package com.example.android.metercollectionapp.presentation.uistate

data class DeviceParamsSelectUiState (
    val availableParams: List<DeviceParamSelectUiState> = listOf(),
    val availableParamsLoading: Boolean = false,
    val availableParamsEmpty: Boolean = false,
    val selectedParams: List<DeviceParamSelectUiState> = listOf(),
    val selectedParamsLoading: Boolean = false,
    val selectedParamsEmpty: Boolean = false
)

// поля с назначением типа "...changed" надо избегать, поскольку они характеризуют изменения в состоянии
// а нужно организовать поток только конечных состояний, машина конечных состояний
// View не должен "знать" о предыдущих состояниях
