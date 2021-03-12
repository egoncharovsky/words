package ru.egoncharovsky.words.ui

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import mu.KotlinLogging
import ru.egoncharovsky.words.R

abstract class SelectableRecyclerViewAdapter<T, K>(
    private val keyStorageStrategy: StorageStrategy<K>,
    val selectionByShortPress: Boolean = true,
    val multiSelection: Boolean = true,
    val persistenceSelection: Boolean = true,
) : RecyclerViewAdapter<T>() {

    private val logger = KotlinLogging.logger {}

    var tracker: SelectionTracker<K>? = null
        private set

    abstract fun bind(itemView: View, item: T, isActivated: Boolean)

    abstract fun getIdentifier(item: T): K

    final override fun bind(itemView: View, item: T) = bind(itemView, item, false)

    fun observe(recyclerView: RecyclerView, observer: Observer<List<K>>) {
        if (tracker == null) {
            val keyProvider = ItemKeyProviderImpl()
            tracker = SelectionTracker.Builder(
                "selection",
                recyclerView,
                keyProvider,
                ItemDetailsLookupImpl(recyclerView),
                keyStorageStrategy
            ).withSelectionPredicate(
                if (multiSelection) {
                    SelectionPredicates.createSelectAnything()
                } else {
                    SelectionPredicates.createSelectSingleAnything()
                }
            ).build()
        }

        tracker!!.addObserver(object : SelectionTracker.SelectionObserver<K>() {
            override fun onItemStateChanged(key: K, selected: Boolean) {
                val selection = tracker!!.selection.toList()
                logger.trace("Changed item $key to $selected selection is $selection")
                if (values.indexOfFirst { getIdentifier(it) == key } != RecyclerView.NO_POSITION) {
                    observer.onChanged(selection)
                }
            }
        })
    }

    override fun update(values: List<T>) {
        if (persistenceSelection) {
            val selected = tracker?.selection?.toSet()
            logger.trace("Save $selected")

            super.update(values)
            tracker?.run {
                if (selected != null && selected.isNotEmpty()) {
                    logger.trace("Restore $selected")
                    setItemsSelected(selected, true)
                }
            }
        } else {
            super.update(values)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        @Suppress("UNCHECKED_CAST")
        val selectableViewHolder = holder as SelectableRecyclerViewAdapter<T, K>.SelectableViewHolder

        val value = values[position]
        val selected = tracker?.run { isSelected(getIdentifier(value)) } ?: false
        selectableViewHolder.bind(value, selected)
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

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<K> =
            object : ItemDetailsLookup.ItemDetails<K>() {
                override fun getPosition(): Int = absoluteAdapterPosition
                override fun getSelectionKey(): K = getIdentifier(values[position])
                override fun inSelectionHotspot(e: MotionEvent): Boolean = selectionByShortPress
            }
    }

    inner class ItemDetailsLookupImpl(private val recyclerView: RecyclerView) : ItemDetailsLookup<K>() {

        @Suppress("UNCHECKED_CAST")
        override fun getItemDetails(event: MotionEvent): ItemDetails<K>? {
            return recyclerView.findChildViewUnder(event.x, event.y)?.let {
                (recyclerView.getChildViewHolder(it) as SelectableRecyclerViewAdapter<*, *>.SelectableViewHolder)
                    .getItemDetails()
            } as ItemDetails<K>?
        }
    }

    inner class ItemKeyProviderImpl : ItemKeyProvider<K>(SCOPE_MAPPED) {
        override fun getKey(position: Int): K = getIdentifier(values[position])

        override fun getPosition(key: K): Int = values.indexOfFirst { getIdentifier(it) == key }
    }
}