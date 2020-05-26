package com.robertreed.papyrusarabic.database.initialize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.model.*
import com.robertreed.papyrusarabic.repository.PapyrusRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.*
import java.io.InputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class PapyrusDatabaseLoader : AppCompatActivity() {

    private val repository = PapyrusRepository.get()
    private lateinit var moduleIds: ArrayList<UUID>
    private lateinit var lessonIds: SparseArray<UUID>
    private lateinit var pageTypeIds: ArrayList<UUID>
    private lateinit var imageIds: ArrayList<UUID>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_papyrus_database_loader)
        GlobalScope.launch {
            PapyrusRepository.get().clearDatabase()
        }
        try {

            val stream: InputStream = this.assets.open("Book.xlsx")
            val workbook: Workbook = WorkbookFactory.create(stream)
            val moduleSheet= workbook.getSheetAt(0)
            moduleIds = insertModules(moduleSheet)

            val lessonSheet = workbook.getSheetAt(1)
            lessonIds = insertLessons(lessonSheet)

            val pageTypeSheet = workbook.getSheetAt(3)
            pageTypeIds = insertPageTypes(pageTypeSheet)

            val imageSheet = workbook.getSheetAt(4)
            imageIds = insertImages(imageSheet)

            val pageSheet = workbook.getSheetAt(2)
            insertPages(pageSheet)
        }catch (e:Exception) {
            throw e
        }
    }

    private fun insertModules(moduleSheet: Sheet): ArrayList<UUID> {
        val moduleUUIDList = ArrayList<UUID>(moduleSheet.physicalNumberOfRows)
        for(i in 1 until moduleSheet.physicalNumberOfRows) {
            val rowData: Row = moduleSheet.getRow(i)
            val module = Module(
                number = rowData.getCell(0).numericCellValue.toInt(),
                title = rowData.getCell(1).stringCellValue,
                shortTitle = rowData.getCell(2).stringCellValue,
                description = rowData.getCell(3).stringCellValue
            )
            moduleUUIDList.add(module.id)
            GlobalScope.launch {
                repository.insertModule(module)
                //Log.i("INSERT_MODULES", repository.getModule(module.id)!!.toString())
            }
        }
        return moduleUUIDList
    }

    private fun insertLessons(sheet: Sheet): SparseArray<UUID> {
        val lessonUUIDArray = SparseArray<UUID>()
        for(i in 1 until sheet.physicalNumberOfRows) {
            val row: Row = sheet.getRow(i)

            val moduleNumber = row.getCell(0).numericCellValue.toInt()
            val lessonNumber = row.getCell(1).numericCellValue.toInt()

            val lesson = Lesson(
                moduleId = moduleIds[moduleNumber],
                number = lessonNumber,
                name = row.getCell(2).stringCellValue
            )
            lessonUUIDArray.append(moduleNumber * 10 + lessonNumber, lesson.id)
            GlobalScope.launch {
                repository.insertLesson(lesson)
                //Log.i("INSERT_LESSONS", repository.getLesson(lesson.id)!!.toString())
            }
        }
        return lessonUUIDArray
    }

    private fun insertPageTypes(pageTypeSheet: Sheet): ArrayList<UUID> {
        val pageTypeUUIDArray = ArrayList<UUID>(pageTypeSheet.physicalNumberOfRows)
        for (i in 1 until pageTypeSheet.physicalNumberOfRows) {
            val rowData: Row = pageTypeSheet.getRow(i)
            val pageType = PageType(
                name = rowData.getCell(1).stringCellValue
            )
            pageTypeUUIDArray.add(pageType.id)
            GlobalScope.launch {
                repository.insertPageType(pageType)
                //Log.i("INSERT_PAGE_TYPES", repository.getPageType(pageType.id)!!.toString())
            }
        }
        return pageTypeUUIDArray
    }

    private fun insertImages(imageSheet: Sheet): ArrayList<UUID> {
        val imageUUIDList = ArrayList<UUID>(imageSheet.physicalNumberOfRows)
        for (i in 1 until imageSheet.physicalNumberOfRows) {
            val rowData: Row = imageSheet.getRow(i)
            val image = Image(
                resourceName = rowData.getCell(1).stringCellValue,
                contentDescription = rowData.getCell(1).stringCellValue
            )
            imageUUIDList.add(image.id)
            GlobalScope.launch {
                repository.insertImage(image)
                //Log.i("INSERT_IMAGES", repository.getImage(image.id)!!.toString())
            }
        }
        return imageUUIDList
    }

    private fun insertPages(sheet: Sheet) {
        for(i in 1 until sheet.physicalNumberOfRows) {
            val row: Row = sheet.getRow(i)

            val moduleNumber = row.getCell(0).numericCellValue.toInt()
            val lessonNumber = row.getCell(1).numericCellValue.toInt()
            val page = Page(
                lessonId = lessonIds.get(moduleNumber * 10 + lessonNumber),
                number = row.getCell(2).numericCellValue.toInt(),
                pageType = pageTypeIds[row.getCell(3).numericCellValue.toInt()],
                header = if(row.getCell(4)?.cellType == Cell.CELL_TYPE_STRING)
                    row.getCell(4).stringCellValue
                else
                    null,
                sub_header = if(row.getCell(5)?.cellType == Cell.CELL_TYPE_STRING)
                    row.getCell(5).stringCellValue
                else
                    null,
                content1 = if(row.getCell(6)?.cellType == Cell.CELL_TYPE_STRING)
                    row.getCell(6).stringCellValue
                else
                    null,
                content2 = if(row.getCell(7)?.cellType == Cell.CELL_TYPE_STRING)
                    row.getCell(7).stringCellValue
                else
                    null,
                content3 = if(row.getCell(8)?.cellType == Cell.CELL_TYPE_STRING)
                    row.getCell(8).stringCellValue
                else
                    null,
                image = imageIds[row.getCell(9)?.numericCellValue?.toInt()?.minus(1)?: 0]

            )
            GlobalScope.launch {
                repository.insertPage(page)
                //Log.i("INSERT_PAGES", repository.getPage(page.id)!!.toString())
            }
        }
    }
}
