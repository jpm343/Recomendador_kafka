package com.grupo.uno.backend.model;

import com.datastax.driver.core.DataType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table
@Getter
@Setter
public class Drink {

    @PrimaryKey
    @CassandraType(type = DataType.Name.UUID)
    private UUID id;

    @Column
    private String category;

    @Column
    private String directions;

    //this is a list that should be parsed to a json object.
    //maybe a transient variable...
    @Column
    private String ingredients;

    //the next 2 fields comes froms a size 2 array
    @Column
    private String firstRating;

    @Column
    private String secondRating;
    
    @Column
    private String title;

}
