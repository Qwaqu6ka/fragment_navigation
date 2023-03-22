package com.example.fragmentnavigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(
    val boxCount: Int,
    val isTimerEnabled: Boolean
) : Parcelable {

    companion object {
        val DEFAULT = Options(3, false)
    }
}
