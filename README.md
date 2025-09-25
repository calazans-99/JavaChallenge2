# 🚦 Smart Parking – Backend (Spring Boot + Thymeleaf)

> Aplicação web para gestão de **Motos**, **Sensores**, **Pátios**, **Status das Motos** e **Usuários** com autenticação, autorização por papéis e telas server-side com Thymeleaf.

---

## 🧾 Sumário  
- [Destaques](#-destaques)
- [Arquitetura & Stack](#%EF%B8%8F-arquitetura--stack)
- [Rotas & Permissões](#%EF%B8%8F-rotas--permissões)
- [Usuários de teste (dev)](#-usuários-de-teste-dev)
- [Como rodar localmente (dev)](#-como-rodar-localmente-dev)
- [Perfis & Configuração](#%EF%B8%8F-perfis--configuração)
- [Migrações (Flyway)](#%EF%B8%8F-migrações-flyway)
- [Regras de Negócio (Fluxos)](#-regras-de-negócio-fluxos)
- [Padrões aplicados](#-padrões-aplicados)
- [Estrutura do projeto](#%EF%B8%8F-estrutura-do-projeto)
- [Testes rápidos](#-testes-rápidos-curl)
- [Troubleshooting](#-troubleshooting)
- [Produção (PostgreSQL)](#-produção-postgresql)
- [Licença](#-licença)
- [Autores](#-autores)

---

## ✨ Destaques

- **Java 17**, **Spring Boot 3**, **Spring MVC**, **Spring Data JPA**, **Thymeleaf**  
- **Autenticação** (form login) e **autorização por perfis** (`ADMIN`, `OPERADOR`)  
- **Migrações Flyway** (V1…V4) — schema versionado e _seeds_ idempotentes  
- **2 fluxos além de CRUD**:
  1) **Ativar / Manutenção** de moto  
  2) **Ocupar / Liberar** sensor  
- **Open-In-View desativado** e consultas com **`@EntityGraph`** (evita `LazyInitializationException`)  
- Pronto para **H2 em dev** e **PostgreSQL em produção**

---

## 🧱 Arquitetura & Stack

- **Camadas**: Controller → Service → Repository → JPA  
- **View**: Thymeleaf + fragmentos reutilizáveis (cabeçalho, alerts, voltar)  
- **Segurança**: Spring Security, `UserDetailsService` com authorities `ROLE_ADMIN`, `ROLE_OPERADOR`  
- **Banco**: H2 (dev) \| PostgreSQL (prod)

**Entidades**
`User`, `Funcao`, `Moto`, `StatusMoto`, `Patio`, `Sensor`.

---

## 🗺️ Rotas & Permissões

| Recurso        | Rotas base             | Acesso                          |
|----------------|------------------------|---------------------------------|
| Login          | `/login`, `/do-login`  | Público                         |
| Home           | `/index`               | Autenticado                     |
| Usuários       | `/usuarios/**`         | **ADMIN**                       |
| Funções        | `/funcao/**`           | **ADMIN**                       |
| Motos          | `/motos/**`            | **ADMIN** ou **OPERADOR**       |
| Sensores       | `/sensores/**`         | **ADMIN** ou **OPERADOR**       |
| Pátios         | `/patios/**`           | **ADMIN** ou **OPERADOR**       |
| Status da Moto | `/status-moto/**`      | **ADMIN** ou **OPERADOR**       |
| H2 Console*    | `/h2-console`          | Público (apenas **dev**)        |

\* Em produção, **desabilitar** H2 console.

---

## 🔐 Usuários de teste (dev)

| Usuário | Senha | Perfil   | Acesso                                                                 |
|--------|-------|----------|------------------------------------------------------------------------|
| admin  | admin | ADMIN    | Total                                                                  |
| oper   | admin | OPERADOR | Sem acesso a `/usuarios/**` e `/funcao/**`; acesso ao restante         |

> Criados na **V4** (idempotente).

---

## 💻 Como rodar localmente (dev)

**Pré-requisitos**
- Java 17
- Maven 3.9+

**1) Subir**
```bash
mvn spring-boot:run
```

**2) Acessos**
- App: http://localhost:8081  
- H2 (dev): http://localhost:8081/h2-console  
  - JDBC URL: `jdbc:h2:mem:testdb`
  - User: `sa` / Password: *(vazio)*

**3) Checklist rápido de demo**
- Logue como `admin/admin` → abra **Motos** e **Sensores**.  
- Clique **Ativar/Manutenção** (moto) e **Ocupar/Liberar** (sensor) → veja alert de sucesso e status mudando.  
- Logue como `oper/admin` → confirme 403 em `/usuarios`.  

---

## ⚙️ Perfis & Configuração

**`application.properties` (dev – H2)**  
- H2 em memória  
- `spring.jpa.open-in-view=false`  
- Flyway habilitado

**`application-prod.properties` (exemplo – PostgreSQL)**
```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://<host>:5432/<db>?sslmode=require}
spring.datasource.username=${DB_USER:app}
spring.datasource.password=${DB_PASS:secret}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
spring.flyway.enabled=true
spring.h2.console.enabled=false
```

> Em produção: `SPRING_PROFILES_ACTIVE=prod` e variáveis `DB_URL/DB_USER/DB_PASS`.

**`pom.xml` (driver Postgres)**
```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```

---

## 🗃️ Migrações (Flyway)

- **V1** – limpeza de tabelas legadas  
- **V2** – criação das tabelas  
- **V3** – _seed_ inicial (ex.: ADMIN)  
- **V4** – **índices** em FKs, **papel OPERADOR**, **usuário `oper`** e vínculo

**Trecho do V4 (resumo):**
```sql
CREATE INDEX IF NOT EXISTS idx_sensor_patio ON sensor (id_patio);
CREATE INDEX IF NOT EXISTS idx_moto_patio   ON moto   (id_patio);
CREATE INDEX IF NOT EXISTS idx_moto_status  ON moto   (id_status_moto);

INSERT INTO funcao (nome)
SELECT 'OPERADOR' WHERE NOT EXISTS (SELECT 1 FROM funcao WHERE nome='OPERADOR');

INSERT INTO usuario (username, senha, nome_perfil, img_perfil)
SELECT 'oper',
       '$2a$12$h227p1QzQEB2cIW/BrzZletfr20O0lNDBMYZM0K6z5faY6bJ17kpO', -- admin (bcrypt)
       'Operador','https://via.placeholder.com/100'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username='oper');

INSERT INTO usuario_funcao_tab (id_usuario, id_funcao)
SELECT u.id, f.id
FROM usuario u JOIN funcao f ON f.nome='OPERADOR'
WHERE u.username='oper'
  AND NOT EXISTS (
    SELECT 1 FROM usuario_funcao_tab x
    WHERE x.id_usuario=u.id AND x.id_funcao=f.id
  );
```

> O Flyway aplica automaticamente na inicialização.

---

## 🧩 Regras de Negócio (Fluxos)

### 1) **Ativar / Manutenção de Moto**
- **Tela:** lista de Motos  
- **Ação:** altera `status` para `ATIVA` ou `EM_MANUTENCAO` via `MotoService` e exibe _feedback_.

### 2) **Ocupar / Liberar Sensor**
- **Tela:** lista de Sensores  
- **Ação:** alterna `status` do sensor entre **OCUPADO** e **LIVRE**, com _feedback_.

---

## 🧠 Padrões aplicados

- **OSIV OFF** (`spring.jpa.open-in-view=false`) + **`@EntityGraph`** nas listas (ex.: `Sensor.patio`, `Moto.patio/status`)  
- **Bean Validation** em entidades e `th:errors` nos formulários  
- **Security** com authorities `ROLE_...`, `.hasRole()` / `.hasAnyRole()`, CSRF nos forms

---

## 🗂️ Estrutura do projeto

```
src/
 ├─ main/java/br/com/fiap/universidade_fiap/
 │   ├─ controller/   (MVC Controllers)
 │   ├─ service/      (regras de negócio)
 │   ├─ repository/   (Spring Data JPA)
 │   ├─ model/        (Entidades JPA)
 │   └─ security/     (Config + UserDetailsService)
 ├─ main/resources/
 │   ├─ db/migration/ (V1…V4 – Flyway)
 │   ├─ templates/    (Thymeleaf + fragmentos)
 │   └─ application*.properties
 └─ test/             (opcional)
```

---

## 🧪 Testes rápidos (curl)

```bash
# Login (200)
curl -i http://localhost:8081/login

# Página protegida (302 para login se não autenticado)
curl -i http://localhost:8081/motos
```

---

## 🧯 Troubleshooting

- **LazyInitializationException na view** → use `@EntityGraph`/`join fetch` nas listas (já aplicado em Sensores; replicar em Motos se necessário).  
- **Loop no /login** → confira `loginProcessingUrl("/do-login")` e o `th:action="@{/do-login}"`.  
- **V4 não aparece no H2** → H2 é em memória; veja os logs do Flyway ao iniciar.

---

## 📦 Produção (PostgreSQL)

1. Adicione driver Postgres no `pom.xml`.  
2. Crie `application-prod.properties` (acima).  
3. Suba com `SPRING_PROFILES_ACTIVE=prod` e exporte `DB_URL/DB_USER/DB_PASS`.  
4. Garanta permissão de `CREATE/ALTER` na primeira execução (Flyway).

---

## 📜 Licença

Projeto acadêmico. Uso livre para fins educacionais.

---

## 👥 Autores

- Time do projeto – nomes/RM  
- Professor(a)/Turma (opcional)
