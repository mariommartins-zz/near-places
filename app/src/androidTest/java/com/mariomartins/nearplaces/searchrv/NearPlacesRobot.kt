package com.mariomartins.nearplaces.searchrv

import com.mariomartins.nearplaces.R
import com.mariomartins.nearplaces.robot.matchGone
import com.mariomartins.nearplaces.robot.matchVisible
import com.mariomartins.nearplaces.robot.matchWithItemCount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

object NearPlacesRobot {
    fun matchEmptyStateIsVisible() {
        matchVisible(R.id.empty_state_image_iv)
        matchVisible(R.id.empty_state_title_tv)
    }

    fun matchEmptyStateIsGone() {
        matchGone(R.id.empty_state_image_iv)
        matchGone(R.id.empty_state_title_tv)
    }

    fun matchAmountOfListedItems(amount: Int) =
        matchWithItemCount(R.id.near_places_results_rv, amount)
}

@ExperimentalCoroutinesApi
fun nearPlacesState(block: suspend NearPlacesRobot.() -> Unit) =
    runBlockingTest { NearPlacesRobot.block() }
