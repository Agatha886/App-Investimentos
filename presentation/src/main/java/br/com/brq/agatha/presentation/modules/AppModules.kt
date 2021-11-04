package br.com.brq.agatha.presentation.modules

import br.com.brq.agatha.base.repository.MoedaDbDataSource
import br.com.brq.agatha.base.repository.UsuarioRepository
import br.com.brq.agatha.base.util.AppContextProvider
import br.com.brq.agatha.investimentos.viewModel.CambioViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewCambioModelModule = module {
    viewModel<CambioViewModel>{ CambioViewModel(get<MoedaDbDataSource>(), get<UsuarioRepository>(), AppContextProvider,
        br.com.brq.agatha.domain.util.ID_USUARIO
    )}
}