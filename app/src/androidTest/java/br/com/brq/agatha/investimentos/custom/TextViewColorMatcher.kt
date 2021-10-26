package br.com.brq.agatha.investimentos.custom

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


class TextViewColorMatcher {
    companion object{
        fun verificaCor(corEsperada: Int): Matcher<View> {
            return object : BoundedMatcher<View, TextView>(TextView::class.java){
                override fun describeTo(description: Description?) {
                    description?.appendText("text color: ");
                    description?.appendValue(corEsperada);
                }

                override fun matchesSafely(textView: TextView?): Boolean {
                    val colorId = ContextCompat.getColor(textView?.context!!, corEsperada)
                    return textView.currentTextColor == colorId
                }
            }
                    
        }
    }
}