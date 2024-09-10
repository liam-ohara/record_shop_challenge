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
    Artist artist2 = new Artist();
    Publisher klingKlang = new Publisher();
    Publisher publisher2 = new Publisher();
    Album menschMaschine = new Album();
    Album menschMaschineDuplicate = new Album();

    @BeforeEach
    public void setup() {

        kraftWerk = Artist.builder()
                .name("Kraftwerk").build();

        artist2 = Artist.builder()
                .name("Artist Two").build();

        klingKlang = Publisher.builder()
                .name("Kling Klang")
                .build();

        publisher2 = Publisher.builder()
                .name("Publisher Two").build();

        menschMaschine = Album.builder()
                .name("Die Mensch-Maschine")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1978, 4, 28))
                .genre(Genre.ELECTRONIC)
                .build();

        menschMaschineDuplicate = Album.builder()
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

    @Test
    @DisplayName("")
    public void testAlbumRespository_saveAlbumWhenAlbumArtistPublisherAlreadyExist() {

        artistRepository.save(kraftWerk);
        publisherRepository.save(klingKlang);
//        artistRepository.save(artist2);
//        publisherRepository.save(publisher2);
        albumRepository.save(menschMaschine);
        albumRepository.save(menschMaschineDuplicate);

        List<Album> results = (List<Album>) albumRepository.findAll();

        Optional<Album> result = Optional.ofNullable(results.get(1));

//        Need handling of duplicate artists/publishers
//
        assertAll(
                () -> assertEquals(2L, result.get().getAlbumId()),
                () -> assertEquals("Die Mensch-Maschine", result.get().getName()),
                () -> assertEquals(1L, result.get().getArtist().getArtistId()),
                () -> assertEquals(1L, result.get().getPublisher().getPublisherId()));
    }

    }


