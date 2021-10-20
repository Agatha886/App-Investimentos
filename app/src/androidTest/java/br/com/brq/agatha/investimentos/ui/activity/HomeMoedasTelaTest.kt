package br.com.brq.agatha.investimentos.ui.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.brq.agatha.investimentos.CustomAssertions
import br.com.brq.agatha.investimentos.CustomMatchers
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.ui.EspressoldlingResoruce
import br.com.brq.agatha.investimentos.ui.recyclerview.ListaMoedasAdpter
import br.com.brq.agatha.investimentos.ui.recyclerview.ListaMoedasAdpter.ListaMoedasViewHolder
import org.hamcrest.Matchers.allOf
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

    @Before
    fun registerIdlingResorce(){
        IdlingRegistry.getInstance().register(EspressoldlingResoruce.counting)
    }

    @After
    fun unregisterIdlingResorce(){
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
    fun deveApresentarORecyclerView_quandoAbreOApp() {
        onView(withId(R.id.home_recyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun deveRetornarAQuantidaeDeItensNaLlistaDoRecyclerView_quandoCarregaLista(){
        onView(withId(R.id.home_recyclerView))
            .check(CustomAssertions.hasItemCount(9))
    }

    @Test
    fun deveRetornarCadaAbreviacaoDaMoeda_quandoCarregaLista(){

        onView(withId(R.id.home_recyclerView))
            .check(ViewAssertions
                .matches(CustomMatchers.withItemText("USD",0)))
            .check(ViewAssertions
                .matches(CustomMatchers.withItemText("JPY",1)))
            .check(ViewAssertions
                .matches(CustomMatchers.withItemText("GBP",2)))
            .check(ViewAssertions
                .matches(CustomMatchers.withItemText("EUR",3)))
            .check(ViewAssertions
                .matches(CustomMatchers.withItemText("CNY",4)))
    }

}