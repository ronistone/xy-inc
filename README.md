# XY-INC


Backend as a service


# Arquitetura

 - Foi escolhido o banco mongodb por ser uma banco nosql que nos 
 fornece uma estrutura mais dinâmica em relação aos objetos persistidos
 - Ao escolher o mongo foi utilizado apenas 2 collections, uma 
 que é a definição do schema e outra que são os dados persistidos.
 - Em uma solução com um banco relacional seria necessário 4 tabelas, 
 que seriam: schema, atributos do schema, objetos e atributos do objeto.
 Isto no forçaria a fazer vários join para se fazer consultas o que poderia prejudicar a performance
 
 - Toda a definição de tipos do schema é dinâmica bastando que o tipo que se 
 deseja utilizar esteja no enum `AttributesTypes` que define o nome que 
 deve vir no payload e o tipo que ele representa
 
 - Ao se tentar atualizar ou criar um objeto, os dados são validados
 com o schema permitindo segurança nos dados que estão sendo persistidos
 
 
 # Build e Execução
 
  - Todo o processo está automatizado com o `docker-compose` basta te-lo
  na máquina e executar:
    - `docker-compose up maven` para executar o build
    - `docker-compose up app` para startar a aplicação
    - Para a instalação do docker recomendo seguir o processo neste [link](https://docs.docker.com/compose/install/)
    
# Testes
   Para testar há uma collection com requisições prontas na raiz do projeto no arquivo `xy-inc.postman_collection.json`,
   - Comece criando  um schema com a request `Create Schema`
   - Com o nome do schema (no payload padrão esta `Product`) use o `Create new Instance` para persistir um objecto
   - Execute o `Get All Instances`
   - Execute o `Get Instance By Id` utilizando o id do objeto criado
   - Execute o `Update Instance Falha pelo tipo` para evidência a validação de tipo com base no schema definido
   - Execute o `Update Instance`
   - Execute o `Delete Instance`

# Técnologias
  - Java 11
  - Maven 3.6.3
  - Spring Boot
  - Mongo DB
  - Docker
  - Docker Compose
  - JUnit
  - Mockito
  - Sonar (Para validação de qualidade do código)