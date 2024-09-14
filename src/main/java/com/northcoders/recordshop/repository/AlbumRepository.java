package com.northcoders.recordshop.repository;

import com.northcoders.recordshop.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {

    public List<Album> findAlbumsByArtistArtistId (Long artistId);

    public List<Album> findAlbumsByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

}
