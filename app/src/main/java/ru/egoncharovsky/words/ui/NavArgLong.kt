package ru.egoncharovsky.words.ui

import android.os.Parcel
import android.os.Parcelable

/**
 * Nullable navigation argument box for long
 */
data class NavArgLong(val value: Long) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavArgLong> {
        override fun createFromParcel(parcel: Parcel): NavArgLong {
            return NavArgLong(parcel)
        }

        override fun newArray(size: Int): Array<NavArgLong?> {
            return arrayOfNulls(size)
        }
    }
}
