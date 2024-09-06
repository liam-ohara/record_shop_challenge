package com.northcoders.recordshop.repository;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    public void testAlbumRepository_getAllAlbums() {

        Artist kraftWerk = Artist.builder()
                .name("Kraftwerk").build();

        Publisher klingKlang = Publisher.builder()
                .name("Kling Klang")
                .build();

        Album menschMaschine = Album.builder()
                .name("Die Mensch-Maschine")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1978, 4, 28))
                .genre(Genre.ELECTRONIC)
                .build();

       artistRepository.save(kraftWerk);
       publisherRepository.save(klingKlang);
       albumRepository.save(menschMaschine);

       List<Album> results = (List<Album>) albumRepository.findAll();

        assertThat(results).contains(menschMaschine);
        }
    }
