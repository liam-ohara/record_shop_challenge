package com.northcoders.recordshop.repository;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.*;

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


    Artist kraftWerk = new Artist();
    Publisher klingKlang = new Publisher();
    Album menschMaschine = new Album();

    @BeforeEach
    public void setup() {

        kraftWerk = Artist.builder()
                .name("Kraftwerk").build();

        klingKlang = Publisher.builder()
                .name("Kling Klang")
                .build();

        menschMaschine = Album.builder()
                .name("Die Mensch-Maschine")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1978, 4, 28))
                .genre(Genre.ELECTRONIC)
                .build();
    }

    @Test
    @DisplayName("Returns a list containing albums in database")
    public void testAlbumRepository_getAllAlbums() {

       artistRepository.save(kraftWerk);
       publisherRepository.save(klingKlang);
       albumRepository.save(menschMaschine);

       List<Album> results = (List<Album>) albumRepository.findAll();

        assertThat(results).contains(menschMaschine);
        }


    @Test
    @DisplayName("Returns album from database matching a valid album ID")
    public void testAlbumRepository_getAlbumById_WithValidAlbumId() {

        artistRepository.save(kraftWerk);
        publisherRepository.save(klingKlang);
        albumRepository.save(menschMaschine);

        Optional<Album> result = albumRepository.findById(1L);

        assertEquals(1L, result.get().getAlbumId());
    }

    }


