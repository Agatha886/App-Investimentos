package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.math.BigDecimal

class HomeViewModelTest{
    @get : Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    @Mock
    private lateinit var retornoDaApiLiveDateObserver: Observer<RetornoStadeApi>

    private val moeda1 = Moeda(
        name = "teste1",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "teste",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )

    private val moeda2 = Moeda(
        name = "teste2",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "teste",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )

    private val moeda3 = Moeda(
        name = "teste3",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "teste",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )


    @Test
    fun deveRetornarSucesso_quandoConsegueBuscarDaApi() {
        //Arrange
        val listaMoedaTeste = listOf(moeda1, moeda2, moeda3)
        val contex = Mockito.mock(Context::class.java)
        val dataSourceMoeda = Mockito.spy(MoedaApiDataSource(contex))
        val finance = Mockito.mock(Finance::class.java)
        val eventRetornoDaApi = MutableLiveData<RetornoStadeApi>()

        viewModel = HomeViewModel(dataSourceMoeda)

        Mockito.doNothing().`when`(dataSourceMoeda)
            .atualizaBancoDeDados(listaMoedaTeste, finance)

//        Mockito.doNothing().`when`(dataSourceMoeda).agrupaTodasAsMoedasNaLista(finance)

        eventRetornoDaApi.observeForever(retornoDaApiLiveDateObserver)

        //Arc
        viewModel.buscaDaApi()

        //Assert
        Mockito.verify(retornoDaApiLiveDateObserver)
            .onChanged(RetornoStadeApi.Sucesso(dataSourceMoeda.listaMoedasDaApi))

    }

    @Test
    fun deveRetornarSucesso_quandoConsegueBuscarDaApiNoDataSorce() {
        //Arrange
        val listaMoedaTeste = listOf(moeda1, moeda2, moeda3)
        val contex = Mockito.mock(Context::class.java)
        val dataSourceMoeda = Mockito.spy(MoedaApiDataSource(contex))
        val finance = Mockito.mock(Finance::class.java)
        val eventRetornoDaApi = MutableLiveData<RetornoStadeApi>()

        Mockito.doNothing().`when`(dataSourceMoeda)
            .atualizaBancoDeDados(listaMoedaTeste, finance)

        //Arc
        dataSourceMoeda.buscaDaApi {eventRetornoDaApi.value = it}

        //Assert
        TestCase.assertEquals(RetornoStadeApi.Sucesso(dataSourceMoeda.listaMoedasDaApi), eventRetornoDaApi.value)
    }

}