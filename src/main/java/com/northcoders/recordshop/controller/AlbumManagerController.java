package com.northcoders.recordshop.controller;


import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.service.AlbumManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AlbumManagerController {

    @Autowired
    AlbumManagerService albumManagerService;

    @GetMapping("/album")
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albumList = albumManagerService.getAllAlbums();
        return new ResponseEntity<>(albumList, HttpStatus.OK);
    }

    @GetMapping("/album/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        Album album;
        try {
            album = albumManagerService.getAlbumById(id);
            if (album == null) {
                throw new RuntimeException();
            }
            return new ResponseEntity<>(album, HttpStatus.OK);
        } catch (RuntimeException e) {
           return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/album/artist/{artistName}")
    public ResponseEntity<List<Album>> getAlbumsByArtistName (@PathVariable ("artistName") String artistName) {
        List<Album> albumList = new ArrayList<>();
        String decodedURI = artistName.replaceAll("%20", " ");

        try {
            albumList = albumManagerService.getAllAlbumsByArtist(decodedURI);
            if (albumList.isEmpty()) {
                throw new RuntimeException();
            }
            return new ResponseEntity<>(albumList, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(albumList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/releaseyear/{year}")
    public ResponseEntity<List<Album>> getAlbumsByReleaseYear(@PathVariable ("year") int year) {
        List<Album> albumList = new ArrayList<>();
        try {
            albumList = albumManagerService.getAllAlbumsByReleaseYear(year);
            if (albumList.isEmpty()) {
                throw new RuntimeException();
            }
            return new ResponseEntity<>(albumList, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(albumList, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Album>> getAlbumsByGenre(@PathVariable ("genre") String genre) {
        List<Album> albumList = new ArrayList<>();
        Genre genreToSearchBy = null;
        ArrayList<String> listOfGenres = new ArrayList<>();

        for (Genre g : Genre.values()) {
            listOfGenres.add(g.toString());
        }

        try {
            for (int i = 0; i < listOfGenres.size(); i++) {
                if (genre.toUpperCase().equals(listOfGenres.get(i))) {
                    genreToSearchBy = Genre.valueOf(genre.toUpperCase());
                }
            }
                if (genreToSearchBy == null) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                    return new ResponseEntity<>(albumList, HttpStatus.NOT_ACCEPTABLE);
                }

        try {
            albumList = albumManagerService.getAllAlbumsByGenre(genreToSearchBy);
            if (albumList.isEmpty()) {
                throw new RuntimeException();
            }
            return new ResponseEntity<>(albumList, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(albumList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/album/albumname/{name}")
    public ResponseEntity<List<Album>> getAlbumsByAlbumName(@PathVariable ("name") String albumName) {
        List<Album> albumList = new ArrayList<>();
        String decodedURI = albumName.replaceAll("%20", " ");

        try {
            albumList = albumManagerService.getAlbumsByAlbumName(decodedURI);

            if (albumList.isEmpty()) {
                throw new RuntimeException();

            }
            return new ResponseEntity<>(albumList, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(albumList, HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping("/album")
    public ResponseEntity<Album> insertAlbum(@RequestBody Album album) {
        try {
            albumManagerService.insertAlbum(album);
           if (album.getName() == null || album.getArtist() == null || album.getPublisher() == null || album.getReleaseDate() == null || album.getGenre() == null) {
               throw new HttpMediaTypeNotAcceptableException("Malformed JSON.");
           } else if (album.getReleaseDate().isAfter(LocalDate.now())) {
               throw new HttpMediaTypeNotAcceptableException("Release date cannot be in the future.");
                          }
           else {
               return new ResponseEntity<>(album, HttpStatus.CREATED);
           }
        } catch (HttpMediaTypeNotAcceptableException e) {
            return  new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/album/{id}")
    public ResponseEntity<Album> replaceAlbum (@PathVariable("id") Long id, @RequestBody Album replacingAlbum) {
        try {
            if (replacingAlbum.getName() == null || replacingAlbum.getArtist() == null || replacingAlbum.getPublisher() == null || replacingAlbum.getReleaseDate() == null || replacingAlbum.getGenre() == null) {
                throw new HttpMediaTypeNotAcceptableException("Malformed JSON.");
            } else if (replacingAlbum.getReleaseDate().isAfter(LocalDate.now())) {
                throw new HttpMediaTypeNotAcceptableException("Release date cannot be in the future.");
            } else {
                Album checkedAlbum = albumManagerService.getAlbumById(id);
                if (checkedAlbum != null) {
                    Album putAlbum = albumManagerService.replaceAlbum(id, replacingAlbum);
                    return new ResponseEntity<>(putAlbum, HttpStatus.OK);
                } else {
                    Album putAlbum = albumManagerService.replaceAlbum(id, replacingAlbum);
                    return new ResponseEntity<>(putAlbum, HttpStatus.CREATED);
                }
            }
        } catch(HttpMediaTypeNotAcceptableException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }

        }
    @PatchMapping("/album/{id}")
    public ResponseEntity<Album> updateAlbum (@PathVariable("id") Long id, @RequestBody Album updatedAlbum) {
        try {
            if (albumManagerService.getAlbumById(id) == null) {
                throw new NullPointerException();
            }
       } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

       updatedAlbum.setAlbumId(id);

        albumManagerService.replaceAlbum(id, updatedAlbum);
        return new ResponseEntity<>(updatedAlbum, HttpStatus.OK);

    }

        @DeleteMapping("/album/{id}")

    public ResponseEntity<Album> deleteAlbum (@PathVariable("id") Long id) {
        Album albumForDeletion;
        try {
            albumForDeletion = albumManagerService.getAlbumById(id);
            if (albumForDeletion == null) {
                throw new RuntimeException();
            }
            albumManagerService.deleteAlbum(id);
            return new ResponseEntity<>(albumForDeletion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

