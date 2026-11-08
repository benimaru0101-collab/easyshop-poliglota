package com.easyshop.poliglota.data.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Banco SQL local: arquivo clientes.db (SQLite) na pasta privada do app. */
@Database(entities = [Cliente::class], version = 1, exportSchema = false)
abstract class BancoRelacional : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao

    companion object {
        fun criar(context: Context): BancoRelacional =
            Room.databaseBuilder(context, BancoRelacional::class.java, "clientes.db").build()
    }
}
