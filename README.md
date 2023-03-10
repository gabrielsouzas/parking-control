# Parking Control - Projeto do Curso de Spring Boot - Michelli Brito - Youtube

Projeto desenvolvido com o video [Spring Boot | Curso Completo 2022](https://youtu.be/LXRU-Z36GEU)

# Modelagem e Configuração

## Base de Dados
>A base de dados foi criada com o PostgreSQL 15.1:

~~~SQL
CREATE DATABASE parking-control-db;
~~~

## resourses/application.properties
>Configurações da Base de Dados JPA, como username, password, etc.

~~~javascript
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

## ParkingSpotModel
>É uma classe Java que faz a representação da vaga de estacionamento dentro do projeto.

**Principais atributos/anotações:**

* @Entity - Define a classe como uma entidade, como a representação descrita acima;
* @Table - Define a classe como uma tabela do banco de dados;
* serialVersionUID - Número serial para uso da JVM;
* @Id - Define a variavel como o id da classe/tabela, toda classe definida como @entity deve ter essa anotação;
* @GeneratedValue - Define que a variavel terá um valor autogerado sequencial
* @Column - Define que a variavel será mais uma coluna da classe/tabela

Em cada anotação pode ou não ser passado um ou mais parâmetros, dependendo do tipo de anotação e da necesidade da regra de negócios.

## ParkingSpotRepository
>Interface que faz o controle das transações com o banco de dados.

**Principais atributos/anotações:**

* @Repository - Define que a interface é um repositório, que faz transações com o banco de dados, ao extender a interface com o JpaRepository não tem a necessidade de inserir essa anotação na interface;
* extends JpaRepository<ParkingSpotModel, UUID> - Define que essa interface carrega todos os métodos do JpaRepository, que possui varios métodos prontos para transações com o banco, como save, update, delete, entre outros.
* public boolean existsByLicensePlateCar(String licensePlateCar) - Método que verifica se a placa passada pelo cliente já existe no banco de dados. **Importante**: Precisa ser declarado no Repository;

## ParkingSpotService
>Classe que faz a camada intermediaria entre o Controller e o Repository.

**Principais atributos/anotações:**

* public ParkingSpotService ou @Autowired - Define que em determinados momentos o spring vai ter que injetar dependências do repository dentro do service;
* @Transactional - Anotação importante para evitar quebras de dados em casos de transações com um banco de dados;
* public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) - Método que salva os dados passados pelo Dto no banco de dados utilizando um método de mesmo nome do JPA (save).

## ParkingSpotController
>Classe que faz o controle entre os dados passados pelo cliente (ParkingSpotDto) com as transações no banco de dados (ParkingSpotService)

**Principais anotações/atributos:**

* @RestController - Define que será uma API Rest;
* @CrossOrigin - Define que pode ser acessado de qualquer ponto;
* @RequestMapping - Mapeamento do recurso a nivel de classe;
* public ParkingSpotController - Ponto de injeção do Service dentro do Controller;
* @PostMapping - Define o mapeamento do método POST. Não passa uma URI pois a classe já faz essa tarefa, assim quando o cliente solicitar o método POST da URI parking-spot será direcionado para este método;


## ParkingSpotDto
>Classe que faz a validação dos dados que vão para o banco. Ou seja, possui apenas os campos que o cliente vai aferir, campos como data e id não entram por não passarem pelo cliente.

**Principais atributos/anotações:**

* @NotBlank - Verifica se a variavel está nula ou vazia;
* @Size(max = 7) - Define um tamanho máximo de 7 caracteres para a variavel

## Requisições - Postman
>Descrição de como foram efetuadas as requisições para a API utilizando o Postman.

**Passo a passo:**

1. Baixe e instale o software Postman;
2. Crie uma conta;
3. Crie uma Collection (Parking Spot Endpoints);
4. Dentro da Collection crie as Requisições:
    * POST - http://localhost:8080/parking-spot
        * body:
            ~~~JSON
                {
                    "parkingSpotNumber" : "2858",
                    "licensePlateCar" : "AVF458",
                    "brandCar" : "Audi",
                    "modelCar" : "Q5",
                    "colorCar" : "Black",
                    "responsibleName" : "Carlos Suarez",
                    "apartment" : "205",
                    "block" : "8"
                }
            ~~~
    * GET ALL - http://localhost:8080/parking-spot
    * GET ONE - http://localhost:8080/parking-spot/id
    * DELETE - http://localhost:8080/parking-spot/id
    * PUT - http://localhost:8080/parking-spot/id
        * body:
            ~~~JSON
                {
                    "parkingSpotNumber" : "2858",
                    "licensePlateCar" : "ATR859",
                    "brandCar" : "BMW",
                    "modelCar" : "X1",
                    "colorCar" : "White",
                    "responsibleName" : "Carlos Suarez",
                    "apartment" : "205",
                    "block" : "8"
                }
            ~~~
5. Salve cada uma das requisições;
6. Suba a aplicação e teste usando o botão **Send**.

**Possíveis erros com as Requisições**

* Ocorreu um erro ao efetuar a validação dos dados, por causa da query enviada ao PostgreSQL na versão 9.3, que não conseguiu interpreta-la. A solução foi atualizar o PostgreSQL para a última versão, a 15.1 até o momento.