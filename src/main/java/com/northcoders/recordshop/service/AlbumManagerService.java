package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;

import java.util.List;

public interface AlbumManagerService {

    List<Album> getAllAlbums();

    Album getAlbumById(Long id);

    List<Album> getAllAlbumsByArtist(String artistName);

    List<Album> getAllAlbumsByReleaseYear(Long year);

    List<Album> getAllAlbumsByGenre(Genre genre);

    Album getAlbumByAlbumName(String albumName);

    void insertAlbum(Album album);

    void updateAlbum(Long id, Album album);

    void deleteAlbum(Long id);
}
