package br.com.brq.agatha.investimentos

import android.app.Application
import br.com.brq.agatha.investimentos.di.modules.daoModules
import br.com.brq.agatha.investimentos.di.modules.dataBaseModules
import br.com.brq.agatha.investimentos.di.modules.repositoryModules
import br.com.brq.agatha.investimentos.di.modules.viewModelModules
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
                viewModelModules
            ))
        }
    }
}