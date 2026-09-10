package com.mruraza.ims.Domain.Repo

import com.mruraza.ims.Domain.Model.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepo {
    suspend fun insert(supplier: Supplier)
    suspend fun update(supplier: Supplier)
    suspend fun delete(supplier: Supplier)
    suspend fun getAll(): Flow<List<Supplier>>
}