package com.easyshop.poliglota.di

import android.content.Context
import com.easyshop.poliglota.data.nosql.MyObjectBox
import com.easyshop.poliglota.data.nosql.Produto
import com.easyshop.poliglota.data.nosql.ProdutoRepository
import com.easyshop.poliglota.data.sql.BancoRelacional
import com.easyshop.poliglota.data.sql.ClienteRepository
import com.easyshop.poliglota.service.ClienteService
import com.easyshop.poliglota.service.ProdutoService
import io.objectbox.BoxStore

/**
 * Monta as camadas uma única vez: bancos -> repositórios -> serviços.
 * Os dois bancos convivem no mesmo app, cada um com sua responsabilidade.
 */
class Servicos(context: Context) {

    // Banco SQL (SQLite/Room): arquivo clientes.db
    private val bancoRelacional = BancoRelacional.criar(context)

    // Banco NoSQL (ObjectBox): pasta catalogo-produtos
    private val bancoDocumentos: BoxStore = MyObjectBox.builder()
        .androidContext(context)
        .name("catalogo-produtos")
        .build()

    val clienteService = ClienteService(ClienteRepository(bancoRelacional.clienteDao()))

    val produtoService = ProdutoService(ProdutoRepository(bancoDocumentos.boxFor(Produto::class.java)))
}
