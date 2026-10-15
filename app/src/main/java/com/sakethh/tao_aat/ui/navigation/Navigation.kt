package com.sakethh.tao_aat.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Navigation {

    @Serializable
    data object Queue: NavKey

    @Serializable
    data object Statistics: NavKey
}