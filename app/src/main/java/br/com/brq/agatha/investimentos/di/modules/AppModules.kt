package br.com.brq.agatha.investimentos.di.modules

import androidx.room.Room
import br.com.brq.agatha.domain.util.NOME_BANCO
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import br.com.brq.agatha.investimentos.repository.MoedaDbDataSource
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import br.com.brq.agatha.domain.util.ID_USUARIO
import br.com.brq.agatha.investimentos.viewModel.CambioViewModel
import br.com.brq.agatha.investimentos.viewModel.HomeViewModel
import br.com.brq.agatha.investimentos.viewModel.MoedaWrapper
import br.com.brq.agatha.investimentos.viewModel.base.AppContextProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataBaseModules = module {
    single<InvestimentosDataBase> {
        Room.databaseBuilder(
            get(),
            InvestimentosDataBase::class.java,
            br.com.brq.agatha.domain.util.NOME_BANCO
        ).build()
    }
}

val daoModules = module {
    single <MoedaDao>{ get <InvestimentosDataBase>().getMoedaDao() }
    single <UsuarioDao>{ get<InvestimentosDataBase>().getUsuarioDao() }
}

val repositoryModules = module {
    single <MoedaApiDataSource>{ MoedaApiDataSource(get<MoedaDao>()) }
    single <MoedaDbDataSource>{ MoedaDbDataSource(get<MoedaDao>(), AppContextProvider) }
    single <UsuarioRepository>{ UsuarioRepository(get<UsuarioDao>(), AppContextProvider) }
}

val viewModelModules = module {
    viewModel<CambioViewModel>{ CambioViewModel(get<MoedaDbDataSource>(), get<UsuarioRepository>(), AppContextProvider,
        br.com.brq.agatha.domain.util.ID_USUARIO
    )}
    viewModel<HomeViewModel>{ HomeViewModel(get<MoedaApiDataSource>(), AppContextProvider, MoedaWrapper())  }
}