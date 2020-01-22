from confluent_kafka import Producer
import json
import random

#declare producer and topic to produce to
p = Producer({'bootstrap.servers': 'localhost:9092'})
topic = "drinks"

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
    #poll for previos produce() callbacks
    p.poll(0)

    while True:
        i = random.randint(0, len(object_json)-1)
        drink = object_json[i]
        p.produce(topic, str(drink).encode('utf-8'), callback = delivery_report)
        p.flush()

except KeyboardInterrupt:
    pass