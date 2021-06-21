package com.example.mykotlinproject.model.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val name: String = getDefaultName(),
) : Parcelable

fun getDefaultName(): String {
    return "Ivan"
}
