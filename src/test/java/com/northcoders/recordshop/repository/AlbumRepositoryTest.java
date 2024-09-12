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


    private Artist kraftWerk = new Artist();
    private Publisher klingKlang = new Publisher();
    private Album menschMaschine = new Album();


    @BeforeEach
    public void setup() {

        kraftWerk = Artist.builder()
                .artistId(1L)
                .name("Kraftwerk").build();


        klingKlang = Publisher.builder()
                .publisherId(1L)
                .name("Kling Klang")
                .build();

        menschMaschine = Album.builder()
                .albumId(1L)
                .name("Die Mensch-Maschine")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1978, 4, 28))
                .genre(Genre.ELECTRONIC)
                .build();
    }

    @Test
    @DisplayName("Returns album from database matching a valid album ID")
    public void testAlbumRepository_getAlbumById_WithValidAlbumId() {

        artistRepository.save(kraftWerk);
        publisherRepository.save(klingKlang);
        albumRepository.save(menschMaschine);

        Album result = albumRepository.findById(1L).get();

        assertEquals(1L, result.getAlbumId());

    }

    @Test
    @DisplayName("Returns a list containing albums in database")
    public void testAlbumRepository_getAllAlbums() {

        Album computerWelt;

        computerWelt = Album.builder()
                .albumId(2L)
                .name("Computerwelt")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1981, 2, 11))
                .genre(Genre.ELECTRONIC)
                .build();

        Album electricCafe;
        electricCafe = Album.builder()
                .albumId(1L)
                .name("Electric Café")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1986, 11, 10))
                .genre(Genre.ELECTRONIC)
                .build();

        artistRepository.save(kraftWerk);
        publisherRepository.save(klingKlang);
        albumRepository.save(menschMaschine);
        albumRepository.save(computerWelt);
        albumRepository.save(electricCafe);

        List <Album> albumResults = (List<Album>) albumRepository.findAll();

        assertThat(albumResults).isNotEmpty();
        assertThat(albumResults.size()).isEqualTo(2);
    }




    @Test
    @DisplayName("")
    public void testAlbumRespository_saveAlbumWhenAlbumArtistPublisherAlreadyExist() {

        artistRepository.save(kraftWerk);
        publisherRepository.save(klingKlang);
        albumRepository.save(menschMaschine);
        albumRepository.save(menschMaschine);

        List <Album> result = (List<Album>) albumRepository.findAll();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);

    }



    }


