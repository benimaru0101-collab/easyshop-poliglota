package com.easyshop.poliglota.data.sql

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabela relacional "clientes" (SQLite via Room).
 * Schema fixo, chave primária autoincremento e e-mail único:
 * o próprio banco recusa dois clientes com o mesmo e-mail.
 */
@Entity(
    tableName = "clientes",
    indices = [Index(value = ["email"], unique = true)]
)
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "nome_completo")
    val nomeCompleto: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "criado_em")
    val criadoEm: Long = System.currentTimeMillis()
)
