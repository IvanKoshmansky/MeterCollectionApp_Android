package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus

data class LoginUiState (
    val name: String = "",
    val syncStatus: SyncStatus = SyncStatus.UNKNOWN,
    val loginError: Boolean = false,
    val isNewUser: Boolean = false
)

//data class LoginUiState (
//    val header: String,
//    val syncStatus: SyncStatus,
//    val login: String,
//    val pass: String,
//)
//можно сделать и "двунаправленный" UiState, но тогда нужно либо отслеживать смену конфигурации для
//восстановления текстовых полей (если они не будут восстанавливаться сами через saveStateHandle)
//потому что байндинг не работает на ввод с несовпадающими типами данных,
//здесь есть два варианта: отслеживать ввод каждого символа и перегенерировать заново весь стейт
//с учетом новых символов (через StateFlow) - это "true" подход, с поддержкой Single Source of Truth,
//либо на момент перед уничтожением View (onDestroyView) все содержимое полей сохранять руками в стейт,
//а затем восстанавливать в onViewCreated, оба подхода для сохранения пользовательского ввода при смене
//конфигурации явно избыточные, лучше сохранять просто через биндинг и прямую LiveData
//другая ситуация, если бы на этом же экране была бы кнопка "что-то сделать" и по ней стейт обновлялся бы,
//вот здесь вариант с подменой в StateFlow был бы правильным, причем либо с погружением в слой данных либо нет
//все поля в UiState должны обновляться одновременно
