package br.com.brq.agatha.investimentos.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brq.agatha.investimentos.viewModel.base.AppContextProvider
import br.com.brq.agatha.investimentos.viewModel.base.TestContextProvider
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get : Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun deveRetornarSucesso_quandoConsegueBuscarDaApi() {
        //Arrange
        MockKAnnotations.init(this, relaxUnitFun = true)
        val dataSource = mockk<MoedaApiDataSource>()
        val viewModel = spyk(HomeViewModel(dataSource))

        val testContextProvider = TestContextProvider()
        AppContextProvider.coroutinesContextProviderDelegate = testContextProvider

        AppContextProvider.coroutinesContextProviderDelegate.run {
            viewModel.buscaDaApi()
            assertEquals(true, viewModel.entrouNoio)
        }


    }
}

