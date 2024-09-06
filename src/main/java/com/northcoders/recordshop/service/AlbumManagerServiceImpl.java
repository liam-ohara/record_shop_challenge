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
    public Album getAlbumById(Long id) throws NullPointerException {
        if (albumRepository.findById(id).isPresent()) {
            return albumRepository.findById(id).get();
    }
        throw new NullPointerException("No album found with that id: " + id);
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
    public Album insertAlbum(Album album) {
        if (album.getName() == null || album.getArtist() == null || album.getPublisher() == null || album.getReleaseDate() == null || album.getGenre() == null) {
            throw new NullPointerException("Missing field(s) in album.");
        }
        albumRepository.save(album);
        return album;
    }

    @Override
    public Album updateAlbum(Long id, Album album) {
        albumRepository.save(album);
        return album;
    }

    @Override
    public void deleteAlbum(Long id) {

    }
}
