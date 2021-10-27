package br.com.brq.agatha.investimentos.ui.activity


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.brq.agatha.investimentos.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeMoedasActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(HomeMoedasActivity::class.java)

    @Test
    fun homeMoedasActivityTest2() {
        val recyclerView = onView(
            allOf(
                withId(R.id.home_recyclerView),
                childAtPosition(
                    withId(R.id.home_swipe),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val editText = onView(
            allOf(
                withId(R.id.cambio_quantidade), withText("Quantidade"),
                withParent(withParent(withId(R.id.activity_cambio_container))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("Quantidade")))

        val button = onView(
            allOf(
                withId(R.id.cambio_button_vender), withText("VENDER"),
                withParent(withParent(withId(R.id.activity_cambio_container))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.cambio_button_comprar), withText("COMPRAR"),
                withParent(withParent(withId(R.id.activity_cambio_container))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withId(R.id.cambio_button_comprar), withText("COMPRAR"),
                withParent(withParent(withId(R.id.activity_cambio_container))),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
