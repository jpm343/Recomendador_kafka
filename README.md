# Plataforma distribuida de ingesta de datos via streaming

## Integrandes del grupo
- [Juan Pablo Martínez](https://github.com/jpm343)
- [José Mellado](https://github.com/JoseMellado)

## Descripción del problema
En la actualidad, el manejo de grandes cantidades de datos en tiempo real se ha vuelto de vital importancia en la industria de la tecnología, uno de los principales problemas que surgen de esto es el envío de datos, como disponibilizar a los usuarios cantidades de datos tan enormes sin sobrecargar con peticiones a la plataforma que dispone de los datos.

Para abordar este problema, se realizará un recomendador de tragos, en donde se recibe un streaming de datos con diversos tipos de licores de todo el mundo con su recetas e ingredientes.

## Enfoque de solución
El enfoque para abordar este problema consiste en desarrollar un consumidor que se suscriba a la entidad que publica los datos de los licores, con el fin de obtener los datos que el publicador envía y así guardarlos en una base de datos que podrá ser consultada por una pequeña aplicación desarrollada en Spring y Vue.

## Tecnologías a utilizar
- Spring Boot: Framework de Java para el desarrollo del backend de la aplicación web.
- Vue.js Framework de JavaScript para desarrollo del frontend de la aplicación web.
- Apache Kafka: Plataforma distribuida que permite la publicación y consumo de streaming de datos.
- Apache Cassandra: Base de datos NoSQL distruibuida y basada en el modelo clave-valor. Permite grandes volúmenes de datos en forma distribuida.
- Docker-compose: Plataforma que automatiza el despliegue de aplicaciones dentro de contenedores de software.

## Principales inconvenientes y barreras detectadas:

Los principales inconvenientes fueron la curva de aprendizaje de los lenguajes de programación y de bases de datos
utilizados en el trabajo, pues es primera vez que se trabaja con tecnologías como Cassandra. Otras barreras fueron
también el adaptarse al uso de Kafka, ya que este es considerado como una tecnología relativamente nueva. Si bien
El streaming de datos es un enfoque muy útil para problemas actuales, es un poco difícil adaptarse a éste, ya que
es muy diferente al que se ha usado con anterioridad.

Otros inconvenientes fueron también los relacionados a la administración de servidores, pues se hizo imposible acceder
a un servicio de hosting, por lo que la aplicación sólo puede ser ejecutada en modo local.


## Funciones principales:

A continuación se muestran los principales scripts de python utilizados en el productor y el consumidor. Notar que el
productor toma datos de tragos definidos en un archivo json y los publica a Kafka en el tópico "drinks". Por otro lado
el script consumidor se suscribe a dicho tópico y se conecta a una base de datos Cassandra para almacenar la información
capturada.

**Productor**
```python
from confluent_kafka import Producer
import json
import random

#declare producer and topic to produce to
p = Producer({'bootstrap.servers': 'localhost:9092'})
topic = "drinks"

#function to report the delivery of the messages sent to the topic
def delivery_report(err, msg):
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

#import data source from json
path_json = "drinks.json"
string_json = open(path_json, 'r')
object_json = json.load(string_json)

#produce random data from json
try:
    #poll for previous produce() callbacks
    p.poll(0)

    #randomly generate messages to send
    while True:
        i = random.randint(0, len(object_json)-1)
        drink = object_json[i]
        p.produce(topic, str(drink).encode('utf-8'), callback = delivery_report)

        #flush in order to wait to message queue to be empty
        p.flush()

except KeyboardInterrupt:
    pass

```

**consumidor**
```python
from confluent_kafka import Consumer, KafkaError
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider
import ast
import uuid

#connect to kafka
c = Consumer({
    'bootstrap.servers': 'localhost:9092',
    'group.id': 'mygroup',
    'auto.offset.reset': 'earliest'
})

#subscribe to 'drinks' topic
c.subscribe(['drinks'])

#connect with cassandra database
auth_provider = PlainTextAuthProvider(username = 'cassandra', password = 'cassandra')
cluster = Cluster(['127.0.0.1'], port = 9042, auth_provider = auth_provider)

#connect to backend keyspace
session = cluster.connect('backend')

#start receiving messages
while True:
    msg = c.poll(1.0)

    if msg is None:
        continue
    if msg.error():
        print("Consumer error: {}".format(msg.error()))
        continue
    
    value = msg.value().decode('utf-8')
    print('Received message: {}'.format(value))

    #parse string to object
    obj = ast.literal_eval(value)

    #insert object to cassandra database
    session.execute(
        """
        INSERT INTO drink (id, category, directions, firstrating, ingredients, secondrating)
        VALUES (%s, %s, %s, %s, %s, %s)
        """,
        (uuid.uuid1(), obj['category'], obj['directions'], obj['rating'][0], str(obj['ingredients']), obj['rating'][1])
    )
    
c.close()
```

## Arquitectura implementada
La arquitectura implementada se muestra en el siguiente diagrama de despliegue:
![alt text](https://raw.githubusercontent.com/jpm343/Recomendador_kafka/master/misc/diagrama_arch.png)

## Resultados
### BackEnd
Los servicios implementados en el backend son:

| path | método | descripción |
| :---: | :---: | :---: |
| /Drink/ | GET | Obtiene todos los tragos que se han recopilado en la base de datos |

### FrontEnd
Se desarrolló una sola vista para el frontend:
![alt text](https://raw.githubusercontent.com/jpm343/Recomendador_kafka/master/misc/home.png)
![alt text](https://raw.githubusercontent.com/jpm343/Recomendador_kafka/master/misc/table.png)

### Base de Datos

| Usuario | Contraseña | Keyspace |
| :---: | :---: | :---: |
| Cassandra | Cassandra | backend |

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

