package ru.egoncharovsky.words.ui

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import mu.KotlinLogging

abstract class SelectableRecyclerViewAdapter<T, K, VB : ViewBinding>(
    private val keyStorageStrategy: StorageStrategy<K>,
    val selectionByShortPress: Boolean = true,
    val multiSelection: Boolean = true,
    val persistenceSelection: Boolean = true,
) : RecyclerViewAdapter<T, VB>() {

    private val logger = KotlinLogging.logger {}

    private var tracker: SelectionTracker<K>? = null

    abstract fun bind(binding: VB, item: T, isActivated: Boolean)

    abstract fun getIdentifier(item: T): K

    final override fun bind(binding: VB, item: T) = bind(binding, item, false)

    fun observe(recyclerView: RecyclerView, observer: Observer<List<K>>) : SelectionTracker<K> {
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
                logger.trace("Changed item $key to $selected, selection is $selection")
                if (values.indexOfFirst { getIdentifier(it) == key } != RecyclerView.NO_POSITION) {
                    observer.onChanged(selection)
                }
            }
        })
        return tracker!!
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
        val selectableViewHolder = holder as SelectableRecyclerViewAdapter<T, K, VB>.SelectableViewHolder

        val value = values[position]
        val selected = tracker?.run { isSelected(getIdentifier(value)) } ?: false
        selectableViewHolder.bind(value, selected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder {
        return SelectableViewHolder(
            bindingInflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = values.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class SelectableViewHolder(binding: VB) : ViewHolder(binding) {

        val itemDetails = object : ItemDetailsLookup.ItemDetails<K>() {
            override fun getPosition(): Int = absoluteAdapterPosition
            override fun getSelectionKey(): K = getIdentifier(values[position])
            override fun inSelectionHotspot(e: MotionEvent): Boolean = selectionByShortPress
        }

        fun bind(value: T, isActivated: Boolean = false) {
            itemView.isActivated = isActivated
            bind(binding, value, isActivated)
        }
    }

    inner class ItemDetailsLookupImpl(private val recyclerView: RecyclerView) : ItemDetailsLookup<K>() {

        @Suppress("UNCHECKED_CAST")
        override fun getItemDetails(event: MotionEvent): ItemDetails<K>? {
            return recyclerView.findNearestChildViewUnder(event.x, event.y)?.let {
                (recyclerView.getChildViewHolder(it) as SelectableRecyclerViewAdapter<*, *, *>.SelectableViewHolder).itemDetails
            } as ItemDetails<K>?
        }
    }

    /**
     * remove it after fixing bug: selection tracker clears all selection if user clicked between child views:
     * if RecyclerView.findChildViewUnder returns null
     */
    private fun RecyclerView.findNearestChildViewUnder(x: Float, y: Float): View? {
        var view = findChildViewUnder(x, y)
        if (view != null) return view

        logger.debug("No view found at $x $y")

        val shift = 10

        fun movePlus(c: Float): Float = c + shift
        fun moveMinus(c: Float): Float = c - shift

        fun findMoved(xM: Float, yM: Float): View? {
            logger.trace("Trying $xM $yM")
            val v = findChildViewUnder(xM, yM)

            if (v != null) {
                logger.warn("Not found child view at $x $y but found at $xM $yM")
            }
            return v
        }

        view = findMoved(x, movePlus(y))

        if (view == null) {
            view = findMoved(movePlus(x), y)
        }
        if (view == null) {
            view = findMoved(movePlus(x), movePlus(y))
        }

        if (view == null) {
            view = findMoved(x, moveMinus(y))
        }
        if (view == null) {
            view = findMoved(moveMinus(x), y)
        }
        if (view == null) {
            view = findMoved(moveMinus(x), moveMinus(y))
        }

        return view
    }

    inner class ItemKeyProviderImpl : ItemKeyProvider<K>(SCOPE_MAPPED) {
        override fun getKey(position: Int): K = getIdentifier(values[position])

        override fun getPosition(key: K): Int = values.indexOfFirst { getIdentifier(it) == key }
    }
}