package br.com.brq.agatha.investimentos

import android.app.Application
import br.com.brq.agatha.base.modules.daoModules
import br.com.brq.agatha.base.modules.dataBaseModules
import br.com.brq.agatha.base.modules.repositoryModules
import br.com.brq.agatha.investimentos.modules.viewModelHomeModule
import br.com.brq.agatha.presentation.modules.viewCambioModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppAplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppAplication)
            modules(listOf(
                dataBaseModules,
                daoModules,
                repositoryModules,
                viewModelHomeModule,
                viewCambioModelModule
            ))
        }
    }
}