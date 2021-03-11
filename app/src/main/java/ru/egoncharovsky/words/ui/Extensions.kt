package ru.egoncharovsky.words.ui

import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

fun <D> Fragment.observe(@NonNull data: LiveData<D>, observer: Observer<in D>) {
    data.observe(viewLifecycleOwner, observer)
}

fun <D> Fragment.observe(@NonNull data: MutableLiveData<D>, observer: Observer<in D>) {
    data.observe(viewLifecycleOwner, observer)
}

fun Fragment.getColor(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(context!!, resId)
}

fun Fragment.getDrawable(@DrawableRes resId: Int): Drawable {
    return resources.getDrawable(resId, context!!.theme)
}

fun Menu.items(): List<MenuItem> = (0 until size()).map { i -> getItem(i) }.toList()

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <D> Fragment.observeNavigationResult(key: String = "result", observer: Observer<in D>) =
        getNavigationResultLiveData<D>(key)?.observe(viewLifecycleOwner, observer)

