package com.mruraza.ims.Domain.Repo

import com.mruraza.ims.Domain.Model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepo {
    suspend fun insert(customer: Customer)
    suspend fun update(customer: Customer)
    suspend fun delete(customer: Customer)
    suspend fun getAll(): Flow<List<Customer>>

}