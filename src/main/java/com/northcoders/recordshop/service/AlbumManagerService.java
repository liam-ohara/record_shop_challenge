package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;

import java.util.List;

public interface AlbumManagerService {

    List<Album> getAllAlbums();

    Album getAlbumById(Long id);

    List<Album> getAllAlbumsByArtist(String artistName);

    List<Album> getAllAlbumsByReleaseYear(int year);

    List<Album> getAllAlbumsByGenre(Genre genre);

    List<Album> getAlbumsByAlbumName(String albumName);

    Album insertAlbum(Album album);

    Album replaceAlbum(Long id, Album album);

    Album deleteAlbum(Long id);
}
