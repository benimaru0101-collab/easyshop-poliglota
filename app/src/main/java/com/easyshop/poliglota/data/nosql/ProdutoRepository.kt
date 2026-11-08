package com.easyshop.poliglota.data.nosql

import io.objectbox.Box

/** Camada de dados (NoSQL): única classe que conversa com a coleção de produtos. */
class ProdutoRepository(private val box: Box<Produto>) {

    fun inserir(produto: Produto): Long = box.put(produto)

    fun listarTodos(): List<Produto> = box.all.sortedBy { it.nome.lowercase() }

    fun contar(): Long = box.count()

    fun excluir(id: Long) {
        box.remove(id)
    }
}
