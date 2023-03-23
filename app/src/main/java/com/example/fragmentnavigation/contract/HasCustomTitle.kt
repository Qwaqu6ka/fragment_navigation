package com.example.fragmentnavigation.contract

import androidx.annotation.StringRes
/*
*   Implement this interface in your fragment if you want to override
*/

interface HasCustomTitle {
    /*
    *   return string resource witch should be displayed instead of default title
    */
    @StringRes
    fun getTitleRes(): Int
}