package com.grupo.uno.backend.repository;

import com.grupo.uno.backend.model.Drink;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DrinkRepository extends CassandraRepository <Drink, UUID> {
}
