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
    //Repository test for duplicate POST/save requests with same record fail if
    // GeneratedValue is in place. However without this, the user will need to know
    // the next ID available
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long albumId;

    @Column
    String name;

    @ManyToOne
            //Cascade annotation is required to pass repository test for duplicate
            //POST/save requests with same record
            (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(referencedColumnName = "artist_id", nullable = false)
    Artist artist;

    @ManyToOne
            (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(referencedColumnName = "publisher_id", nullable = false)
    Publisher publisher;

    @Column
    LocalDate releaseDate;

    @Column
    Genre genre;
}
