package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import br.com.brq.agatha.investimentos.viewModel.base.CoroutinesContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(
    val dataSource: MoedaApiDataSource,
    private val coroutinesContextProvider: CoroutinesContextProvider,
    private val moedaWrapper: MoedaWrapper
) : ViewModel() {

    var quandoFinaliza: () -> Unit = {}

    private val eventRetornoDaApi = MutableLiveData<RetornoStadeApi>()

    val viewModelRetornoDaApi: LiveData<RetornoStadeApi> = eventRetornoDaApi

    private val io = CoroutineScope(coroutinesContextProvider.io)

    fun buscaDaApi() {
        io.launch {
            val moedasDoBanco = dataSource.buscaMoedasNoBanco()
            try {
                val financeDaApi = dataSource.getFinanceDaApi()
                atualizaBanco(moedasDoBanco, financeDaApi)
                val listaMoedadaApi = moedaWrapper.agrupaTodasAsMoedasNaLista(financeDaApi)
                setEventRetornoEFinalizaBusca(RetornoStadeApi.SucessoRetornoApi(listaMoedadaApi))
            } catch (e: Exception) {
                setEventRetornoEFinalizaBusca(RetornoStadeApi.SucessoRetornoBanco(moedasDoBanco))
            }
        }
    }

    private fun setEventRetornoEFinalizaBusca(retornoStadeApi: RetornoStadeApi) {
        eventRetornoDaApi.postValue(retornoStadeApi)
        quandoFinaliza()
    }

    private fun atualizaBanco(
        moedasDoBanco: List<Moeda>,
        financeDaApi: Finance?
    ) {
        dataSource.atualizaBancoDeDados(moedasDoBanco, financeDaApi)
    }

    class HomeViewModelFactory(
        private val context: Context,
        private val coroutinesContextProvider: CoroutinesContextProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(
                        dataSource = MoedaApiDataSource(context),
                        coroutinesContextProvider, MoedaWrapper()
                    ) as T
                }
                else -> {
                    throw IllegalArgumentException("Unknow ViewModel class")
                }
            }
        }
    }
}

