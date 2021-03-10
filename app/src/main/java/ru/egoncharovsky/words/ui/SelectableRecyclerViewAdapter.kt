package ru.egoncharovsky.words.ui

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import ru.egoncharovsky.words.R

abstract class SelectableRecyclerViewAdapter<T>(
    val selectionByShortPress: Boolean = true,
    val multiSelection: Boolean = true
) : RecyclerViewAdapter<T>() {

    private var tracker: SelectionTracker<Long>? = null

    init {
        @Suppress("LeakingThis")
        setHasStableIds(true)
    }

    abstract fun bind(itemView: View, item: T, isActivated: Boolean)

    final override fun bind(itemView: View, item: T) = bind(itemView, item, false)

    fun observe(recyclerView: RecyclerView, observer: Observer<List<T>>) {
        if (tracker == null) {
            tracker = SelectionTracker.Builder(
                "selection:${toString()}",
                recyclerView,
                StableIdKeyProvider(recyclerView),
                ItemDetailsLookupLong(recyclerView),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                if (multiSelection) {
                    SelectionPredicates.createSelectAnything()
                } else {
                    SelectionPredicates.createSelectSingleAnything()
                }
            ).build()
        }
        tracker!!.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                observer.onChanged(tracker!!.selection.toList().map { values[it.toInt()] })
            }
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selected = tracker?.run { isSelected(position.toLong()) } ?: false
        (holder as SelectableViewHolder).bind(values[position], selected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fragment_choose_words_item, parent, false)
        return SelectableViewHolder(itemView)
    }

    override fun getItemCount(): Int = values.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class SelectableViewHolder(itemView: View) : ViewHolder(itemView) {

        fun bind(value: T, isActivated: Boolean = false) {
            itemView.isActivated = isActivated
            bind(itemView, value, isActivated)
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long = itemId
                override fun inSelectionHotspot(e: MotionEvent): Boolean = selectionByShortPress
            }
    }

    class ItemDetailsLookupLong(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
            return recyclerView.findChildViewUnder(event.x, event.y)?.let {
                (recyclerView.getChildViewHolder(it) as SelectableRecyclerViewAdapter<*>.SelectableViewHolder).getItemDetails()
            }
        }
    }
}