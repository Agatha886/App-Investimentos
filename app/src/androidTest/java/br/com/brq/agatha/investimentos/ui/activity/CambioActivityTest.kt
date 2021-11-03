package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.domain.util.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.custom.TextViewColorMatcher.Companion.verificaCor
import br.com.brq.agatha.domain.model.Moeda
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class)
class CambioActivityTest {
    private val moeda = br.com.brq.agatha.domain.model.Moeda(
        name = "Dollar",
        buy = BigDecimal(10),
        sell = BigDecimal(10),
        abreviacao = "USD",
        totalDeMoeda = 0,
        variation = BigDecimal(-1)
    )

    @Before
    fun before() {
        val intentCambio =
            Intent(ApplicationProvider.getApplicationContext(), CambioActivity::class.java)
        intentCambio.putExtra(br.com.brq.agatha.domain.util.CHAVE_MOEDA, moeda)
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
    fun deveVerificarSeApareceAbreviacaoMoedaENome_quandoCarregaATela() {
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
    fun deveVerificarSeApareceAVariacaoDaMoedaComACorDesejada_quandoCarregaATela(){
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

    @Test
    fun deveVerificarSeApareceOValorDeCompraMoeda_quandoCarregaATela(){
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.cardView_cambio_valor_compra_moeda),
                withParent(
                    withParent(
                        allOf(instanceOf(CardView::class.java), withId(R.id.cambio_cardView))
                    )
                )
            )).check(matches(withText("Compra: R\$ 10,00")))
    }

    @Test
    fun deveVerificarSeApareceOValorDeVendaMoeda_quandoCarregaATela(){
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.cardView_cambio_valor_venda_moeda),
                withParent(
                    withParent(
                        allOf(instanceOf(CardView::class.java), withId(R.id.cambio_cardView))
                    )
                )
            )).check(matches(withText("Venda: R\$ 10,00")))
    }

    @Test
    fun deveVerificarSeApareceOSaldoDisponivelDoUsuario_quandoCarregaATela(){
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.cambio_saldo_disponivel),
                withParent(
                  instanceOf(ConstraintLayout::class.java)
                )
            )).check(matches(withText("R\$ 5.672,33")))
    }

    @Test
    fun deveVerificarSeApareceOTotalDeMoedasNaFormatacaoDaMoeda_quandoCarregaATela(){
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.cambio_saldo_moeda),
                withParent(
                    instanceOf(ConstraintLayout::class.java)
                )
            )).check(matches(withText("US\$ 801.00")))
    }

    @Test
    fun deveVerificarSeApareceOEditTextQuantidadeNaTela_quandoCarregaATela(){
        onView(
            allOf(
                withId(R.id.cambio_quantidade),
                withParent(withParent(withId(R.id.activity_cambio_container))),
                isDisplayed()
            )
        ).check(matches(withText("Quantidade")))
    }

    @Test
    fun deveVerificarSeApareceOBotaoVender(){
        onView(
            allOf(
                withId(R.id.cambio_button_vender), withText("VENDER"),
                withParent(withParent(withId(R.id.activity_cambio_container))),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun deveVerificarSeApareceOBotaoComprar(){
        onView(
            allOf(
                withId(R.id.cambio_button_comprar), withText("COMPRAR"),
                withParent(withParent(withId(R.id.activity_cambio_container))),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
    }




}
