package br.com.brq.agatha.investimentos.ui.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.custom.CustomAssertions
import br.com.brq.agatha.investimentos.custom.CustomMatchers
import br.com.brq.agatha.investimentos.ui.EspressoldlingResoruce
import br.com.brq.agatha.investimentos.ui.recyclerview.ListaMoedasAdpter.ListaMoedasViewHolder
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeMoedasTelaTest {

    @get : Rule
    val rule = ActivityScenarioRule(HomeMoedasActivity::class.java)

    private val customMatchers = CustomMatchers()

    @Before
    fun registerIdlingResorce() {
        IdlingRegistry.getInstance().register(EspressoldlingResoruce.counting)
    }

    @After
    fun unregisterIdlingResorce() {
        IdlingRegistry.getInstance().unregister(EspressoldlingResoruce.counting)
    }

    @Test
    fun deveApresentarOTextoDeTituloDaAppBar_quandoCarregaATela() {
        onView(ViewMatchers.withText("Home/Moeda"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.include_my_toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun deveApresentarORecyclerView_quandoAbreATela() {
        onView(withId(R.id.home_recyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun deveRetornarAQuantidaeDeItensNaLlistaDoRecyclerView_quandoCarregaLista() {
        onView(withId(R.id.home_recyclerView))
            .check(CustomAssertions.hasItemCount(9))
    }

    @Test
    fun deveVerificarCadaMoeda_quandoCarregaAParteVisivelDaLista() {
        onView(withId(R.id.home_recyclerView))
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("USD", 0))
            )
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("JPY", 1))
            )
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("GBP", 2))
            )
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("EUR", 3))
            )
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("CNY", 4))
            )
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("CAD", 5))
            )
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("BTC", 6))
            )
    }

    @Test
    fun deveVerificarCadaMoeda_quandoCarregaTodaLista() {
        deveVerificarCadaMoeda_quandoCarregaAParteVisivelDaLista()

        onView(withId(R.id.home_recyclerView))
            .perform(RecyclerViewActions.scrollToPosition<ListaMoedasViewHolder>(8))
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("AUD", 7))
            )
            .check(
                ViewAssertions
                    .matches(customMatchers.apresentaMoeda("ARS", 8))
            )
    }

    @Test
    fun deveRetornarIndexOutOfBoundsException_quandoIndexDaMoedaIndicadoNoTesteNaoTemNaLista() {
        try {
            onView(withId(R.id.home_recyclerView))
                .check(
                    ViewAssertions
                        .matches(customMatchers.apresentaMoeda("ITEM", 9))
                )
        }catch (e: IndexOutOfBoundsException){
            assertEquals("Item na posição 9 não foi encontrado", e.message)
        }
    }


}