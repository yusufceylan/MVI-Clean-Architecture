package com.ysfcyln.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ysfcyln.local.model.PostLocalModel

@Database(entities = [PostLocalModel::class], version = 1, exportSchema = false) // We need migration if increase version
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao() : PostDAO
}