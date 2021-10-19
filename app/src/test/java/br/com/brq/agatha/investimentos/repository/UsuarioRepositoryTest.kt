package br.com.brq.agatha.investimentos.repository

import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.viewModel.base.TestContextProvider
import io.mockk.MockKAnnotations
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal


@RunWith(JUnit4::class)
class UsuarioRepositoryTest {

    private lateinit var usuarioRepository: UsuarioRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
//        usuarioRepository = UsuarioRepository(teste, TestContextProvider())
    }

    @Test
    fun deveRetornarUsuario_quandoChamaUsario() {
        val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal(150))
        usuarioRepository.getUsuario(usuarioDeExemplo.id)
        assertEquals(usuarioDeExemplo, usuarioRepository.getUsuario(usuarioDeExemplo.id))
    }
}