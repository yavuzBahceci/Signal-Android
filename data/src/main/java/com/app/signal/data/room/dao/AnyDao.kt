package com.app.signal.data.room.dao

import androidx.room.*

@Dao
abstract class AnyDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: T): Long

    @Update
    abstract suspend fun update(obj: T)

    @Transaction
    open suspend fun insertOrUpdate(obj: T) {
        val id = insert(obj)
        if (id == -1L) update(obj)
    }

}