package com.northcoders.recordshop.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Artist {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    Long id;

    @Column
    String name;

}
