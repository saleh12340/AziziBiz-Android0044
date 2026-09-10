package com.mruraza.ims.Domain.UseCase

import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Domain.Repo.SupplierRepo
import com.mruraza.ims.Utils.Objects.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddSuppliers @Inject constructor(private val repo: SupplierRepo) {
    suspend operator fun invoke(supplier: Supplier) = repo.insert(supplier)
}

class GetSuppliers @Inject constructor(private val repo: SupplierRepo) {
    operator fun invoke(): Flow<Resources<List<Supplier>>> = flow {
        emit(Resources.Loading)
        repo.getAll()
            .catch { e -> emit(Resources.Error(e)) }
            .collect { supplier -> emit(Resources.Success(supplier)) }
    }.flowOn(Dispatchers.IO)
}

class UpdateSuppliers @Inject constructor(private val repo: SupplierRepo) {
    suspend operator fun invoke(supplier: Supplier) = repo.update(supplier)
}

class DeleteSuppliers @Inject constructor(private val repo: SupplierRepo) {
    suspend operator fun invoke(supplier: Supplier) = repo.delete(supplier)
}


data class SupplierUseCases @Inject constructor(
    val addSuppliers: AddSuppliers,
    val getSuppliers: GetSuppliers,
    val updateSuppliers: UpdateSuppliers,
    val deleteSuppliers: DeleteSuppliers
)