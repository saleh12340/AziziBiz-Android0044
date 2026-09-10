package com.mruraza.ims.Data.Local.Mappers

import com.mruraza.ims.Data.Local.Enitites.CustomerEntity
import com.mruraza.ims.Domain.Model.Customer

object CustomerMapper {
    fun Customer.toEntity() = CustomerEntity(id,name,phone,address,due,dueDate)
    fun CustomerEntity.toDomain() = Customer(id,name,phone,address,due,dueDate)
}