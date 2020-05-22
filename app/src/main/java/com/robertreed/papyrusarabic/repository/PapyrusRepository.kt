package com.robertreed.papyrusarabic.repository

import android.content.Context

class PapyrusRepository private constructor(context: Context){
    companion object {
        private  var INSTANCE: PapyrusRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE =
                    PapyrusRepository(
                        context
                    )
            }
        }

        fun get(): PapyrusRepository {
            return INSTANCE
                ?: throw IllegalStateException("Repository Must be initialized")
        }
    }
}