package com.mruraza.ims.Data.RepoImpl

import com.mruraza.ims.Data.Local.DAO.CustomerDAO
import com.mruraza.ims.Data.Local.Mappers.CustomerMapper.toDomain
import com.mruraza.ims.Data.Local.Mappers.CustomerMapper.toEntity
import com.mruraza.ims.Domain.Model.Customer
import com.mruraza.ims.Domain.Repo.CustomerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CustomerRepoImpl @Inject constructor(private val customerDAO: CustomerDAO): CustomerRepo {
    override suspend fun insert(customer: Customer) {
        customerDAO.insert(customer.toEntity())
    }

    override suspend fun update(customer: Customer) {
        customerDAO.update(customer.toEntity())
    }

    override suspend fun delete(customer: Customer) {
        customerDAO.delete(customer.toEntity())
    }

    override suspend fun getAll(): Flow<List<Customer>> {
        return customerDAO.getALLCustomers().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

}
