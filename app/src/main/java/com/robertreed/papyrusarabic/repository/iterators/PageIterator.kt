package com.robertreed.papyrusarabic.repository.iterators

import com.robertreed.papyrusarabic.model.Page

class PageIterator(private val pageList: List<Page>,
                   private var index: Int = 0): ListIterator<Page> {

    override fun hasNext() = index < pageList.size - 1

    override fun hasPrevious() = index > 0

    override fun next(): Page {
        index += 1
        return pageList[index - 1]
    }

    fun peek() = pageList[index]

    override fun previous() : Page {
        index -= 1
        return pageList[index]
    }

    override fun nextIndex() = index

    override fun previousIndex() = index - 1

    fun size() = pageList.size

    fun get(i: Int) = pageList[i]

    fun getIndex() = index

    fun setIndex(i: Int) {
        index = i
    }
}