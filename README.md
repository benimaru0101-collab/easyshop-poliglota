# EasyShop — Persistência Poliglota no Android

Protótipo de mini e-commerce que usa **dois bancos de dados locais ao mesmo tempo**, no mesmo aplicativo Android:

| Dado | Banco | Por quê |
|---|---|---|
| **Catálogo de Produtos** | **NoSQL — ObjectBox** (documentos/objetos, sem schema fixo) | Cada produto tem atributos diferentes (camisa: tamanho e cor; notebook: processador e RAM). Os atributos ficam numa propriedade flexível (chave/valor), sem criar colunas nulas nem tabelas EAV. |
| **Cadastro de Clientes** | **SQL — SQLite via Room** (tabela relacional) | Dados estruturados que exigem consistência: chave primária autoincremento e e-mail **único** garantido pelo próprio banco. |

## Integrantes

- Juliano Silva Tempesta
- [Preencher: nome completo]
- [Preencher: nome completo]

## Vídeo de demonstração

[Preencher: link do YouTube ou Google Drive]

## Funcionalidades

**Aba Produtos (NoSQL / ObjectBox)**
- Formulário com Nome, Preço, Categoria (opcional) e **atributos dinâmicos** (quantos quiser, no formato atributo/valor).
- Listagem do catálogo lida do ObjectBox, mostrando os atributos de cada produto.
- Botão **Carregar exemplos**: grava 4 produtos com atributos totalmente diferentes, para demonstrar o schema livre.
- Exclusão de produto.

**Aba Clientes (SQL / Room + SQLite)**
- Formulário com Nome completo e E-mail.
- Validação: nome e sobrenome, formato de e-mail e e-mail não repetido.
- Listagem lida da tabela `clientes`, atualizada automaticamente (Flow do Room).
- Exclusão de cliente.

## Arquitetura em camadas

```
app/src/main/java/com/easyshop/poliglota/
├── ui/                      CAMADA DE APRESENTAÇÃO (Jetpack Compose + ViewModels)
│   ├── MainActivity.kt        duas abas: Produtos (NoSQL) e Clientes (SQL)
│   ├── produtos/              ProdutosScreen + ProdutosViewModel
│   └── clientes/              ClientesScreen + ClientesViewModel
├── service/                 CAMADA DE NEGÓCIO
│   ├── ProdutoService.kt      valida nome/preço e monta o documento com atributos
│   └── ClienteService.kt      valida nome completo, e-mail e duplicidade
├── data/                    CAMADA DE ACESSO A DADOS
│   ├── nosql/                 ObjectBox: Produto (entidade com atributos flexíveis) + ProdutoRepository
│   └── sql/                   Room/SQLite: Cliente (tabela), ClienteDao, BancoRelacional + ClienteRepository
└── di/Servicos.kt           monta bancos -> repositórios -> serviços
```

Regras seguidas:
- As telas e os ViewModels **não acessam banco**; só chamam os serviços.
- Os serviços concentram as regras de negócio e chamam os repositórios.
- Só os repositórios conversam com o ObjectBox e com o SQLite.

Onde os dados ficam no aparelho (pasta privada do app):
- SQL: `databases/clientes.db`
- NoSQL: `files/objectbox/catalogo-produtos/`

## Tecnologias

- Kotlin 1.9 + Jetpack Compose (Material 3)
- Room 2.6.1 (SQLite) — banco relacional
- ObjectBox 4.0 — banco NoSQL local
- Android Gradle Plugin 8.5, Gradle 8.8, minSdk 24, targetSdk 34

## Como executar

**Pelo Android Studio (recomendado para o emulador)**
1. Abra o Android Studio e escolha **Open**, apontando para esta pasta.
2. Espere o Gradle sincronizar (a primeira vez baixa as dependências).
3. Inicie um emulador Android (Device Manager) e clique em **Run ▶**.

**Pela linha de comando (Windows)**
1. Dê dois cliques em `compilar-e-instalar.bat`.
2. O APK é gerado em `app/build/outputs/apk/debug/app-debug.apk` e, se houver um emulador aberto, é instalado nele automaticamente.

Requisitos: Android Studio (com SDK 34) ou JDK 17+ e Android SDK configurado em `ANDROID_HOME`.

## Roteiro rápido de teste

1. Aba **Produtos**: toque em **Carregar exemplos** e veja produtos com atributos diferentes na lista.
2. Cadastre um produto novo com um atributo inventado (ex.: "Sabor: Morango").
3. Aba **Clientes**: cadastre "Maria Souza" / maria@email.com.
4. Tente cadastrar outro cliente com o mesmo e-mail: o app recusa.
5. Feche e reabra o app: os dados dos dois bancos continuam lá.
