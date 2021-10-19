package br.com.brq.agatha.investimentos.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.viewModel.base.TestContextProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal


@RunWith(JUnit4::class)
class UsuarioRepositoryTest {

    @get : Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var usuarioRepository: UsuarioRepository

    @MockK
    private lateinit var usuarioDao: UsuarioDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        usuarioRepository = UsuarioRepository(usuarioDao, TestContextProvider())
    }

    @Test
    fun deveRetornarUsuario_quandoChamaUsario() {
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(150))
        every { usuarioDao.retornaUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        assertEquals(usuarioDeExemplo, usuarioRepository.getUsuario(usuarioDeExemplo.id))
    }

    @Test
    fun deveRetornarUmLiveDateComOSaldoDoUsario_quandoChamaOGetSaldoDisponivel() {
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(150))
        every { usuarioDao.retornaUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        assertEquals(
            BigDecimal(150),
            usuarioRepository.getSaldoDisponivel(usuarioDeExemplo.id).value
        )
    }

    @Test
    fun deveSetarSaldoDoUsaurioERetornarNovoSaldo_quandoChamaFuncaoSetSaldoERetornaSaldo() {
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(150))
        every { usuarioDao.retornaUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        val saldoERetornaSaldo =
            usuarioRepository.setSaldoERetornaSaldo(usuarioDeExemplo.id, BigDecimal(50))
        assertEquals(BigDecimal(50), usuarioDeExemplo.saldoDisponivel)
    }

    @Test
    fun deveSetarSaldoDoUsaurioAposAVendaERetornarEsseSaldo_quandoChamaFuncaoSetSaldoVendaERetornaSaldo() {
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(150))
        val moedaDeExemplo = Moeda(
            name = "Dollar",
            buy = BigDecimal.TEN,
            sell = BigDecimal.TEN,
            abreviacao = "USD",
            totalDeMoeda = 10.00,
            variation = BigDecimal(1)
        )
        every { usuarioDao.retornaUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        val retornaSaldoDoUsarioAposVenda =
            usuarioRepository.setSaldoVendaERetornaSaldo(usuarioDeExemplo.id, moedaDeExemplo, "10")
        assertEquals(BigDecimal(250), retornaSaldoDoUsarioAposVenda.value)
    }
    

}