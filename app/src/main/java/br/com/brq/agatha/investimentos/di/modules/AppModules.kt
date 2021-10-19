package br.com.brq.agatha.investimentos.di.modules

import androidx.room.Room
import br.com.brq.agatha.investimentos.constantes.NOME_BANCO
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import br.com.brq.agatha.investimentos.repository.MoedaDbDataSource
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import br.com.brq.agatha.investimentos.viewModel.CambioViewModel
import br.com.brq.agatha.investimentos.viewModel.HomeViewModel
import br.com.brq.agatha.investimentos.viewModel.MoedaWrapper
import br.com.brq.agatha.investimentos.viewModel.base.AppContextProvider
import br.com.brq.agatha.investimentos.viewModel.base.CoroutinesContextProvider
import org.koin.dsl.module

val dataBaseModules = module {
    single<InvestimentosDataBase> {
        Room.databaseBuilder(
            get(),
            InvestimentosDataBase::class.java,
            NOME_BANCO
        ).build()
    }
}

val daoModules = module {
    single <MoedaDao>{ get <InvestimentosDataBase>().getMoedaDao() }
    single <UsuarioDao>{ get<InvestimentosDataBase>().getUsuarioDao() }
}

val contextProvider = module {
    single <AppContextProvider>{ AppContextProvider }
}

val repositoryModules = module {
    single <MoedaApiDataSource>{ MoedaApiDataSource(get<MoedaDao>()) }
    single <MoedaDbDataSource>{ MoedaDbDataSource(get<MoedaDao>(), get<AppContextProvider>()) }
    single <UsuarioRepository>{ UsuarioRepository(get<UsuarioDao>(), get<AppContextProvider>()) }
}

val viewModelModules = module {
    single<CambioViewModel>{ CambioViewModel(get<MoedaDbDataSource>(), get<UsuarioRepository>(), get <AppContextProvider>())  }
    single <HomeViewModel>{ HomeViewModel(get<MoedaApiDataSource>(), get <AppContextProvider>(), MoedaWrapper())  }
}