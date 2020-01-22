# Plataforma distribuida de ingesta de datos via streaming

## Integrandes del grupo
- [Juan Pablo Martínez](https://github.com/jpm343)
- [José Mellado](https://github.com/JoseMellado)

## Descripción del problema
En la actualidad, el manejo de grandes cantidades de datos en tiempo real se ha vuelto de vital importancia en la industria de la tecnología, uno de los principales problemas que surgen de esto es el envío de datos, como disponibilizar a los usuarios cantidades de datos tan enormes sin sobrecargar con peticiones a la plataforma que dispone de los datos.

Para abordar este problema, se realizará un recomendador de tragos, en donde se recibe un streaming de datos con diversos tipos de licores de todo el mundo con su recetas e ingredientes.

## Enfoque de solución
El enfoque para abordar este problema consiste en desarrollar un consumidor que se suscriba a la entidad que publica los datos de los licores, con el fin de obtener los datos que el publicador envía y así guardarlos en una base de datos que podrá ser consultada por una pequeña aplicación desarrollada en Spring y Cue.

## Tecnologías a utilizar
- Spring Boot: Framework de Java para el desarrollo del backend de la aplicación web.
- Vue.js Framework de JavaScript para desarrollo del frontend de la aplicación web.
- Apache Kafka: Plataforma distribuida que permite la publicación y consumo de streaming de datos.
- Apache Cassandra: Base de datos NoSQL distruibuida y basada en el modelo clave-valor. Permite grandes volúmenes de datos en forma distribuida.
- Docker-compose: Plataforma que automatiza el despliegue de aplicaciones dentro de contenedores de software.

## Arquitectura implementada
La arquitectura implementada se muestra en el siguiente diagrama de despliegue:
![alt text](https://raw.githubusercontent.com/jpm343/Recomendador_kafka/master/misc/diagrama_arch.png)

## Pasos para la ejecución de la plataforma
Antes de ejecutar la aplicación es necesario instalar ciertas dependencias de python
### Dependencias a instalar:
**Confluent-kafka**
Esta librería de python se utiliza para establecer conexión con *Kafka*. Necesaria para desarrollar los scripts productores y consumidores.
Para instalar se debe usar el siguiente comando:
```
pip install confluent-kafka
```
**Cassandra-driver**
Librería utilizada para establecer conexión con las base de datos *Cassandra*. Necesaria para almacenar los datos que produce el script productor.
Para instalar usar el siguiente comando:
```
pip install cassandra-driver
```
### Ejecución de scripts de docker
Los scrpits de docker compose se utilizar para inicializar ciertas funcionalidades necesarias para el funcionamiento de la plataforma. Estos vienen incluidos en el repositorio.

**Primer script: Cassandra**

Para crear la base de datos se ejecutan los siguientes comandos en la ruta principal del repositorio:
```
cd backend
docker-compose up -d
```
Una vez creada la base de datos, es necesario crear un *KeySpace* dentro de ella. Para eso, se ejecutan los siguientes comandos:
```
docker exec -it backend_cassandra_1 bash
cqlsh -u cassandra -p cassandra
CREATE KEYSPACE backend WITH replication = {'class': 'SimpleStrategy', 'replication_factor':'1'};
```
Cabe destacar que el script anterior debe ser ejecutado sólo una vez.

**Segundo script: Kafka**

Volviendo a la ruta principal del repositorio, se ejecutan los siguiente comandos para ejecutar kafka:
```
cd kafka_cluster
docker-compose up -d
```
### Ejecución de Backend y scripts productores-consumidores

**Ejecución del backend**

Para ejecutar el backend se ejecutan los siguientes comandos desde el directorio base del repositorio
```
cd backend
mvn compile
mvn install
mvn spring-boot:run
```
Una vez ejecutado ya se puede hacer funcionar el productor y el consumidor para obtener datos y almacenarlos en cassandra.

**Ejecutar el consumidor**
Desde el directorio base ejecutar lo siguiente:
```
cd kafka_consumer
python consumer.py
```

**Ejecutar el productor**
Sin cerrar el consumidor, se ejecutan los siguientes comandos desde el directorio base:
```
cd kafka_producer
python producer.py
```
Con esto se hace un *stream* de datos que serán capturados por cassandra. Se recomienda ejecutar el productor por períodos cortos de tiempo para evitar que la base de datos quede demasiado grande.

**Ejecución del frontend**
Para ejecutar el frontend, se aplican los siguientes comandos desde el directorio base:
```
cd frontend
npm install
npm run serve
```
Una vez que cargue, ya se puede acceder a la plataforma en el navegador ingresando la dirección: localhost:8080

