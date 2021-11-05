package br.com.brq.agatha.investimentos.modules

import br.com.brq.agatha.base.repository.MoedaApiDataSource
import br.com.brq.agatha.base.util.AppContextProvider
import br.com.brq.agatha.investimentos.viewmodel.HomeViewModel
import br.com.brq.agatha.presentation.viewModel.MoedaWrapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelHomeModule = module {
    viewModel<HomeViewModel>{ HomeViewModel(get<MoedaApiDataSource>(), AppContextProvider, MoedaWrapper())  }
}