package br.com.brq.agatha.investimentos.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.rule.IntentsTestRule

import br.com.brq.agatha.data.repository.MoedaApiDataSource
import br.com.brq.agatha.investimentos.ui.activity.HomeMoedasActivity
import br.com.brq.agatha.investimentos.viewModel.base.TestContextProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest {
    @get : Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val intentsTestRule = IntentsTestRule(HomeMoedasActivity::class.java)

    @MockK
    private lateinit var dataSource: MoedaApiDataSource

    @MockK
    private lateinit var finance: br.com.brq.agatha.domain.model.Finance

    @MockK
    private lateinit var moedaWrapper: MoedaWrapper

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = HomeViewModel(dataSource,TestContextProvider(),moedaWrapper)
    }

    @Test
    fun deveRetornarSucessoApi_quandoConsegueBuscarDaApi() {
        val listaMoedas = listOf<br.com.brq.agatha.domain.model.Moeda>()
        coEvery { dataSource.buscaTodasMoedasNoBanco()} returns listaMoedas
        coEvery { dataSource.getFinanceDaApi()} returns finance
        every { moedaWrapper.agrupaTodasAsMoedasNaLista(finance) } returns listaMoedas
        viewModel.buscaDaApi()
        assertEquals(RetornoStadeApi.SucessoRetornoApi(listaMoedas), viewModel.viewModelRetornoDaApi.value)
    }


    @Test
    fun deveRetornarSucessoBanco_quandoDarFalhaAoBuscarDaApi(){
        val listaMoedas = listOf<br.com.brq.agatha.domain.model.Moeda>()
        coEvery{ dataSource.buscaTodasMoedasNoBanco()} returns listaMoedas
        coEvery { dataSource.getFinanceDaApi()} throws Exception("Deu exceção")
        viewModel.buscaDaApi()
        assertEquals(RetornoStadeApi.SucessoRetornoBanco(listaMoedas), viewModel.viewModelRetornoDaApi.value)
    }


    @Test
    fun deveRealizarFunçoesEspecificas_quandoConsegueBuscarDaApi(){
        val listaMoedas = listOf<br.com.brq.agatha.domain.model.Moeda>()
        coEvery { dataSource.buscaTodasMoedasNoBanco()} returns listaMoedas
        coEvery { dataSource.getFinanceDaApi()} returns finance
        every { moedaWrapper.agrupaTodasAsMoedasNaLista(finance) } returns listaMoedas
        viewModel.buscaDaApi()

        verifySequence{
            dataSource.buscaTodasMoedasNoBanco()
            dataSource.getFinanceDaApi()
            dataSource.atualizaBancoDeDados(listaMoedas, finance)
            moedaWrapper.agrupaTodasAsMoedasNaLista(finance)
            viewModel.quandoFinaliza
        }
    }

    @Test
    fun deveRealizarFunçoesEspecificas_quandoDarFalhaAoBuscarDaApi(){
        val listaMoedas = listOf<br.com.brq.agatha.domain.model.Moeda>()
        coEvery{ dataSource.buscaTodasMoedasNoBanco()} returns listaMoedas
        coEvery { dataSource.getFinanceDaApi()} throws Exception("Deu exceção")
        every { moedaWrapper.agrupaTodasAsMoedasNaLista(finance) } returns listaMoedas
        viewModel.buscaDaApi()

        verifySequence{
            dataSource.buscaTodasMoedasNoBanco()
            dataSource.getFinanceDaApi()
            viewModel.quandoFinaliza
        }
    }
}

