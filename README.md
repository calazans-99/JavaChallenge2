# 🚦 Smart Parking – Backend (Spring Boot + Thymeleaf)

> Aplicação web para gestão de **Motos**, **Sensores**, **Pátios**, **Status das Motos** e **Usuários** com autenticação, autorização por papéis e telas server‑side com Thymeleaf.

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
- [Produção (MySQL)](#-produção-mysql)
- [Licença](#-licença)
- [Autores](#-autores)

---

## ✨ Destaques

- **Java 17**, **Spring Boot 3**, **Spring MVC**, **Spring Data JPA**, **Thymeleaf**
- **Autenticação** (form login) e **autorização por perfis** (`ADMIN`, `OPERADOR`)
- **Migrações Flyway** (versionadas por banco) — schema versionado e _seeds_ idempotentes
- **2 fluxos além de CRUD**:
  1) **Ativar / Manutenção** de moto  
  2) **Ocupar / Liberar** sensor
- **Open-In-View desativado** e consultas com **`@EntityGraph`** (evita `LazyInitializationException`)
- Pronto para **H2 em dev** e **MySQL em produção**

---

## 🧱 Arquitetura & Stack

- **Camadas**: Controller → Service → Repository → JPA
- **View**: Thymeleaf + fragmentos reutilizáveis (cabeçalho, alerts, voltar)
- **Segurança**: Spring Security, `UserDetailsService` com authorities `ROLE_ADMIN`, `ROLE_OPERADOR`
- **Banco**: H2 (dev) \| MySQL (mysql/prod)

**Entidades**: `User`, `Funcao`, `Moto`, `StatusMoto`, `Patio`, `Sensor`

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

> Criados por migrations idempotentes (veja `db/migration/common` e `db/migration/*`).

---

## 💻 Como rodar localmente (dev)

**Pré-requisitos**
- Java 17
- Maven 3.9+

### A) H2 (rápido para desenvolvimento)
```bash
mvn spring-boot:run
# App: http://localhost:8081
# H2:  http://localhost:8081/h2-console  (JDBC: jdbc:h2:mem:testdb | user: sa | pass: vazio)
```

### B) MySQL local (sem Docker)
1. Crie um schema `smart_parking` (ou deixe `createDatabaseIfNotExist=true` criar).
2. Exporte variáveis ou edite `src/main/resources/application-mysql.properties`.
3. Rode com o perfil `mysql`:
```bash
mvn -Dspring-boot.run.profiles=mysql spring-boot:run
# App: http://localhost:8081
```

### C) MySQL via Docker (recomendado)
`docker-compose.yml` (exemplo):
```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: smart-parking-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: smart_parking
      MYSQL_USER: app
      MYSQL_PASSWORD: app
    ports:
      - "3306:3306"
    command:
      - --default-authentication-plugin=mysql_native_password
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-proot"]
      interval: 10s
      timeout: 5s
      retries: 10
    volumes:
      - mysql_data:/var/lib/mysql
volumes:
  mysql_data:
```

Com o container no ar:
```bash
docker compose up -d
mvn -Dspring-boot.run.profiles=mysql spring-boot:run
```

**Variáveis padrão (exemplo)**
```
DB_URL=jdbc:mysql://localhost:3306/smart_parking?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=app
DB_PASS=app
```

---

## ⚙️ Perfis & Configuração

### `application.properties` (dev – H2)
- H2 em memória (modo compatível com MySQL)
- `spring.jpa.open-in-view=false`
- Flyway aplicado: `db/migration/common` + `db/migration/h2`

```properties
server.port=8081
spring.application.name=smart-parking

# H2
spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration/common,classpath:db/migration/h2

# H2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### `application-mysql.properties` (MySQL – dev/prod)
- MySQL 8 (driver `com.mysql.cj.jdbc.Driver`)
- Flyway aplicado: `db/migration/common` + `db/migration/mysql`

```properties
server.port=8081
spring.application.name=smart-parking

# MySQL
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/smart_parking?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASS:root}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration/common,classpath:db/migration/mysql

# Console H2 off
spring.h2.console.enabled=false
```

### `pom.xml` (dependências principais)
```xml
<dependencies>
  <!-- ...outras dependências... -->

  <!-- MySQL -->
  <dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
  </dependency>

  <!-- H2 (dev) -->
  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>
</dependencies>
```

---

## 🗃️ Migrações (Flyway)

- **H2 (dev):** `classpath:db/migration/common` + `classpath:db/migration/h2`
- **MySQL:** `classpath:db/migration/common` + `classpath:db/migration/mysql`
- Pastas já presentes no projeto:  
  - `db/migration/common` → scripts neutros (ex.: limpeza/seed comuns)  
  - `db/migration/h2` → sintaxe específica para H2  
  - `db/migration/mysql` → sintaxe específica para MySQL

Exemplos típicos (MySQL):
```sql
CREATE INDEX idx_sensor_patio ON sensor (id_patio);
CREATE INDEX idx_moto_patio   ON moto   (id_patio);
CREATE INDEX idx_moto_status  ON moto   (id_status_moto);
```

> O Flyway aplica automaticamente na inicialização (ver logs).

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
- **Security** com authorities `ROLE_...` e `hasRole()/hasAnyRole()`, CSRF nos forms
- **OpenAPI/Swagger** habilitado (via `OpenApiConfig`) — UI padrão em `/swagger-ui/index.html`

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
 │   ├─ db/migration/ (common, h2, mysql)
 │   ├─ templates/    (Thymeleaf + fragmentos)
 │   ├─ static/       (assets estáticos, ex.: images/avatar.png)
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

- **LazyInitializationException na view** → use `@EntityGraph`/`join fetch` nas listas (Sensores/Motos) com OSIV off.
- **Loop no /login** → confira `loginProcessingUrl("/do-login")` e `th:action="@{/do-login}"`.
- **Timezone/SSL no MySQL** → mantenha `serverTimezone=UTC`, `useSSL=false`, `allowPublicKeyRetrieval=true` na URL.
- **Migrations não rodaram** → verifique perfil ativo e `spring.flyway.locations` nos `application*.properties`.
- **Permissões DDL** → conceda `CREATE/ALTER` ao usuário na 1ª execução para o Flyway.
- **Engine/Charset** → garanta **InnoDB** + `utf8mb4_unicode_ci` para chaves estrangeiras e acentos.

---

## 📦 Produção (MySQL)

1. Disponibilize um MySQL 8 (cloud/VM).
2. Configure o ambiente do app:
   - `SPRING_PROFILES_ACTIVE=mysql`
   - `DB_URL` (ex.: `jdbc:mysql://<host>:3306/smart_parking?...`)
   - `DB_USER`, `DB_PASS`
3. Na 1ª execução, garanta permissões de DDL (Flyway).
4. Monitore logs para confirmar a aplicação das migrations.

---

## 👥 Autores

Marcus Vinicius de Souza Calazans — RM: 556620
Lucas Abud Berbel — RM: 557957

📅 Challenge 2025 – FIAP | 2TDS | 2º Semestre
