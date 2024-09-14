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

import java.time.LocalDate;
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
        List<Album> listOfMatchingAlbums = new ArrayList<>();
        Artist artistSearchedFor = new Artist();

        try {
            if (artistRepository.findArtistByNameIgnoreCase(artistName) != null) {
                artistSearchedFor = artistRepository.findArtistByNameIgnoreCase(artistName);
            }
        } catch (NullPointerException e) {
            return listOfMatchingAlbums;
        }

        try {
            if (albumRepository.findAlbumsByArtistArtistId(artistSearchedFor.getArtistId()) != null) {
                listOfMatchingAlbums = albumRepository.findAlbumsByArtistArtistId(artistSearchedFor.getArtistId());
                return listOfMatchingAlbums;
            }
        } catch (NullPointerException e) {
           listOfMatchingAlbums = null;
        }
        return listOfMatchingAlbums;
    }

    @Override
    public List<Album> getAllAlbumsByReleaseYear(int year) {
        List<Album> listOfMatchingAlbums;
        LocalDate startDate = LocalDate.of(year, 1, 1);;
        LocalDate endDate = LocalDate.of(year + 1, 1 , 1);

        listOfMatchingAlbums = albumRepository.findAlbumsByReleaseDateBetween(startDate, endDate);
        return listOfMatchingAlbums;

    }

    @Override
    public List<Album> getAllAlbumsByGenre(String genre) {
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
//Refactor to include some of logical from above - as incoming album does not contain
    // Artist or Publisher ID.
    @Override
    public Album replaceAlbum(Long id, Album replacingAlbum) {
        Album checkedAlbum = new Album();
        if (albumRepository.findById(id).isPresent()) {
            checkedAlbum = albumRepository.findById(id).get();
        }
        if (id.equals(checkedAlbum.getAlbumId())) {
            replacingAlbum.setAlbumId(checkedAlbum.getAlbumId());

            try {
                if (replacingAlbum.getName() == null || replacingAlbum.getName().isBlank()) {
                    replacingAlbum.setName(checkedAlbum.getName());
                }
            } catch (NullPointerException e) {
                replacingAlbum.setName(checkedAlbum.getName());
            }

            try {
                if (replacingAlbum.getArtist().getName() == null || replacingAlbum.getArtist().getName().isBlank()) {
                    replacingAlbum.setArtist(checkedAlbum.getArtist());
                }
            } catch (NullPointerException e) {
                replacingAlbum.setArtist(checkedAlbum.getArtist());
            }

            try {
                if (replacingAlbum.getPublisher().getName() == null || replacingAlbum.getPublisher().getName().isBlank()) {
                    replacingAlbum.setPublisher(checkedAlbum.getPublisher());
                }
            } catch (NullPointerException e) {
                replacingAlbum.setPublisher(checkedAlbum.getPublisher());
            }

            try {
                if (replacingAlbum.getReleaseDate() == null) {
                    replacingAlbum.setReleaseDate(checkedAlbum.getReleaseDate());
                }
            } catch (NullPointerException e) {
                replacingAlbum.setReleaseDate(checkedAlbum.getReleaseDate());
            }

            try {
                if (replacingAlbum.getGenre() == null) {
                    replacingAlbum.setGenre(checkedAlbum.getGenre());
                }
            } catch (NullPointerException e) {
                replacingAlbum.setGenre(checkedAlbum.getGenre());
            }

            List<Artist> artistList = new ArrayList<>();
            artistRepository.findAll().forEach(artistList::add);
            List<Publisher> publisherList = new ArrayList<>();
            publisherRepository.findAll().forEach(publisherList::add);

            if (replacingAlbum.getArtist().getArtistId() == null) {
                for (int i = 0; i < artistList.size(); i++) {
                    if (artistList.get(i).getName().equals(replacingAlbum.getArtist().getName())) {
                        replacingAlbum.setArtist(artistList.get(i));
                    }
                }
            } else {
                artistRepository.save(replacingAlbum.getArtist());
            }

            if (replacingAlbum.getPublisher().getPublisherId() == null) {
                for (int j = 0; j < artistList.size(); j++) {
                    if (publisherList.get(j).getName().equals(replacingAlbum.getPublisher().getName())) {
                        replacingAlbum.setPublisher(publisherList.get(j));
                    }
                }
            } else {
                publisherRepository.save(replacingAlbum.getPublisher());
            }
            albumRepository.save(replacingAlbum);
        } else {
            artistRepository.save(replacingAlbum.getArtist());
            publisherRepository.save(replacingAlbum.getPublisher());
            albumRepository.save(replacingAlbum);
        }
        return replacingAlbum;
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
