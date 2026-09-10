package com.mruraza.ims.Domain.UseCase

import com.mruraza.ims.Domain.Model.Customer
import com.mruraza.ims.Domain.Repo.CustomerRepo
import com.mruraza.ims.Utils.Objects.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddCustomer @Inject constructor(private val repository: CustomerRepo) {
    suspend operator fun invoke(customer: Customer) = repository.insert(customer)
}

class UpdateCustomers @Inject constructor(private val repository: CustomerRepo) {
    suspend operator fun invoke(customer: Customer) = repository.update(customer)
}

class DeleteCustomers @Inject constructor(private val repository: CustomerRepo) {
    suspend operator fun invoke(customer: Customer) = repository.delete(customer)
}

class GetCustomers @Inject constructor(private val repository: CustomerRepo) {
    operator fun invoke(): Flow<Resources<List<Customer>>> = flow {
        emit(Resources.Loading)
        repository.getAll()
            .catch { e -> emit(Resources.Error(e)) }
            .collect { customers ->
                emit(Resources.Success(customers))
            }
    }.flowOn(
        Dispatchers.IO
    )

}

data class CustomerUseCases @Inject constructor(
    val addCustomer: AddCustomer,
    val updateCustomers: UpdateCustomers,
    val deleteCustomers: DeleteCustomers,
    val getCustomers: GetCustomers
)