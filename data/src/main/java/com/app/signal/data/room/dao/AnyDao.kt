package com.app.signal.data.room.dao

import androidx.room.*

@Dao
abstract class AnyDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: T): Long

    @Update
    abstract suspend fun update(obj: T): Long

    @Transaction
    open suspend fun insertOrUpdate(obj: T): Long {
        val id = insert(obj)
        return if (id == -1L) update(obj) else id
    }

}