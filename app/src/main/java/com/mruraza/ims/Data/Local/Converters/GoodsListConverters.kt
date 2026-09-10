package com.mruraza.ims.Data.Local.Converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mruraza.ims.Domain.Model.Goods

class GoodsListConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromGoodsList(goodsList:List<Pair<Goods, Int>>): String{
        return gson.toJson(goodsList)
    }

    @TypeConverter
    fun toGoodsList(goodsListString: String):List<Pair<Goods, Int>>{
        val type = object : TypeToken<List<Pair<Goods, Int>>>(){}.type
        return gson.fromJson(goodsListString,type)
    }
}

