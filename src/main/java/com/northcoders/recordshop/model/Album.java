package com.northcoders.recordshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

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

    @ManyToOne
    @JoinColumn(referencedColumnName = "artist_id", nullable = false)
    Artist artist;

    @ManyToOne
    @JoinColumn(referencedColumnName = "publisher_id", nullable = false)
    Publisher publisher;

    @Column
    LocalDate releaseDate;

    @Column
    Genre genre;
}
