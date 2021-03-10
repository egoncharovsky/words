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
    val multiSelection: Boolean = true,
    val persistenceSelection: Boolean = true
) : RecyclerViewAdapter<T>() {

    var tracker: SelectionTracker<Long>? = null
        private set

    abstract fun bind(itemView: View, item: T, isActivated: Boolean)

    abstract fun getIdentifier(item: T): Long

    final override fun bind(itemView: View, item: T) = bind(itemView, item, false)

    fun observe(recyclerView: RecyclerView, observer: Observer<List<Long>>) {
        if (tracker == null) {
            val keyProvider = ItemKeyProviderLong()
            tracker = SelectionTracker.Builder(
                "selection",
                recyclerView,
                keyProvider,
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
            override fun onItemStateChanged(key: Long, selected: Boolean) {
                if (values.indexOfFirst { getIdentifier(it) == key } != RecyclerView.NO_POSITION) {
                    observer.onChanged(tracker!!.selection.toList())
                }
            }
        })
    }

    override fun update(values: List<T>) {
        if (persistenceSelection) {
            val selected = tracker?.selection?.toSet()
            super.update(values)
            tracker?.run {
                if (selected != null && selected.isNotEmpty()) {
                    setItemsSelected(selected, true)
                }
            }
        } else {
            super.update(values)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = values[position]

        val selected = tracker?.run { isSelected(getIdentifier(entry)) } ?: false
        (holder as SelectableViewHolder).bind(entry, selected)
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
                override fun getPosition(): Int = absoluteAdapterPosition
                override fun getSelectionKey(): Long = getIdentifier(values[position])
                override fun inSelectionHotspot(e: MotionEvent): Boolean = selectionByShortPress
            }
    }

    inner class ItemDetailsLookupLong(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
            return recyclerView.findChildViewUnder(event.x, event.y)?.let {
                (recyclerView.getChildViewHolder(it) as SelectableRecyclerViewAdapter<*>.SelectableViewHolder)
                    .getItemDetails()
            }
        }
    }

    inner class ItemKeyProviderLong : ItemKeyProvider<Long>(SCOPE_MAPPED) {
        override fun getKey(position: Int): Long = getIdentifier(values[position])

        override fun getPosition(key: Long): Int = values.indexOfFirst { getIdentifier(it) == key }
    }
}