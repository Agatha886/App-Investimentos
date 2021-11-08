package br.com.brq.agatha.presentation.modules

import br.com.brq.agatha.base.repository.MoedaDbDataSource
import br.com.brq.agatha.base.repository.UsuarioRepository
import br.com.brq.agatha.base.util.AppContextProvider
import br.com.brq.agatha.base.util.ID_USUARIO
import br.com.brq.agatha.presentation.viewModel.CambioViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewCambioModelModule = module {
    viewModel<CambioViewModel>{ CambioViewModel(get<MoedaDbDataSource>(), get<UsuarioRepository>(), AppContextProvider,
        ID_USUARIO
    )
    }
}