package com.example.android.metercollectionapp.presentation.uistate

data class DeviceParamsSelectUiState (
    val availableParams: List<DeviceParamSelectUiState> = emptyList(),
    val availableParamsLoading: Boolean = false,
    val selectedParams: List<DeviceParamSelectUiState> = emptyList(),
    val selectedParamsLoading: Boolean = false
)

// поля с назначением типа "...changed" надо избегать, поскольку они характеризуют изменения в состоянии
// а нужно организовать поток только конечных состояний, машина конечных состояний
// View не должен "знать" о предыдущих состояниях
// "Empty" при наличии списка тоже избыточно и может привести к нарушению "атомарности"
