package com.robertreed.papyrusarabic.repository.iterators

import androidx.lifecycle.LiveData
import com.robertreed.papyrusarabic.model.Page

class PageIterator(private val input: LiveData<List<Page>>) : ListIterator<Page> {

    private var loaded = false

    private var list: List<Page>? = emptyList()

    init {
        setLoaded()
        if (loaded)
            list = input.value
        else
            input.observeForever {
                setLoaded()
                if(loaded)
                    list = input.value
            }
    }

    private var index = 0

    fun get(i: Int) : Page {
        if(loaded)
            return list!![i]
        else
            throw IllegalAccessError()
    }

    fun getIndex() = index

    fun getLiveData() = input

    override fun hasNext() :Boolean {
        if(loaded)
            return index < list!!.size - 1
        else
            throw IllegalAccessError()
    }

    override fun hasPrevious() = index > 0

    fun isLoaded() = loaded

    override fun next(): Page {
        if(loaded) {
            index += 1
            return list!![index - 1]
        } else
            throw IllegalAccessError()
    }

    override fun nextIndex() = index

    fun peek() : Page {
        if (loaded)
            return list!![index]
        else
            throw IllegalAccessError()
    }

    override fun previous() : Page {
        if(loaded) {
            index -= 1
            return list!![index]
        } else
            throw IllegalAccessError()
    }

    override fun previousIndex() = index - 1

    fun size(): Int {
        if(loaded)
            return list!!.size
        else
            throw IllegalAccessError()
    }

    fun setIndex(i: Int) {
        index = i
    }

    private fun setLoaded() {
        loaded = input.value != null && input.value!!.isNotEmpty()
    }
}