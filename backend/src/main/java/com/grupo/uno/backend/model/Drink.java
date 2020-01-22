package com.grupo.uno.backend.model;

import com.datastax.driver.core.DataType;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table
public class Drink {

    @PrimaryKey
    @CassandraType(type = DataType.Name.UUID)
    private UUID id;

    @Column
    private String cateogry;

    @Column
    private String directions;

    //this is a list that should be parsed to a json object.
    //maybe a transient variable...
    @Column
    private String ingredients;

    //the next 2 fields comes froms a size 2 array
    @Column
    private String firtRating;

    @Column
    private String secondRating;
}
