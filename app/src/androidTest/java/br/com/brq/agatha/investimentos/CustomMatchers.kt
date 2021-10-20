package br.com.brq.agatha.investimentos

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class CustomMatchers {
    companion object{
        fun withItemText(text: String, positionItem: Int): Matcher<View>{
            return object: BoundedMatcher<View, RecyclerView>(RecyclerView::class.java){

                //describeTo nos permite anexar nossa própria descrição ao matcher personalizado.
                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView with item Test: $text")
                }

                //matchSafely é onde implementamos a lógica de comparação para a contagem de itens.
                override fun matchesSafely(item: RecyclerView?): Boolean {
                    val viewHolder = item?.findViewHolderForAdapterPosition(positionItem)?.itemView
                    val view = viewHolder?.findViewById<TextView>(R.id.cardView_home_nome_moeda)
                    return view?.text.toString() == text
                }

            }

        }
    }
}