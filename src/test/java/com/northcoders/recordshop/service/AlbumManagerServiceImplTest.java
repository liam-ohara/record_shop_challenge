package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import com.northcoders.recordshop.repository.AlbumRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class AlbumManagerServiceImplTest {

    @Mock
    private AlbumRepository mockAlbumRepository;

    @InjectMocks
    private AlbumManagerServiceImpl albumManagerServiceImpl;

    List<Album> albumList = new ArrayList<>();
    Artist kraftWerk = new Artist();
    Publisher klingKlang = new Publisher();
    Album menschMaschine = new Album();
    Album computerWelt = new Album();
    Album invalidAlbum = new Album();
    Album invalidAlbumDate = new Album();

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

        computerWelt = Album.builder()
                .albumId(2L)
                .name("Computerwelt")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1981, 2, 11))
                .genre(Genre.ELECTRONIC)
                .build();

        invalidAlbumDate = Album.builder()
                .name("Electric Café")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(2981, 2, 11))
                .genre(Genre.ELECTRONIC)
                .build();
    }

    @Test
    @DisplayName("Returns a list of all albums when method called.")
    public void testAlbumManagerService_getAllAlbums() {

        albumList.add(menschMaschine);
        albumList.add(computerWelt);

        when(mockAlbumRepository.findAll()).thenReturn(albumList);

        List<Album> actualResults = albumManagerServiceImpl.getAllAlbums();

       assertEquals(albumList, actualResults);

    }

    @Test
    @DisplayName("Returns album when passed a valid album ID")
    public void testAlbumManagerService_getAlbumById_WhenPassValidId() {

        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.ofNullable(menschMaschine));

        Album result = albumManagerServiceImpl.getAlbumById(1L);

        assertEquals(menschMaschine, result);

    }

    @Test
    @DisplayName("Throws NullPointerException when passed an invalid album ID")
    public void testAlbumManagerService_getAlbumById_WhenPassedInvalidId() {

        when(mockAlbumRepository.findById(3L)).thenThrow(new NullPointerException("No album found with that id: 3"));

        NullPointerException thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            albumManagerServiceImpl.getAlbumById(3L);
        }, "NullPointerException was expected");

        assertAll(
                () -> assertEquals("No album found with that id: 3", thrown.getMessage()),
                () -> assertNull(thrown.getCause()));
    }

    @Test
    @DisplayName("Returns album that was posted when passed valid album object")
    public void testAlbumManagerService_insertAlbum_WhenPassedValidAlbum() {

        when(mockAlbumRepository.save(menschMaschine)).thenReturn(menschMaschine);

        Album result = albumManagerServiceImpl.insertAlbum(menschMaschine);

        assertEquals(menschMaschine, result);
    }

    @Test
    @DisplayName("Throws NullPointerException when passed an album with one or more missing require fields.")
    public void testAlbumManagerService_insertAlbum_WhenPassedNullAlbum() {
        when(mockAlbumRepository.save(invalidAlbum)).thenReturn(invalidAlbum);

        NullPointerException thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            albumManagerServiceImpl.insertAlbum(invalidAlbum);
        }, "NullPointerException was expected");

        assertAll(
                () -> assertEquals("Missing field(s) in album.", thrown.getMessage()),
                () -> assertNull(thrown.getCause()));

    }





}