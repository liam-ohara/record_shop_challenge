package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import com.northcoders.recordshop.repository.AlbumRepository;
import com.northcoders.recordshop.repository.ArtistRepository;
import com.northcoders.recordshop.repository.PublisherRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class AlbumManagerServiceImplTest {

    @Mock
    private AlbumRepository mockAlbumRepository;

    @Mock
    private ArtistRepository mockArtistRepository;

    @Mock
    private PublisherRepository mockPublisherRepository;

    @InjectMocks
    private AlbumManagerServiceImpl albumManagerServiceImpl;

    List<Album> albumList = new ArrayList<>();
    List<Artist> artistList = new ArrayList<>();
    List<Publisher> publisherList = new ArrayList<>();
    Artist kraftWerk = new Artist();
    Publisher klingKlang = new Publisher();
    Artist kraftWerkDuplicate = new Artist();
    Publisher klingKlangDuplicate = new Publisher();
    Artist steeleyeSpan = new Artist();
    Publisher airStudios = new Publisher();
    Album menschMaschine = new Album();
    Album computerWelt = new Album();
    Album invalidAlbum = new Album();
    Album invalidAlbumDate = new Album();
    Album updatedAlbum = new Album();
    Album menschMaschineDuplicate = new Album();
    Album allAroundMyHat = new Album();

    @BeforeEach
    public void setup() {

        kraftWerk = Artist.builder()
                .artistId(1L)
                .name("Kraftwerk").build();

        kraftWerkDuplicate = Artist.builder()
                .name("Kraftwerk").build();

        steeleyeSpan = Artist.builder()
                .name("Steeleye Span").build();

        klingKlang = Publisher.builder()
                .publisherId(1L)
                .name("Kling Klang")
                .build();

        klingKlangDuplicate = Publisher.builder()
                .name("Kling Klang")
                .build();

        airStudios = Publisher.builder()
                .name("Air Studios").build();

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

        updatedAlbum = Album.builder()
//                .albumId(1L)
                .name("Electric Café")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1986, 11, 10))
                .genre(Genre.ELECTRONIC)
                .build();

        menschMaschineDuplicate = Album.builder()
                .name("Die Mensch-Maschine")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1978, 4, 28))
                .genre(Genre.ELECTRONIC)
                .build();

        allAroundMyHat = Album.builder()
                .name("All Around My Hat")
                .artist(steeleyeSpan)
                .publisher(airStudios)
                .releaseDate(LocalDate.of(1977, 10, 1))
                .genre(Genre.FOLK)
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
    @DisplayName("")
    public void testAlbumManagerService_insertAlbum_WhenPassedDuplicateAlbum() {


        when(mockAlbumRepository.save(menschMaschine)).thenReturn(menschMaschine);
        albumManagerServiceImpl.insertAlbum(menschMaschine);
        when(mockAlbumRepository.save(menschMaschineDuplicate)).thenReturn(menschMaschineDuplicate);

        Album result = albumManagerServiceImpl.insertAlbum(menschMaschineDuplicate);

        assertAll(
                () -> assertEquals(1L, result.getArtist().getArtistId()),
                () -> assertEquals(1L, result.getPublisher().getPublisherId()));

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

    @Test
    @DisplayName("Returns album that was posted when passed valid album object")
    public void testAlbumManagerService_replaceAlbum_WhenAlbumDoesNotExist() {

        when(mockAlbumRepository.save(menschMaschine)).thenReturn(menschMaschine);

        Album result = albumManagerServiceImpl.replaceAlbum(menschMaschine.getAlbumId(), menschMaschine);

        assertEquals(menschMaschine, result);
    }


    @Test
    @DisplayName("Returns album that was posted when passed valid album object")
    public void testAlbumManagerService_replaceAlbum_WhenAlbumExists() {

        when(mockAlbumRepository.findById(menschMaschine.getAlbumId())).thenReturn(Optional.ofNullable(menschMaschine));
        albumManagerServiceImpl.insertAlbum(menschMaschine);

        Album result = albumManagerServiceImpl.replaceAlbum(1L, updatedAlbum);

        assertEquals(updatedAlbum, result);
    }

    @Test
    @DisplayName("Returns album that was deleted when passed a valid id")
    public void testAlbumManagerService_deleteAlbum_WhenAlbumExists() {

        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.ofNullable(menschMaschine));

        albumManagerServiceImpl.deleteAlbum(1L);

        Album result = albumManagerServiceImpl.deleteAlbum(1L);

        verify(mockAlbumRepository, times(2)).findById(1L);
        verify(mockAlbumRepository, times(2)).deleteById(1L);

        assertEquals(menschMaschine, result);

    }

    @Test
    @DisplayName("Returns null when passed an invalid id")
    public void testAlbumManagerService_deleteAlbum_WhenAlbumDoesNotExist() {

        when(mockAlbumRepository.findById(2L)).thenReturn(null);

        NullPointerException thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            albumManagerServiceImpl.getAlbumById(2L);
        }, "NullPointerException was expected");

        albumManagerServiceImpl.deleteAlbum(2L);

        Album result = albumManagerServiceImpl.deleteAlbum(2L);

        verify(mockAlbumRepository, times(3)).findById(2L);
        verify(mockAlbumRepository, times(0)).deleteById(2L);

        assertNull(result);
    }

    @Test
    @DisplayName("Returns list of albums recorded by artist passed in when matches found.")
    public void testAlbumManagerService_getAllAlbumsByArtist_WhenPassedArtistNameMatchingAlbums() {

        artistList.add(kraftWerk);
        albumList.add(menschMaschine);
        albumList.add(computerWelt);

        when(mockArtistRepository.findArtistByNameIgnoreCase("Kraftwerk")).thenReturn(kraftWerk);
        when(mockAlbumRepository.findAlbumsByArtistArtistId(kraftWerk.getArtistId())).thenReturn(albumList);

        List<Album> actualResults = albumManagerServiceImpl.getAllAlbumsByArtist("Kraftwerk");

        assertEquals(albumList, actualResults);

    }

    @Test
    @DisplayName("Returns empty list of albums when no album matches for artist found")
    public void testAlbumManagerService_getAllAlbumsByArtist_WhenPassedArtistNameMatchingNoAlbums() {
        when(mockArtistRepository.findArtistByNameIgnoreCase("Unknown artist")).thenReturn(null);
        when(mockAlbumRepository.findAlbumsByArtistArtistId(null)).thenReturn(albumList);

        List<Album> actualResults = albumManagerServiceImpl.getAllAlbumsByArtist("Unknown artist");

        assertEquals(albumList, actualResults);

    }

    @Test
    @DisplayName("Returns list of albums released by year passed in when matches found.")
    public void testAlbumManagerService_getAllAlbumsByReleaseYear_WhenPassedYearMatchingAlbums() {

        albumList.add(menschMaschine);

        when(mockAlbumRepository.findAlbumsByReleaseDateBetween(LocalDate.of(1978, 1, 1), LocalDate.of(1979, 1, 1))).thenReturn(albumList);

        List<Album> actualResults = albumManagerServiceImpl.getAllAlbumsByReleaseYear(1978);

        assertEquals(albumList, actualResults);

    }

    @Test
    @DisplayName("Returns empty list of albums when no matches found for release year.")
    public void testAlbumManagerService_getAllAlbumsByReleaseYear_WhenPassedYearMatchesNoAlbums() {

        when(mockAlbumRepository.findAlbumsByReleaseDateBetween(LocalDate.of(1979, 1, 1), LocalDate.of(1980, 1, 1))).thenReturn(albumList);

        List<Album> actualResults = albumManagerServiceImpl.getAllAlbumsByReleaseYear(1979);

        assertEquals(albumList, actualResults);

    }

    @Test
    @DisplayName("Returns list of albums by genre passed in when matches found.")
    public void testAlbumManagerService_getAllAlbumsByGenre_WhenPassedGenreMatchingAlbums() {
        albumList.add(menschMaschine);
        albumList.add(computerWelt);

        when(mockAlbumRepository.findAlbumsByGenre(Genre.ELECTRONIC)).thenReturn(albumList);

        List<Album> actualResults = albumManagerServiceImpl.getAllAlbumsByGenre("electronic");

        assertEquals(albumList, actualResults);

    }

}