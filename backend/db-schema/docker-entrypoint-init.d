CREATE KEYSPACE IF NOT EXISTS backend WITH replication =
{'class':'SimpleStrategy', 'replication_factor':'1'};