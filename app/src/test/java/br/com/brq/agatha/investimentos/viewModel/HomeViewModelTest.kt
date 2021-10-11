package br.com.brq.agatha.investimentos.viewModel

import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import java.math.BigDecimal


class HomeViewModelTest : TestCase() {
    //    @get : Rule
//    val rule = InstantTaskExecutorRule()
    private lateinit var viewModel: HomeViewModel

    val moeda1 = Moeda(
        name = "teste1",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "teste",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )

    val moeda2 = Moeda(
        name = "teste2",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "teste",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )

    val moeda3 = Moeda(
        name = "teste3",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "teste",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )

    @Mock
    

    @Test
    fun deveRetornarSucesso_quandoConsegueBuscarDaApi() {
        //Arrange
        val listaMoedaTeste = listOf<Moeda>(moeda1,moeda2,moeda3)

        val resultSucess = RetornoStadeApi.Sucesso(listaMoedaTeste)
        viewModel = HomeViewModel(Mockito.mock(MoedaApiDataSource::class.java))

        //Arc
        viewModel.buscaDaApi()

        //Assert



    }

}