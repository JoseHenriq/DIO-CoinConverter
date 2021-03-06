package br.com.dio.coinconverter.presentation.di

import br.com.dio.coinconverter.presentation.HistoryViewModel
import br.com.dio.coinconverter.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModule {

    //--- Carrega os modulos a terem suas dependĂȘncias injetadas
    fun load() {
        //------------------------------------
        loadKoinModules(viewModelModules())
        //------------------------------------
    }

    //--- Modulo viewModelModule
    private fun viewModelModules(): Module {
        return module {
            viewModel { HistoryViewModel(get()) }
            viewModel { MainViewModel(get(), get()) }
        }
    }
}