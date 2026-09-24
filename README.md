# 🌸 PerfumeHub

Sistema de gerenciamento de perfumes desenvolvido como projeto de portfólio backend, com foco em boas práticas de arquitetura, modelagem de domínio realista e evolução incremental por blocos de funcionalidade.

O sistema permite cadastrar perfumes, marcas e notas olfativas, associar notas a perfumes com sua respectiva camada (saída, coração, fundo), e buscar/filtrar perfumes por múltiplos critérios combinados.

---

## 📋 Sumário

- [Sobre o projeto](#-sobre-o-projeto)
- [Tecnologias](#-tecnologias)
- [Arquitetura e decisões técnicas](#-arquitetura-e-decisões-técnicas)
- [Modelo de domínio](#-modelo-de-domínio)
- [Como executar](#-como-executar)
- [Endpoints da API](#-endpoints-da-api)
- [Estrutura de pastas](#-estrutura-de-pastas)

---

## 🎯 Sobre o projeto

O PerfumeHub nasceu como um projeto de estudo aprofundado de backend com Java e Spring Boot, evoluindo bloco a bloco: modelagem → CRUD → busca e filtros → (próximas etapas: estoque, autenticação, testes, Docker, cache, mensageria e recomendação de perfumes).

Cada etapa foi desenvolvida em branch própria, revisada via Pull Request antes do merge, seguindo um fluxo próximo do ambiente de trabalho real. O desenvolvimento segue um roadmap incremental por blocos de funcionalidade — modelagem, CRUD, busca e filtros — com etapas futuras planejadas para estoque, autenticação, testes, containerização e recomendação de perfumes.

---

## 🛠 Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | PostgreSQL |
| Geração de schema | Hibernate (`ddl-auto`) |
| Mapeamento DTO ↔ Entity | MapStruct |
| Validação | Jakarta Bean Validation |
| Build | Maven |
| Boilerplate | Lombok |

---

## 🏗 Arquitetura e decisões técnicas

### Camadas
```
Controller → DTO → Service → Repository → Banco de Dados
```
Controllers são finos — apenas recebem a requisição, validam (`@Valid`) e delegam para o `Service`. Toda regra de negócio, conversão e orquestração vive no `Service`.

### DTOs desacoplados da entidade
Nenhuma entidade JPA é exposta diretamente em request ou response. Cada recurso tem seu `RequestDto` (com validação) e `ResponseDto` (moldado para o que o cliente precisa ver), evitando acoplar o contrato da API ao modelo interno de dados.

### Tratamento de erros centralizado
Um `@RestControllerAdvice` único (`GlobalHandlerException`) trata todas as exceções da aplicação de forma padronizada, retornando sempre o mesmo formato de erro (`timestamp`, `message`, `details`, `status`):

- `ResourceNotFoundException` → `404`
- `ResourceConflictException` → `409` (ex: nome duplicado, associação já existente)
- `MethodArgumentNotValidException` → `400`, agregando **todos** os erros de validação de uma vez (não só o primeiro)

### Validação de enums recebidos como String
Campos como `genre` e `concentration` chegam como `String` no `RequestDto` e são validados por uma anotação customizada (`@ValueOfEnum`), permitindo capturar múltiplos erros de enum inválido numa única resposta — o parser padrão do Jackson interrompe no primeiro erro encontrado, o que impediria reportar mais de um campo inválido por vez.

### Prevenção de duplicidade em nível de aplicação
Campos únicos (nome de marca, de perfume, de nota) são checados **antes** de persistir, retornando `409 Conflict` de forma explícita, em vez de deixar a constraint `UNIQUE` do banco estourar uma exceção genérica de integridade (que resultaria em `500`).

### Busca e filtros com JPA Specifications
A listagem de perfumes (`GET /api/perfumes`) aceita múltiplos filtros opcionais e combináveis (nome, marca, gênero, concentração, faixa de preço, nota olfativa) implementados como `Specification`s isoladas e testáveis, combinadas dinamicamente conforme os parâmetros informados. A busca por nota atravessa dois relacionamentos (`Perfume → PerfumeNote → OlfactoryNote`) via `JOIN` explícito na Criteria API.

### MapStruct para mapeamento
Toda conversão entre entidade e DTO é gerada em tempo de compilação via MapStruct, evitando reflection em runtime e expondo erros de mapeamento já no build, não em produção. Mappers são encadeados via `uses` para resolver objetos aninhados (ex: `PerfumeMapper` reaproveita `BrandMapper` e `PerfumeNoteMapper`).

### Segurança de referência entre recursos
Operações sobre sub-recursos aninhados (ex: remover uma nota de um perfume) validam que o recurso filho realmente pertence ao recurso pai informado na URL, evitando que um ID manipulado afete um registro fora do escopo esperado.

---

## 🗂 Modelo de domínio

```
Marca (Brand)
  │
  └──1:N──< Perfume >──N:N──< NotaOlfativa (OlfactoryNote)
                │                        │
                └────< PerfumeNote >─────┘
                   (camada: saída/coração/fundo)
```

**Perfume**: nome, descrição, preço, quantidade em estoque, gênero, concentração, marca.

**PerfumeNote**: entidade associativa entre `Perfume` e `OlfactoryNote`, carregando o atributo `type` (camada olfativa: `TOP`, `MIDDLE`, `BASE`). Constraint composta única em `(perfume_id, note_id, type)`.

---

## ▶️ Como executar

### Pré-requisitos
- Java 21+
- Maven
- PostgreSQL rodando localmente (ou via Docker, quando o Bloco 8 for implementado)

### Passos

```bash
git clone https://github.com/KaykeS8/perfumeHUB.git
cd perfumeHUB

cp .env.example .env
# preencha .env com suas credenciais de banco

mvn clean install
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. O schema do banco é gerado/atualizado automaticamente pelo Hibernate na inicialização, conforme a propriedade `spring.jpa.hibernate.ddl-auto` configurada.

---

## 📡 Endpoints da API

### Marca (`/api/brand`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/brand` | Cria marca |
| GET | `/api/brand` | Lista paginada |
| GET | `/api/brand/{id}` | Busca por ID |
| PUT | `/api/brand/{id}` | Atualiza |
| DELETE | `/api/brand/{id}` | Remove |

### Nota Olfativa (`/api/notes`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/notes` | Cria nota |
| GET | `/api/notes` | Lista paginada |
| GET | `/api/notes/{id}` | Busca por ID |
| PUT | `/api/notes/{id}` | Atualiza |
| DELETE | `/api/notes/{id}` | Remove |

### Perfume (`/api/perfumes`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/perfumes` | Cria perfume |
| GET | `/api/perfumes` | Lista paginada, com filtros opcionais (ver abaixo) |
| GET | `/api/perfumes/{id}` | Busca por ID, incluindo marca e notas associadas |
| PUT | `/api/perfumes/{id}` | Atualização completa |
| DELETE | `/api/perfumes/{id}` | Remove |

**Filtros disponíveis em `GET /api/perfumes`** (todos opcionais e combináveis):

```
?name=Sauvage
?brand=Dior
?genre=MASCULINO
?concentration=EDP
?minPrice=100&maxPrice=500
?note=Bergamota
```

### Associação Perfume ↔ Nota (`/api/perfumes/{perfumeId}/notes`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/perfumes/{perfumeId}/notes` | Associa uma nota ao perfume (com camada) |
| GET | `/api/perfumes/{perfumeId}/notes` | Lista as notas do perfume |
| DELETE | `/api/perfumes/{perfumeId}/notes/{id}` | Remove a associação |

---

## 📁 Estrutura de pastas

```
src/main/java/com/simao/perfumehub
├── controllers      → Endpoints REST
├── services         → Regras de negócio
├── repositories     → Acesso a dados (Spring Data JPA)
├── entities         → Entidades JPA
│   └── enums        → Genre, Concentration, NoteType
├── dtos             → Request/Response DTOs, organizados por recurso
├── mapper           → Interfaces MapStruct
├── specifications   → JPA Specifications para busca/filtros
├── validation       → Anotações de validação customizadas (@ValueOfEnum)
└── exceptions       → Exceções customizadas e handler global
```

---

## 👤 Autor

Kayke — [GitHub](https://github.com/KaykeS8)
