package com.example.firebaseexample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmpData(
    var id : String? = null,
    var name : String? = null,
    var age : String? = null,
    var salary : String? = null
) : Parcelable

