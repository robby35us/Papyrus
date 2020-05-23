package com.robertreed.papyrusarabic.repository.iterators

import com.robertreed.papyrusarabic.model.Lesson

class LessonIterator(private val lessonList: List<Lesson>,
                     private var index: Int = 0): ListIterator<Lesson> {
    override fun hasNext() = index < lessonList.size - 1

    override fun hasPrevious() = index > 0

    override fun next(): Lesson {
        index += 1
        return lessonList[index - 1]
    }

    fun peek() = lessonList[index]

    override fun previous() : Lesson {
        index -= 1
        return lessonList[index]
    }

    override fun nextIndex() = index

    override fun previousIndex() = index - 1

    fun size() = lessonList.size

    fun get(i: Int) = lessonList[i]

    fun getIndex() = index

    fun setIndex(i: Int) {
        index = i
    }
}