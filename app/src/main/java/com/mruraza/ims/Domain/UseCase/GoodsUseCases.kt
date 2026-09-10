package com.mruraza.ims.Domain.UseCase

import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Repo.GoodsRepo
import com.mruraza.ims.Utils.Objects.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddGoods @Inject constructor(private val repository: GoodsRepo){
    suspend operator fun invoke(goods: Goods) = repository.insert(goods)
}

class GetGoods @Inject constructor(private val repository: GoodsRepo) {
    operator fun invoke(): Flow<Resources<List<Goods>>> = flow {
        emit(Resources.Loading)
        repository.getAll()
            .catch { e -> emit(Resources.Error(e)) }
            .collect { goods -> emit(Resources.Success(goods)) }
    }.flowOn(Dispatchers.IO)
}

class UpdateGoods @Inject constructor(private val repository: GoodsRepo) {
    suspend operator fun invoke(goods: Goods) = repository.update(goods)
}

class DeleteGoods @Inject constructor(private val repository: GoodsRepo) {
    suspend operator fun invoke(goods: Goods) = repository.delete(goods)
}


data class GoodsUseCases @Inject constructor(
    val addGoods: AddGoods,
    val getGoods: GetGoods,
    val updateGoods: UpdateGoods,
    val deleteGoods: DeleteGoods
)