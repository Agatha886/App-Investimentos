package br.com.brq.agatha.base.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brq.agatha.base.database.dao.UsuarioDao
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
        usuarioRepository = UsuarioRepository(usuarioDao)
    }

    @Test
    fun deveRetornarUsuario_quandoChamaUsario() {
        val usuarioDeExemplo =
            br.com.brq.agatha.domain.model.Usuario(saldoDisponivel = BigDecimal(150))
        every { usuarioDao.retornaUsuario(usuarioDeExemplo.id) } returns usuarioDeExemplo
        assertEquals(usuarioDeExemplo, usuarioRepository.getUsuario(usuarioDeExemplo.id))
    }

}