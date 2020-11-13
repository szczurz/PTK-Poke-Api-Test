package com.pkurkowski.pokeapi.data.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "databaseState")
data class DatabaseStateEntity(
    val pokemonCount: Int? = null,
    val pokemonLoadedCount: Int = 0,
    @PrimaryKey @ColumnInfo(name = "id")  val id:Int = 0,
)