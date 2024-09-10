package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Artist;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.model.Publisher;
import com.northcoders.recordshop.repository.AlbumRepository;
import com.northcoders.recordshop.repository.ArtistRepository;
import com.northcoders.recordshop.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumManagerServiceImpl implements AlbumManagerService {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    PublisherRepository publisherRepository;

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

        if (this.albumRepository.findById(1L).isPresent()) {
            List<Album> listOfAlbums = new ArrayList<>();
            albumRepository.findAll().forEach(listOfAlbums::add);
            Artist artistOfAlbumToPost = album.getArtist();

            for (int i = 0; i < listOfAlbums.size(); i++) {
                if (listOfAlbums.get(i).getArtist().getName().equals(artistOfAlbumToPost.getName())) {
                    artistOfAlbumToPost.setArtistId(listOfAlbums.get(i).getArtist().getArtistId());
                    album.setArtist(artistOfAlbumToPost);
                }
            }
        }

        if (this.albumRepository.findById(1L).isPresent()) {
            List<Album> listOfAlbums = new ArrayList<>();
            albumRepository.findAll().forEach(listOfAlbums::add);
            Publisher publisherOfAlbumToPost = album.getPublisher();;


            for (int j = 0; j < listOfAlbums.size(); j++) {
                if (listOfAlbums.get(j).getPublisher().getName().equals(publisherOfAlbumToPost.getName())) {
                    publisherOfAlbumToPost.setPublisherId(listOfAlbums.get(j).getPublisher().getPublisherId());
                    album.setPublisher(publisherOfAlbumToPost);
                }
            }
        }

        artistRepository.save(album.getArtist());
        publisherRepository.save(album.getPublisher());
        albumRepository.save(album);
        return album;
    }

    @Override
    public Album updateAlbum(Long id, Album album) {
        Album currentAlbum = new Album();
        if (albumRepository.findById(id).isPresent()) {
            currentAlbum = albumRepository.findById(id).get();
        }
        if (id.equals(currentAlbum.getAlbumId())) {
            albumRepository.deleteById(id);
            albumRepository.save(album);
        } else {
            albumRepository.save(album);
        }
        return album;
    }

    @Override
    public Album deleteAlbum(Long id) throws NullPointerException {
        Album albumForDeletion = new Album();

        try {
           albumForDeletion = albumRepository.findById(id).get();
            if (albumForDeletion == null) {
                throw new NullPointerException("No album found with that id: " + id);
            }
            if (albumForDeletion.getAlbumId().equals(id)) {
                albumRepository.deleteById(id);
                return albumForDeletion;
            }
        } catch (NullPointerException e) {
            albumForDeletion = null;
        }

        return albumForDeletion;
    }
}
