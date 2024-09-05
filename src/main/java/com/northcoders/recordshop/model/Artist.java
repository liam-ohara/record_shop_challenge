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
    private Long id;

    @Column
    private String name;

    @OneToOne(mappedBy = "album")
    private Album album;

}
