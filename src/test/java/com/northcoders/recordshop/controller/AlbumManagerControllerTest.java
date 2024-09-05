package com.northcoders.recordshop.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class AlbumManagerControllerTest {

    @Mock
    private AlbumManagerServiceImpl mockAlbumManagerServiceImpl;

    @InjectMocks
    private AlbumManagerController albumManagerController;

    @Autowired
    private MockMvc mockMvcController;

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(albumManagerController).build();
    }

    @Test
    @DisplayName("Returns list of all albums when GET instructions sent to /album end point")
    public void testAlbumManagerController_getAllAlbums() throws Exception {
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

        when(mockAlbumManagerServiceImpl.getAllAlbums()).thenReturn(albumList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album"))

         .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Die Mensch-Maschine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Genre.ELECTRONIC.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Computerwelt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].genre").value(Genre.ELECTRONIC.toString()));

    }
}