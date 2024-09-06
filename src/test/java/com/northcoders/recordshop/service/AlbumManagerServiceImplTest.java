package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import com.northcoders.recordshop.repository.AlbumRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class AlbumManagerServiceImplTest {

    @Mock
    private AlbumRepository mockAlbumRepository;

    @InjectMocks
    private AlbumManagerServiceImpl albumManagerServiceImpl;


    @Test
    @DisplayName("Returns a list of all albums when method called.")
    public void testAlbumManagerService_getAllAlbums() {

        List<Album> albumList = new ArrayList<>();
        Artist kraftWerk = Artist.builder()
                .artistId(1L)
                .name("Kraftwerk").build();

        Publisher klingKlang = Publisher.builder()
                .publisherId(1L)
                .name("Kling Klang")
                .build();

        Album menschMaschine = Album.builder()
                .albumId(1L)
                .name("Die Mensch-Maschine")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1978, 4, 28))
                .genre(Genre.ELECTRONIC)
                .build();

        Album computerWelt = Album.builder()
                .albumId(2L)
                .name("Computerwelt")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1981, 2, 11))
                .genre(Genre.ELECTRONIC)
                .build();

        albumList.add(menschMaschine);
        albumList.add(computerWelt);

        when(mockAlbumRepository.findAll()).thenReturn(albumList);

        List<Album> actualResults = albumManagerServiceImpl.getAllAlbums();

       assertEquals(albumList, actualResults);


    }
}