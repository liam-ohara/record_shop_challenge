package com.northcoders.recordshop.controller;


import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.service.AlbumManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        albumManagerService.insertAlbum(album);
        return new ResponseEntity<>(album, HttpStatus.CREATED);
    }



}
