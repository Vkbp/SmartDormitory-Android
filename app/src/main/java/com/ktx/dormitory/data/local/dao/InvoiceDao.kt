package com.ktx.dormitory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ktx.dormitory.data.local.entity.InvoiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoices ORDER BY dueDate DESC")
    fun getAllInvoices(): Flow<List<InvoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoices(invoices: List<InvoiceEntity>)

    @Query("DELETE FROM invoices")
    suspend fun clearAll()
}
