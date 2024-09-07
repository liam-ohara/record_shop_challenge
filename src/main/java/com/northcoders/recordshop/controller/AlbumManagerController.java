package com.northcoders.recordshop.controller;


import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.service.AlbumManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

import java.time.LocalDate;
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
    public ResponseEntity<Album> updateAlbum (@PathVariable("id") Long id, @RequestBody Album updatedAlbum) {
        try {
            if (updatedAlbum.getName() == null || updatedAlbum.getArtist() == null || updatedAlbum.getPublisher() == null || updatedAlbum.getReleaseDate() == null || updatedAlbum.getGenre() == null) {
                throw new HttpMediaTypeNotAcceptableException("Malformed JSON.");
            } else if (updatedAlbum.getReleaseDate().isAfter(LocalDate.now())) {
                throw new HttpMediaTypeNotAcceptableException("Release date cannot be in the future.");
            } else {
                Album checkedAlbum = albumManagerService.getAlbumById(id);
                if (checkedAlbum != null) {
                    Album putAlbum = albumManagerService.updateAlbum(id, updatedAlbum);
                    return new ResponseEntity<>(putAlbum, HttpStatus.OK);
                } else {
                    Album putAlbum = albumManagerService.updateAlbum(id, updatedAlbum);
                    return new ResponseEntity<>(putAlbum, HttpStatus.CREATED);
                }
            }
        } catch(HttpMediaTypeNotAcceptableException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum (@PathVariable("id") Long id) {
        Album albumForDeletion = albumManagerService.getAlbumById(id);
        albumManagerService.deleteAlbum(id);
        return new ResponseEntity<>(albumForDeletion, HttpStatus.OK);
    }

}

