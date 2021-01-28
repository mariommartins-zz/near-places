package com.mariomartins.nearplaces.assertions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun withItemCount(count: Int): Matcher<View> = RecyclerViewCounterMatcher(count)

private class RecyclerViewCounterMatcher(private val count: Int) :
    BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("RecyclerView with item count: $count")
    }

    override fun matchesSafely(item: RecyclerView?) =
        item?.adapter?.itemCount == count
}
