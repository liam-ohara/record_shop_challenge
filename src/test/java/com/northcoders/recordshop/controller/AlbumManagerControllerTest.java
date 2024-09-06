package com.northcoders.recordshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import com.northcoders.recordshop.service.AlbumManagerServiceImpl;
import org.junit.jupiter.api.BeforeAll;
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
    Publisher klingKlang = new Publisher();
    Album menschMaschine = new Album();
    Album computerWelt = new Album();
    Album invalidAlbum = new Album();

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(albumManagerController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<Album> albumList = new ArrayList<>();
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

        invalidAlbum = Album.builder()
                .build();
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

}