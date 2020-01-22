from confluent_kafka import Consumer, KafkaError
from cassandra.cluster import Cluster
from cassanta.auth import PlainTextAuthProvider

c = Consumer({
    'bootstrap.servers': '',
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

    print('Received message: {}'.format(msg.value().decode('utf-8')))
    value = msg.value()

c.close()

