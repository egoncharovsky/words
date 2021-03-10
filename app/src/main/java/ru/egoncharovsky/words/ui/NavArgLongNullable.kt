package ru.egoncharovsky.words.ui

import android.os.Parcel
import android.os.Parcelable

data class NavArgLongNullable(val value: Long) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavArgLongNullable> {
        override fun createFromParcel(parcel: Parcel): NavArgLongNullable {
            return NavArgLongNullable(parcel)
        }

        override fun newArray(size: Int): Array<NavArgLongNullable?> {
            return arrayOfNulls(size)
        }
    }
}
