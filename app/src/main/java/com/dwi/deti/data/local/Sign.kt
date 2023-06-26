package com.dwi.deti.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sign(
    var id: String,
    var letter: String,
    var image: String,
) : Parcelable
