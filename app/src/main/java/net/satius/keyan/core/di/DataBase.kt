package net.satius.keyan.core.di

import android.content.Context
import androidx.room.Room
import net.satius.keyan.infrastructure.common.KeyanDataBase
import org.koin.dsl.module

fun createDataBaseModules(applicationContext: Context) = listOf(
    module {
        single {
            Room
                .databaseBuilder(
                    applicationContext,
                    KeyanDataBase::class.java,
                    "app.db"
                )
                .build()
        }
    }
)
