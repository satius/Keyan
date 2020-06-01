package net.satius.keyan.infrastructure

import android.content.Context
import androidx.room.Room
import net.satius.keyan.infrastructure.common.KeyanDataBase

fun build(applicationContext: Context, dbName: String) = Room.databaseBuilder(
    applicationContext,
    KeyanDataBase::class.java,
    dbName
).build()
