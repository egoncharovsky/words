package ru.egoncharovsky.words.ui

import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <D> Fragment.observe(@NonNull data: LiveData<D>, observer: Observer<in D>) {
    data.observe(viewLifecycleOwner, observer)
}

fun Fragment.getColor(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(context!!, resId)
}