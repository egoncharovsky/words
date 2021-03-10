package ru.egoncharovsky.words.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewAdapter<T>(
    protected var values: List<T> = listOf()
) : RecyclerView.Adapter<RecyclerViewAdapter<T>.ViewHolder>() {

    @get:LayoutRes
    abstract val itemLayoutId: Int

    abstract fun bind(itemView: View, item: T)

    fun update(values: List<T>) {
        this.values = values
        notifyDataSetChanged()
    }

    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(values[position])

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: T) = bind(itemView, item)
    }
}