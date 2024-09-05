package com.northcoders.recordshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Album {

    @Id
    @GeneratedValue
    @Column
    Long albumId;

    @Column
    String name;

    @OneToOne
    @JoinColumn(referencedColumnName = "artist_Id")
    Artist artist;

    @OneToOne
    @JoinColumn(referencedColumnName = "publisher_Id")
    Publisher publisher;

    @Column
    LocalDate releaseDate;

    @Column
    Genre genre;
}
