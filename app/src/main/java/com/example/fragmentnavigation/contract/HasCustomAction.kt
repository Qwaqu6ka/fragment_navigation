package com.example.fragmentnavigation.contract

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/*
*   Implement this interface if you want to show additional action in the toolbar
*   while this fragment displayed to user
*/
interface HasCustomAction {
    fun getCustomAction(): CustomAction
}

class CustomAction(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val onCustomAction: Runnable
)