# Plataforma distribuida de ingesta de datos via streaming

## Integrandes del grupo
- [Juan Pablo Martínez](https://github.com/jpm343)
- [José Mellado](https://github.com/JoseMellado)

## Descripción del problema
En la actualidad, el manejo de grandes cantidades de datos en tiempo real se ha vuelto de vital importancia en la industria de la tecnología, uno de los principales problemas que surgen de esto es el envío de datos, como disponibilizar a los usuarios cantidades de datos tan enormes sin sobrecargar con peticiones a la plataforma que dispone de los datos.

Para abordar este problema, se realizará un recomendador de tragos, en donde se recibe un streaming de datos con diversos tipos de licores de todo el mundo con su recetas e ingredientes.

## Enfoque de solución
El enfoque para abordar este problema consiste en desarrollar un consumidor que se suscriba a la entidad que publica los datos de los licores, con el fin de obtener los datos que el publicador envía y así guardarlos en una base de datos que podrá ser consultada por una pequeña aplicación desarrollada en Spring y Cue.

## Tecnologas a utilizar
- Spring Boot: Framework de Java para el desarrollo del backend de la aplicación web.
- Vue.js Framework de JavaScript para desarrollo del frontend de la aplicación web.
- Apache Kafka: Plataforma distribuida que permite la publicación y consumo de streaming de datos.
- Apache Cassandra: Base de datos NoSQL distruibuida y basada en el modelo clave-valor. Permite grandes volúmenes de datos en forma distribuida.

- Cassandra: Base de datos NoSQL distruibuida y basada en el modelo clave-valor. Permite grandes volúmenes de datos en forma distribuida.
20
