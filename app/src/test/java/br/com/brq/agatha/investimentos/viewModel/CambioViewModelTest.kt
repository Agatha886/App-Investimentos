package br.com.brq.agatha.investimentos.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.repository.MoedaDbDataSource
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import br.com.brq.agatha.investimentos.viewModel.base.TestContextProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class) // Para ter certeza que o teste irá rodar no JUnit4
class CambioViewModelTest {
    @get : Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var dataSource: MoedaDbDataSource

    @MockK
    private lateinit var repository: UsuarioRepository

    private lateinit var viewModel: CambioViewModel


    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true) // Inicializa todas as anotações e relaxUnitFun faz com que todas as funções já retornem nada
        viewModel = CambioViewModel(dbDataSource = dataSource, repositoryUsuario= repository, coroutinesContextProvider = TestContextProvider()) // Cria instancia da ViewModel
    }

    @Test
    fun deveRetornarSucessoCompra_quandoSaldoDoUsuarioForIgualZeroAposOCalculoDeCompra(){
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(100))
        val moedaDeExemplo = Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10.00,
            variation = BigDecimal(1)
        )


        coEvery { repository.getUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        viewModel.compra(usuarioDeExemplo.id,moedaDeExemplo, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoCompra("10"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarSucessoCompra_quandoSaldoDoUsuarioForMaiorQueZeroAposOCalculoDeCompra(){
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(150))
        val moedaDeExemplo = Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10.00,
            variation = BigDecimal(1)
        )

        coEvery { repository.getUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        viewModel.compra(usuarioDeExemplo.id,moedaDeExemplo, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoCompra("10"), viewModel.viewEventRetornoCompraEVenda.value)
    }


    @Test
    fun deveRetornarFalhaCompra_quandoSaldoDoUsuarioForMenorQueZeroAposOCalculoDeCompra(){
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(50))
        val moedaDeExemplo = Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10.00,
            variation = BigDecimal(1)
        )

        coEvery { repository.getUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        viewModel.compra(usuarioDeExemplo.id,moedaDeExemplo, "10")
        assertEquals(RetornoStadeCompraEVenda.FalhaCompra("Valor de Compra Inválido"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarSucessoVenda_quandoValorTotalDeMoedaForIgualAZeroAposOCalculoDeVenda(){
        val moedaDeExemplo = Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10.00,
            variation = BigDecimal(1)
        )

        coEvery { dataSource.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        viewModel.venda(moedaDeExemplo.name, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoVenda(0.00, "10"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarSucessoVenda_quandoTotalDeMoedasForMaiorQueZeroAposOCalculoDeVernda(){
        val moedaDeExemplo = Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 50.00,
            variation = BigDecimal(1)
        )

        coEvery { dataSource.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        viewModel.venda(moedaDeExemplo.name, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoVenda(40.00, "10"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarFalhaVenda_quandoValorTotalDeMoedasForMenorQueZeroAposOCalculoDaVenda(){
        val moedaDeExemplo = Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10.00,
            variation = BigDecimal(1)
        )

        coEvery { dataSource.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        viewModel.venda(moedaDeExemplo.name, "11")
        assertEquals(RetornoStadeCompraEVenda.FalhaVenda("Valor inválido"), viewModel.viewEventRetornoCompraEVenda.value)
    }


    @Test
    fun deveRetornarNada_quandoChamaFuncaoSetEventRetornoComoSem(){
        viewModel.setEventRetornoComoSem()
        assertEquals(RetornoStadeCompraEVenda.SemRetorno, viewModel.viewEventRetornoCompraEVenda.value)
    }



}