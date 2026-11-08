package com.easyshop.poliglota.data.sql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** DAO: comandos SQL da tabela clientes. */
@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes ORDER BY nome_completo COLLATE NOCASE")
    fun observarTodos(): Flow<List<Cliente>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(cliente: Cliente): Long

    @Query("SELECT COUNT(*) FROM clientes WHERE email = :email")
    suspend fun contarPorEmail(email: String): Int

    @Delete
    suspend fun excluir(cliente: Cliente)
}
