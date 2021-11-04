package br.com.brq.agatha.base.modules

import androidx.room.Room
import br.com.brq.agatha.base.database.InvestimentosDataBase
import br.com.brq.agatha.base.database.dao.MoedaDao
import br.com.brq.agatha.base.database.dao.UsuarioDao
import br.com.brq.agatha.base.api.MoedaApiDataSource
import br.com.brq.agatha.base.api.MoedaDbDataSource
import br.com.brq.agatha.base.api.UsuarioRepository
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
    single <MoedaDbDataSource>{ MoedaDbDataSource(get<MoedaDao>()) }
    single <UsuarioRepository>{ UsuarioRepository(get<UsuarioDao>()) }
}