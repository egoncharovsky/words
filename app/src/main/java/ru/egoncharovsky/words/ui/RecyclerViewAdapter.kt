package ru.egoncharovsky.words.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class RecyclerViewAdapter<T, VB : ViewBinding>(
    protected var values: List<T> = listOf()
) : RecyclerView.Adapter<RecyclerViewAdapter<T, VB>.ViewHolder>() {

    abstract val bindingInflate: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> VB

    abstract fun bind(binding: VB, item: T)

    open fun update(values: List<T>) {
        this.values = values
        notifyDataSetChanged()
    }

    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        bindingInflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(values[position])

    open inner class ViewHolder(protected val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) = bind(binding, item)
    }
}