package com.easyshop.poliglota.data.sql

import kotlinx.coroutines.flow.Flow

/** Camada de dados (SQL): única classe que conversa com a tabela clientes. */
class ClienteRepository(private val dao: ClienteDao) {

    fun observarTodos(): Flow<List<Cliente>> = dao.observarTodos()

    suspend fun inserir(cliente: Cliente): Long = dao.inserir(cliente)

    suspend fun emailJaExiste(email: String): Boolean = dao.contarPorEmail(email) > 0

    suspend fun excluir(cliente: Cliente) = dao.excluir(cliente)
}
