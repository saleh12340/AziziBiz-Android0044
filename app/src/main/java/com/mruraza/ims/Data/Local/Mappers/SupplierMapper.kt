package com.mruraza.ims.Data.Local.Mappers

import com.mruraza.ims.Data.Local.Enitites.SupplierEntity
import com.mruraza.ims.Domain.Model.Supplier

object SupplierMapper {
    fun SupplierEntity.toDomain() = Supplier(id, name, address, contactInfo, totalPaid, due,dueDate)
    fun Supplier.toEntity() = SupplierEntity(id, name, address, contactInfo, totalPaid, due,dueDate)
}