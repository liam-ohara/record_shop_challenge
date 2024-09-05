package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumManagerServiceImpl implements AlbumManagerService {

    @Autowired
    AlbumRepository albumRepository;

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<>();
        albumRepository.findAll().forEach(albumList::add);
        return albumList;

    }

    @Override
    public Album getAlbumById(Long id) {
        return null;
    }

    @Override
    public List<Album> getAllAlbumsByArtist(String artistName) {
        return null;
    }

    @Override
    public List<Album> getAllAlbumsByReleaseYear(Long year) {
        return null;
    }

    @Override
    public List<Album> getAllAlbumsByGenre(Genre genre) {
        return null;
    }

    @Override
    public Album getAlbumByAlbumName(String albumName) {
        return null;
    }

    @Override
    public void insertAlbum(Album album) {

    }

    @Override
    public void updateAlbum(Long id, Album album) {

    }

    @Override
    public void deleteAlbum(Long id) {

    }
}
