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
    @Column (name = "artist_id")
    Long artistId;

    @Column
    String name;



}
