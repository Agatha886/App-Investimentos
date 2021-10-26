package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.custom.TextViewColorMatcher.Companion.verificaCor
import br.com.brq.agatha.investimentos.model.Moeda
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class)
class CambioActivityTest {
    private val moeda = Moeda(
        name = "Dollar",
        buy = BigDecimal(10),
        sell = BigDecimal(10),
        abreviacao = "USD",
        totalDeMoeda = 0.00,
        variation = BigDecimal(-1)
    )

    @Before
    fun before() {
        val intentCambio =
            Intent(ApplicationProvider.getApplicationContext(), CambioActivity::class.java)
        intentCambio.putExtra(CHAVE_MOEDA, moeda)
        launchActivity<CambioActivity>(intentCambio)
    }

    @Test
    fun deveVerificarOTituloDaAppBar_quandoCarregaATela() {
        onView(withId(R.id.include_my_toolbar))
            .check(matches(isDisplayed()))

        onView(withText("Câmbio"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun deveVerificarSeApareceAbreviacaoMoedaENome() {
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.cardView_cambio_abreviacao_nome_moeda),
                withParent(
                    withParent(
                        allOf(instanceOf(CardView::class.java), withId(R.id.cambio_cardView))
                    )
                )
            )).check(matches(withText("USD - Dollar")))
    }

    @Test
    fun deveVerificarSeApareceAVariacaoDaMoedaComACorDesejada(){
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.cardView_cambio_variation_moeda),
                withParent(
                    withParent(
                        allOf(instanceOf(CardView::class.java), withId(R.id.cambio_cardView))
                    )
                )
            ))
            .check(matches(withText("-1,00%")))
            .check(matches(verificaCor(R.color.red)))
    }
}
