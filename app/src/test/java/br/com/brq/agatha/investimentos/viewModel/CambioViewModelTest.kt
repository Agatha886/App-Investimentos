package br.com.brq.agatha.investimentos.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brq.agatha.base.api.MoedaDbDataSource
import br.com.brq.agatha.base.api.UsuarioRepository
import br.com.brq.agatha.domain.util.ID_USUARIO
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
        viewModel = CambioViewModel(dbDataSource = dataSource, repositoryUsuario= repository, coroutinesContextProvider = TestContextProvider(), ID_USUARIO) // Cria instancia da ViewModel
    }

    @Test
    fun deveRetornarSucessoCompra_quandoSaldoDoUsuarioForIgualZeroAposOCalculoDeCompra(){
        val usuarioDeExemplo =
            br.com.brq.agatha.domain.model.Usuario(saldoDisponivel = BigDecimal(100))
        val moedaDeExemplo = br.com.brq.agatha.domain.model.Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10,
            variation = BigDecimal(1)
        )


        coEvery { repository.getUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        viewModel.compra(moedaDeExemplo, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoCompra("10"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarSucessoCompra_quandoSaldoDoUsuarioForMaiorQueZeroAposOCalculoDeCompra(){
        val usuarioDeExemplo =
            br.com.brq.agatha.domain.model.Usuario(saldoDisponivel = BigDecimal(150))
        val moedaDeExemplo = br.com.brq.agatha.domain.model.Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10,
            variation = BigDecimal(1)
        )

        coEvery { repository.getUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        viewModel.compra(moedaDeExemplo, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoCompra("10"), viewModel.viewEventRetornoCompraEVenda.value)
    }


    @Test
    fun deveRetornarFalhaCompra_quandoSaldoDoUsuarioForMenorQueZeroAposOCalculoDeCompra(){
        val usuarioDeExemplo =
            br.com.brq.agatha.domain.model.Usuario(saldoDisponivel = BigDecimal(50))
        val moedaDeExemplo = br.com.brq.agatha.domain.model.Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10,
            variation = BigDecimal(1)
        )

        coEvery { repository.getUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        viewModel.compra(moedaDeExemplo, "10")
        assertEquals(RetornoStadeCompraEVenda.FalhaCompra("Valor de Compra Inválido"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarSucessoVenda_quandoValorTotalDeMoedaForIgualAZeroAposOCalculoDeVenda(){
        val moedaDeExemplo = br.com.brq.agatha.domain.model.Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10,
            variation = BigDecimal(1)
        )

        coEvery { dataSource.buscaMoedaNoBando(moedaDeExemplo.name)} returns moedaDeExemplo
        viewModel.venda(moedaDeExemplo.name, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoVenda(0, "10"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarSucessoVenda_quandoTotalDeMoedasForMaiorQueZeroAposOCalculoDeVernda(){
        val moedaDeExemplo = br.com.brq.agatha.domain.model.Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 50,
            variation = BigDecimal(1)
        )

        coEvery { dataSource.buscaMoedaNoBando(moedaDeExemplo.name)} returns moedaDeExemplo
        viewModel.venda(moedaDeExemplo.name, "10")
        assertEquals(RetornoStadeCompraEVenda.SucessoVenda(40, "10"), viewModel.viewEventRetornoCompraEVenda.value)
    }

    @Test
    fun deveRetornarFalhaVenda_quandoValorTotalDeMoedasForMenorQueZeroAposOCalculoDaVenda(){
        val moedaDeExemplo = br.com.brq.agatha.domain.model.Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10,
            variation = BigDecimal(1)
        )

        coEvery { dataSource.buscaMoedaNoBando(moedaDeExemplo.name)} returns moedaDeExemplo
        viewModel.venda(moedaDeExemplo.name, "11")
        assertEquals(RetornoStadeCompraEVenda.FalhaVenda("Valor inválido"), viewModel.viewEventRetornoCompraEVenda.value)
    }


    @Test
    fun deveRetornarNada_quandoChamaFuncaoSetEventRetornoComoSem(){
        viewModel.setEventRetornoComoSem()
        assertEquals(RetornoStadeCompraEVenda.SemRetorno, viewModel.viewEventRetornoCompraEVenda.value)
    }



}