package com.easyshop.poliglota.data.nosql

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Documento do catálogo (ObjectBox, banco NoSQL local).
 *
 * "atributos" é uma propriedade flexível (flex property): cada produto
 * guarda o seu próprio conjunto de chave/valor, sem coluna fixa.
 * Uma camisa pode ter tamanho e cor; um notebook, processador e RAM.
 * Nenhuma alteração de schema é necessária para uma categoria nova.
 */
@Entity
data class Produto(
    @Id var id: Long = 0,
    var nome: String = "",
    var preco: Double = 0.0,
    var categoria: String? = null,
    var atributos: MutableMap<String, Any>? = null,
    var criadoEm: Long = 0
)
