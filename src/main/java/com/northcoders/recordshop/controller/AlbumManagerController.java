package com.northcoders.recordshop.controller;


import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.service.AlbumManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/album")
public class AlbumManagerController {

    @Autowired
    AlbumManagerService albumManagerService;

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albumList = albumManagerService.getAllAlbums();
        return new ResponseEntity<>(albumList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
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

    @GetMapping("/artist/{artistName}")
    public ResponseEntity<List<Album>> getAlbumsByArtistName (@PathVariable ("artistName") String artistName) {
        List<Album> albumList = new ArrayList<>();
        try {
            albumList = albumManagerService.getAllAlbumsByArtist(artistName);
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

        try {
            albumList = albumManagerService.getAllAlbumsByGenre(genre);
            if (albumList.isEmpty()) {
                throw new RuntimeException();
            }
            return new ResponseEntity<>(albumList, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(albumList, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
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

    @PutMapping("/{id}")
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
    @PatchMapping("/{id}")
    public ResponseEntity<Album> updateAlbum (@PathVariable("id") Long id, @RequestBody Album updatedAlbum) {
        Album checkedAlbum = new Album();

        try {
            if (albumManagerService.getAlbumById(id) == null) {
                throw new NullPointerException();
            }
       } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        albumManagerService.replaceAlbum(id, updatedAlbum);
        return new ResponseEntity<>(updatedAlbum, HttpStatus.OK);

    }

        @DeleteMapping("/{id}")

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

