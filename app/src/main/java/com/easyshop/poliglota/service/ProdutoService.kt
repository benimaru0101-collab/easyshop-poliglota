package com.easyshop.poliglota.service

import com.easyshop.poliglota.data.nosql.Produto
import com.easyshop.poliglota.data.nosql.ProdutoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Regras de negócio do catálogo (dados flexíveis).
 * Valida nome e preço; os atributos são livres e vão como documento.
 */
class ProdutoService(private val repository: ProdutoRepository) {

    suspend fun listar(): List<Produto> = withContext(Dispatchers.IO) {
        repository.listarTodos()
    }

    suspend fun cadastrar(
        nomeDigitado: String,
        precoDigitado: String,
        categoriaDigitada: String,
        atributos: Map<String, String>
    ): Resultado {
        val nome = nomeDigitado.trim()
        if (nome.isEmpty()) return Resultado.Erro("Informe o nome do produto.")

        val preco = precoDigitado.trim().replace(",", ".").toDoubleOrNull()
        if (preco == null || preco < 0) return Resultado.Erro("Preço inválido.")

        val produto = Produto(
            nome = nome,
            preco = preco,
            categoria = categoriaDigitada.trim().ifEmpty { null },
            atributos = if (atributos.isEmpty()) null else HashMap<String, Any>(atributos),
            criadoEm = System.currentTimeMillis()
        )
        withContext(Dispatchers.IO) { repository.inserir(produto) }
        return Resultado.Sucesso("Produto salvo no banco NoSQL (documento).")
    }

    /** Produtos de exemplo com atributos diferentes, para demonstrar o schema livre. */
    suspend fun carregarExemplos(): Resultado = withContext(Dispatchers.IO) {
        val agora = System.currentTimeMillis()
        listOf(
            Produto(nome = "Camiseta Básica", preco = 49.9, categoria = "Vestuário",
                atributos = hashMapOf<String, Any>("Tamanho" to "M", "Cor" to "Azul", "Tecido" to "Algodão"), criadoEm = agora),
            Produto(nome = "Notebook Pro 14", preco = 4599.0, categoria = "Informática",
                atributos = hashMapOf<String, Any>("Processador" to "Core i7", "Memória RAM" to "16 GB", "Armazenamento" to "512 GB SSD"), criadoEm = agora),
            Produto(nome = "Liquidificador Turbo", preco = 189.9, categoria = "Eletroportáteis",
                atributos = hashMapOf<String, Any>("Voltagem" to "220 V", "Potência" to "900 W", "Garantia" to "12 meses"), criadoEm = agora),
            Produto(nome = "Caneca de Cerâmica", preco = 29.9, categoria = null, atributos = null, criadoEm = agora)
        ).forEach { repository.inserir(it) }
        Resultado.Sucesso("4 produtos de exemplo salvos, cada um com atributos diferentes.")
    }

    suspend fun excluir(id: Long) = withContext(Dispatchers.IO) { repository.excluir(id) }
}
