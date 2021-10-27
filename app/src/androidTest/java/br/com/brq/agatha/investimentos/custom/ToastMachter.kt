package br.com.brq.agatha.investimentos.custom

import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class ToastMachter : TypeSafeMatcher<Root>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is Toast")
    }
    override fun matchesSafely(root: Root?): Boolean {
        val viewTitle = (root?.decorView?.layoutParams as WindowManager.LayoutParams).title
        if (viewTitle == "Toast") {
            val windowToken: IBinder = root.decorView.windowToken
            val appToken: IBinder = root.decorView.applicationWindowToken
            if (windowToken == appToken) {
                return true
            }
        }
        return false
    }
}