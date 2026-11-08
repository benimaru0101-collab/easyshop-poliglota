package com.easyshop.poliglota.service

import android.database.sqlite.SQLiteConstraintException
import android.util.Patterns
import com.easyshop.poliglota.data.sql.Cliente
import com.easyshop.poliglota.data.sql.ClienteRepository
import kotlinx.coroutines.flow.Flow

/**
 * Regras de negócio do cadastro de clientes (dados estruturados).
 * Valida tudo antes de chegar no banco SQL.
 */
class ClienteService(private val repository: ClienteRepository) {

    fun observarClientes(): Flow<List<Cliente>> = repository.observarTodos()

    suspend fun cadastrar(nomeDigitado: String, emailDigitado: String): Resultado {
        val nome = nomeDigitado.trim().replace(Regex("\\s+"), " ")
        val email = emailDigitado.trim().lowercase()

        if (nome.split(" ").size < 2) {
            return Resultado.Erro("Informe o nome completo (nome e sobrenome).")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resultado.Erro("E-mail inválido.")
        }
        if (repository.emailJaExiste(email)) {
            return Resultado.Erro("Já existe um cliente com este e-mail.")
        }
        return try {
            repository.inserir(Cliente(nomeCompleto = nome, email = email))
            Resultado.Sucesso("Cliente salvo no banco SQL (tabela clientes).")
        } catch (e: SQLiteConstraintException) {
            Resultado.Erro("Já existe um cliente com este e-mail.")
        }
    }

    suspend fun excluir(cliente: Cliente) = repository.excluir(cliente)
}
