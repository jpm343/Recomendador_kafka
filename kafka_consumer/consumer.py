from confluent_kafka import Consumer, KafkaError
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider
import ast
import uuid

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

    session.execute(
        """
        INSERT INTO drink (id, cateogry, directions, firtrating, ingredients, secondrating)
        VALUES (%s, %s, %s, %s, %s, %s)
        """,
        (uuid.uuid1(), obj['category'], obj['directions'], obj['rating'][0], str(obj['ingredients']), obj['rating'][1])
    )
    
c.close()

