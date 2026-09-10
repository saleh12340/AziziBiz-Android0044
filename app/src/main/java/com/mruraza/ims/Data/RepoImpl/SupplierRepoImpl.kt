package com.mruraza.ims.Data.RepoImpl

import com.mruraza.ims.Data.Local.DAO.SupplierDAO
import com.mruraza.ims.Data.Local.Mappers.SupplierMapper.toDomain
import com.mruraza.ims.Data.Local.Mappers.SupplierMapper.toEntity
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Domain.Repo.SupplierRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SupplierRepoImpl @Inject constructor(private val dao: SupplierDAO) : SupplierRepo {
    override suspend fun insert(supplier: Supplier) {
        dao.insert(supplier.toEntity())
    }

    override suspend fun update(supplier: Supplier) {
        dao.update(supplier.toEntity())
    }

    override suspend fun delete(supplier: Supplier) {
        dao.delete(supplier.toEntity())
    }

    override suspend fun getAll(): Flow<List<Supplier>> {
        return dao.getAllSuppliers().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }

        }
    }
}