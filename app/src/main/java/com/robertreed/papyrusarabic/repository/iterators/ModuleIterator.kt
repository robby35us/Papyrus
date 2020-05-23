package com.robertreed.papyrusarabic.repository.iterators

import com.robertreed.papyrusarabic.model.Module

class ModuleIterator(private val moduleList: List<Module>,
                     private var index: Int = 0) : ListIterator<Module> {

    override fun hasNext() = index < moduleList.size - 1

    override fun hasPrevious() = index > 0

    override fun next(): Module {
        index += 1
        return moduleList[index - 1]
    }

    fun peek() = moduleList[index]

    override fun previous() : Module {
        index -= 1
        return moduleList[index]
    }

    override fun nextIndex() = index

    override fun previousIndex() = index - 1

    fun size() = moduleList.size

    fun get(i: Int) = moduleList[i]

    fun getIndex() = index

    fun setIndex(i: Int) {
        index = i
    }
}