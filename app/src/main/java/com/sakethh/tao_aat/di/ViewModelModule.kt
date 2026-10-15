package com.sakethh.tao_aat.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.tao_aat.ui.screens.events.EventQueueScreenVM
import kotlin.reflect.KClass


@Composable
inline fun <reified T : ViewModel> aatViewModel(
    factory: ViewModelProvider.Factory = AATViewModelFactory,
): T = viewModel(modelClass = T::class, factory = factory)

object AATViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = when (modelClass) {
        EventQueueScreenVM::class -> EventQueueScreenVM(
            attributionRepo = DIContainer.attributionRepo
        )
        else -> error(
            "Not sure how to create an instance of ${modelClass.simpleName}",
        )
    } as T
}