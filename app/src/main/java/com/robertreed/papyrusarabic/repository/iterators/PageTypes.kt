package com.robertreed.papyrusarabic.repository.iterators

import androidx.lifecycle.LiveData
import com.robertreed.papyrusarabic.model.PageType
import java.util.*

class PageTypes(private val input: LiveData<List<PageType>>) {

    private var loaded = false

    private var list: List<PageType>? = emptyList()

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

    fun get(i: Int) : PageType {
        if(loaded)
            return list!![i]
        else
            throw IllegalAccessError()
    }

    fun get(uuid: UUID) : PageType {
        if(loaded)
            for(i in list!!)
                if(i.id == uuid)
                    return i
        throw IllegalAccessError()
    }

    fun getLiveData() = input

    fun isLoaded() = loaded

    fun size(): Int {
        if(loaded)
            return list!!.size
        else
            throw IllegalAccessError()
    }

    private fun setLoaded() {
        loaded = input.value != null && input.value!!.isNotEmpty()
    }
}