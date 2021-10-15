package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import br.com.brq.agatha.investimentos.viewModel.base.AppContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(val dataSource: MoedaApiDataSource) : ViewModel() {

    var quandoFinaliza: () -> Unit = {}

    private val eventRetornoDaApi = MutableLiveData<RetornoStadeApi>()

    val viewModelRetornoDaApi: LiveData<RetornoStadeApi> = eventRetornoDaApi

    val io = CoroutineScope(AppContextProvider.io)

    val listaMoedaApi = mutableListOf<Moeda>()
    var entrouNoio = false


    fun buscaDaApi() {
        io.launch {
            val moedasDoBanco = dataSource.buscaMoedasNoBanco()
            try {
                val financeDaApi = dataSource.getFinanceDaApi()
                atualizaBanco(moedasDoBanco, financeDaApi)
                dataSource.agrupaTodasAsMoedasNaLista(financeDaApi, listaMoedaApi)
                entrouNoio = true
                withContext(AppContextProvider.main) {
                    eventRetornoDaApi.value = RetornoStadeApi.Sucesso(listaMoedaApi)
                    quandoFinaliza()
                    entrouNoio = true
                }
            } catch (e: Exception) {
                eventRetornoDaApi.postValue(RetornoStadeApi.FalhaApi(moedasDoBanco))
                quandoFinaliza()
            }
        }
    }

    fun atualizaBanco(
        moedasDoBanco: List<Moeda>,
        financeDaApi: Finance?
    ) {
        dataSource.atualizaBancoDeDados(moedasDoBanco, financeDaApi)
    }

    class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(dataSource = MoedaApiDataSource(context)) as T
                }
                else -> {
                    throw IllegalArgumentException("Unknow ViewModel class")
                }
            }
        }
    }
}

