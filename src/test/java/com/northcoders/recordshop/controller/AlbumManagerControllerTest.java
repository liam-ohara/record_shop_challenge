package com.northcoders.recordshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import com.northcoders.recordshop.service.AlbumManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest
public class AlbumManagerControllerTest {

    @Mock
    private AlbumManagerServiceImpl mockAlbumManagerServiceImpl;

    @InjectMocks
    private AlbumManagerController albumManagerController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    List<Album> albumList = new ArrayList<>();
    Artist kraftWerk = new Artist();
    Artist steeleyeSpan = new Artist();
    Publisher klingKlang = new Publisher();
    Publisher airStudios = new Publisher();
    Album menschMaschine = new Album();
    Album computerWelt = new Album();
    Album invalidAlbum = new Album();
    Album invalidAlbumDate = new Album();
    Album updatedAlbum = new Album();
    Album allAroundMyHat = new Album();

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(albumManagerController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<Album> albumList = new ArrayList<>();
        kraftWerk = Artist.builder()
                .artistId(1L)
                .name("Kraftwerk").build();

        steeleyeSpan = Artist.builder()
                .artistId(2L)
                .name("Steeleye Span").build();

        klingKlang = Publisher.builder()
                .publisherId(1L)
                .name("Kling Klang")
                .build();

        airStudios = Publisher.builder()
                .publisherId(2L)
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

        invalidAlbum = Album.builder()
                .build();

        invalidAlbumDate = Album.builder()
                .name("Electric Café")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1986, 11, 3))
                .genre(Genre.ELECTRONIC)
                .build();

        updatedAlbum = Album.builder()
                .albumId(1L)
                .name("Electric Café")
                .artist(kraftWerk)
                .publisher(klingKlang)
                .releaseDate(LocalDate.of(1986, 11, 10))
                .genre(Genre.ELECTRONIC)
                .build();

        allAroundMyHat = Album.builder()
                .albumId(3L)
                .name("All Around My Hat")
                .artist(steeleyeSpan)
                .publisher(airStudios)
                .releaseDate(LocalDate.of(1977, 10, 1))
                .genre(Genre.FOLK).build();

    }

    @Test
    @DisplayName("Returns list of all albums when GET instructions sent to /album end point")
    public void testAlbumManagerController_getAllAlbums() throws Exception {

        albumList.add(menschMaschine);
        albumList.add(computerWelt);

        when(mockAlbumManagerServiceImpl.getAllAlbums()).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album"))

         .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.ELECTRONIC.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Computerwelt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].genre").value(Genre.ELECTRONIC.toString()));

    }

    @Test
    @DisplayName("Returns JSON with album details when passed query parameter containing valid id.")
    public void testAlbumManagerController_getAlbumById_WithValidId() throws Exception {

        when(mockAlbumManagerServiceImpl.getAlbumById(menschMaschine.getAlbumId())).thenReturn(menschMaschine);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/1"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(Genre.ELECTRONIC.toString()));

    }

    @Test
    @DisplayName("Returns handled exception when passed query parameter containing invalid id.")
    public void testAlbumManagerController_getAlbumById_WithInvalidId() throws Exception {

        when(mockAlbumManagerServiceImpl.getAlbumById(2L)).thenReturn(null);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/2"))

                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").doesNotExist());
    }

    @Test
    @DisplayName("Returns JSON of new album to be posted and returns HTTP CREATED by passed valid JSON.")
    public void testAlbumManagerController_insertAlbum_WithValidJSON() throws Exception {

        when(mockAlbumManagerServiceImpl.insertAlbum(menschMaschine)).thenReturn(menschMaschine);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/album/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(menschMaschine)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(mockAlbumManagerServiceImpl, times(1)).insertAlbum(menschMaschine);
    }

    @Test
    @DisplayName("Returns 406 Not Acceptable error when invalid JSON submitted as part of POST request")
    public void testAlbumManagerController_insertAlbum_WithJSONMissingRequiredFields() throws Exception {

        when(mockAlbumManagerServiceImpl.insertAlbum(invalidAlbum)).thenReturn(invalidAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/album/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(invalidAlbum)))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").doesNotExist());

    }

    @Test
    @DisplayName("Returns 406 Not Acceptable error when JSON with invalid release date submitted as part of POST request")
    public void testAlbumManagerController_insertAlbum_WithInvalidReleaseData() throws Exception {

        when(mockAlbumManagerServiceImpl.insertAlbum(invalidAlbumDate)).thenReturn(invalidAlbumDate);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/album/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(invalidAlbumDate)))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("Returns JSON of album to be PUT and returns HTTP CREATED by passed valid JSON if album does not already exist.")
    public void testAlbumManagerController_replaceAlbum_WhenAlbumDoesNotExist() throws Exception{
        when(mockAlbumManagerServiceImpl.replaceAlbum(menschMaschine.getAlbumId(), menschMaschine)).thenReturn(menschMaschine);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/album/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(menschMaschine)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(Genre.ELECTRONIC.toString()));
    }

    @Test
    @DisplayName("Returns JSON of album to be PUT and returns HTTP CREATED by passed valid JSON if album does not already exist.")
    public void testAlbumManagerController_replaceAlbum_WhenAlbumAlreadyExists() throws Exception{

        when(mockAlbumManagerServiceImpl.getAlbumById(menschMaschine.getAlbumId())).thenReturn(menschMaschine);

        when(mockAlbumManagerServiceImpl.replaceAlbum(updatedAlbum.getAlbumId(), updatedAlbum)).thenReturn(updatedAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/album/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updatedAlbum)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Electric Café"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(Genre.ELECTRONIC.toString()));
    }

    @Test
    @DisplayName("Returns 406 Not Acceptable error when invalid JSON submitted as part of PUT request")
    public void testAlbumManagerController_replaceAlbum_WithJSONMissingRequiredFields() throws Exception {

        when(mockAlbumManagerServiceImpl.replaceAlbum(invalidAlbum.getAlbumId(), invalidAlbum)).thenReturn(invalidAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/album/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(invalidAlbum)))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").doesNotExist());

    }

    @Test
    @DisplayName("Returns 406 Not Acceptable error when JSON with invalid release date submitted as part of POST request")
    public void testAlbumManagerController_replaceAlbum_WithInvalidReleaseData() throws Exception {

        when(mockAlbumManagerServiceImpl.replaceAlbum(invalidAlbumDate.getAlbumId(), invalidAlbumDate)).thenReturn(invalidAlbumDate);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/album/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(invalidAlbumDate)))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Returns 200 OK when album requested for deletion is found and deleted successfully.")
    public void testAlbumManagerController_deleteAlbum_WhenAlbumIsFound() throws Exception {

        when(mockAlbumManagerServiceImpl.getAlbumById(menschMaschine.getAlbumId())).thenReturn(menschMaschine);

        when(mockAlbumManagerServiceImpl.deleteAlbum(menschMaschine.getAlbumId())).thenReturn(menschMaschine);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.delete("/api/v1/album/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(Genre.ELECTRONIC.toString()));

    }

    @Test
    @DisplayName("Returns 404 NOT FOUND when album requested for deletion is not found.")
    public void testAlbumManagerController_deleteAlbum_WhenAlbumIsNotFound() throws Exception {
        when(mockAlbumManagerServiceImpl.getAlbumById(menschMaschine.getAlbumId())).thenReturn(null);

        when(mockAlbumManagerServiceImpl.deleteAlbum(menschMaschine.getAlbumId())).thenReturn(null);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/album/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //Tests for updateAlbum for PATCH endpoint
    @Test
    @DisplayName("Returns 200 OK with JSON of updated album record when album with specified ID is found")
    public void testAlbumManagerController_updateAlbum_WhenAlbumIsFound() throws Exception {
        when(mockAlbumManagerServiceImpl.getAlbumById(menschMaschine.getAlbumId())).thenReturn(menschMaschine);

        when(mockAlbumManagerServiceImpl.replaceAlbum(updatedAlbum.getAlbumId(), updatedAlbum)).thenReturn(updatedAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.patch("/api/v1/album/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updatedAlbum)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Electric Café"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(Genre.ELECTRONIC.toString()));
    }

    @Test
    @DisplayName("Returns 404 NOT FOUND when no album record with specified ID is found.")
    public void testAlbumManagerController_updateAlbum_WhenAlbumIsNotFound() throws Exception {

        when(mockAlbumManagerServiceImpl.getAlbumById(menschMaschine.getAlbumId())).thenReturn(null);

        when(mockAlbumManagerServiceImpl.replaceAlbum((menschMaschine.getAlbumId()), null)).thenReturn(null);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.patch("/api/v1/album/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menschMaschine)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Returns 200 OK and list of albums when matches to artist name is found")
    public void testAlbumManagerController_getAlbumByArtistName_WhenMatchFound() throws Exception {

        albumList.add(menschMaschine);
        albumList.add(computerWelt);

        when(mockAlbumManagerServiceImpl.getAllAlbumsByArtist("Kraftwerk")).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/artist/Kraftwerk"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.ELECTRONIC.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Computerwelt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].genre").value(Genre.ELECTRONIC.toString()));
    }

    @Test
    @DisplayName("Returns 404 NOT FOUND when no albums have matches to passed artist name")
    public void testAlbumManagerController_getAlbumByArtistName_WhenNoMatchesFound() throws Exception {

        when(mockAlbumManagerServiceImpl.getAllAlbumsByArtist("Kraftwerk")).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/artist/Kraftwerk"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Returns 200 OK and a list of albums when an artist name with spaces is passed")
    public void testAlbumManagerController_getAlbumByArtistName_WhenPassedArtistNameWithSpaces() throws Exception {

        albumList.add(allAroundMyHat);
        String getRequest = "/api/v1/album/artist/Steeleye%20Span";
        URI uri = new URI(getRequest);

        when(mockAlbumManagerServiceImpl.getAllAlbumsByArtist("Steeleye Span")).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get(uri))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("All Around My Hat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.FOLK.toString()));

    }

    @Test
    @DisplayName("Returns 200 OK and a list of albums when matches to release year are found")
    public void testAlbumManagerController_getAlbumsByReleaseYear_WhenMatchFound() throws Exception {

        albumList.add(menschMaschine);

        when(mockAlbumManagerServiceImpl.getAllAlbumsByReleaseYear(1978)).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/releaseyear/1978"))

               .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.ELECTRONIC.toString()));
    }

    @Test
    @DisplayName("Returns 404 NOT FOUND when no albums have matches to passed artist name")
    public void testAlbumManagerController_getAlbumsByReleaseYear_WhenNoMatchesFound() throws Exception {

        when(mockAlbumManagerServiceImpl.getAllAlbumsByReleaseYear(1980)).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/releaseyear/1978"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Returns 200 OK and a list of albums when matches to genre are found")
    public void testAlbumManagerController_getAlbumByGenre_WhenMatchFound() throws Exception {

        albumList.add(menschMaschine);
        albumList.add(computerWelt);

        when(mockAlbumManagerServiceImpl.getAllAlbumsByGenre(Genre.ELECTRONIC)).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/genre/electronic"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.ELECTRONIC.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Computerwelt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].genre").value(Genre.ELECTRONIC.toString()));
    }

    @Test
    @DisplayName("Returns 404 NOT FOUND when no albums have matches to passed genre")
    public void testAlbumManagerController_getAlbumByGenre_WhenNoMatchesFound() throws Exception {
        when(mockAlbumManagerServiceImpl.getAllAlbumsByGenre(Genre.ELECTRONIC)).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/genre/electronic"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Returns 406 NOT ACCEPTABLE when passed an invalid genre")
    public void testAlbumManagerController_getAlbumsByGenre_WhenPassedInvalidGenre() throws Exception {

                this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/genre/teapot"))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Returns 200 OK and a list of albums when matches to album name are found")
    public void testAlbumManagerController_getAlbumsByAlbumName_WhenPassedValidAlbumName() throws Exception {

        albumList.add(computerWelt);

        when(mockAlbumManagerServiceImpl.getAlbumsByAlbumName("computerwelt")).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/albumname/computerwelt"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Computerwelt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.ELECTRONIC.toString()));

    }

    @Test
    @DisplayName("Returns 200 OK and a list of albums when an album name with spaces is passed")
    public void testAlbumManagerController_getAlbumsByAlbumName_WhenPassedAlbumNameWithSpaces() throws Exception {

        albumList.add(menschMaschine);

        when(mockAlbumManagerServiceImpl.getAlbumsByAlbumName("Die Mensch-Maschine")).thenReturn(albumList);


        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/albumname/Die%20Mensch-Maschine"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.ELECTRONIC.toString()));

    }

    @Test
    @DisplayName("Returns 404 NOT FOUND when no albums have matches to passed album name")
    public void testAlbumManagerController_getAlbumsByAlbumName_WhenNoMatchesFound() throws Exception {
        when(mockAlbumManagerServiceImpl.getAlbumsByAlbumName("Die Mensch-Maschine")).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album/albumname/Die%20Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }




}