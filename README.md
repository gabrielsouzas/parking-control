# Parking Control - Projeto do Curso de Spring Boot - Michelli Brito - Youtube

Projeto desenvolvido com o video [Spring Boot | Curso Completo 2022](https://youtu.be/LXRU-Z36GEU)

## Modelagem e Configuração

### Base de Dados
A base de dados foi criada com o PostgreSQL:

~~~SQL
CREATE DATABASE parking-control-db;
~~~

### resourses/application.properties
Configurações da Base de Dados JPA, como username, password, etc.

~~~properties
// URL de conexão com o banco local
spring.datasource.url=jdbc:postgresql://localhost:5432/parking-control-db
// Nome de usuário e senha para acesso ao PosgreSQL
spring.datasource.username=USUARIO_POSTGRESQL
spring.datasource.password=SENHA_POSTGRESQL
// Configuração do JPA - Define que ao mapear e/ou criar entidades, colunas, restrições, já sejam criadas as mesmas coisas na base de dados, sem a necessidade da execução de scripts diretamente no gerenciador do banco
spring.jpa.hibernate.ddl-auto=update

// Evitar erros por conta da criação de metadados pelo hibernate
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
~~~


