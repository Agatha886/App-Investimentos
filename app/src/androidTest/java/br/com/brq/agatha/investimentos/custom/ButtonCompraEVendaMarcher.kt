package br.com.brq.agatha.investimentos.custom

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


class ButtonCompraEVendaMarcher {
    companion object {
        fun comparaCorBotao(drawable: Drawable): Matcher<View> {
            return object : BoundedMatcher<View, Button>(Button::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("Item não possui o drawable: ${drawable.constantState}")
                }

                override fun matchesSafely(item: Button?): Boolean {
                    val buttonColor: Drawable.ConstantState? = item?.background?.constantState
                    Log.i("TAG", "matchesSafely: $buttonColor")
                    return (buttonColor == drawable.constantState)
                }

            }
        }
    }
}