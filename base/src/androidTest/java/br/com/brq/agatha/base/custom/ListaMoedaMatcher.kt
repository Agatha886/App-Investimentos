package br.com.brq.agatha.base.custom

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import br.com.brq.agatha.investimentos.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.lang.IndexOutOfBoundsException

class ListaMoedaMatcher {

    fun apresentaMoeda(text: String, positionItem: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

            //describeTo nos permite anexar nossa própria descrição ao matcher personalizado.
            override fun describeTo(description: Description?) {
                description?.appendText("Item com a descrição: $text na posição " +
                        "$positionItem não foi encontrado")
            }

            //matchSafely é onde implementamos a lógica de comparação para a contagem de itens.
            override fun matchesSafely(item: RecyclerView?): Boolean {
                val viewHolder = item?.findViewHolderForAdapterPosition(positionItem)?.itemView
                    ?: throw IndexOutOfBoundsException("Item na posição $positionItem não foi encontrado")

                return verificaCampoAbreviacaoMoeda(viewHolder, text) && verificaCampoVariacaoMoeda(viewHolder)
            }
        }
    }

    fun verificaVariacao(valorEsperado: String, positionItem: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

            //describeTo nos permite anexar nossa própria descrição ao matcher personalizado.
            override fun describeTo(description: Description?) {
                description?.appendText("Item com a variação: $valorEsperado na posição " +
                        "$positionItem não foi encontrado")
            }

            //matchSafely é onde implementamos a lógica de comparação para a contagem de itens.
            override fun matchesSafely(item: RecyclerView?): Boolean {
                val viewHolder = item?.findViewHolderForAdapterPosition(positionItem)?.itemView
                    ?: throw IndexOutOfBoundsException("Item na posição $positionItem não foi encontrado")

                return verificaTextoAbreviacaoMoeda(viewHolder, valorEsperado) && verificaCampoVariacaoMoeda(viewHolder)
            }
        }
    }

    private fun verificaTextoAbreviacaoMoeda(viewHolder: View, text: String): Boolean{
        val view = viewHolder.findViewById<TextView>(R.id.cardView_home_variation_moeda)
        return view?.text.toString() == text
    }

    private fun verificaCampoAbreviacaoMoeda(
        viewHolder: View,
        text: String
    ): Boolean {
        val view = viewHolder.findViewById<TextView>(R.id.cardView_home_nome_moeda)
        return view?.text.toString() == text
    }

    private fun verificaCampoVariacaoMoeda(viewHolder: View): Boolean {
        val view = viewHolder.findViewById<TextView>(R.id.cardView_home_variation_moeda)
        if(view?.isVisible == true && verificaCorDoCampoVariacaoMoeda(view, viewHolder)) return true
        else throw  NullPointerException("Campo variação do item não foi encontrado ou estar com problemas")
    }

    private fun verificaCorDoCampoVariacaoMoeda(view: TextView?, viewHolder: View): Boolean {
        val context = viewHolder.context
        val colorRed: Int = ContextCompat.getColor(context, R.color.red)
        val colorWhite: Int = ContextCompat.getColor(context, R.color.white)
        val colorVerde = ContextCompat.getColor(context, R.color.verde)

        return when(view?.currentTextColor){
            colorRed -> true
            colorVerde -> true
            colorWhite -> true
            else -> false
        }
    }

}