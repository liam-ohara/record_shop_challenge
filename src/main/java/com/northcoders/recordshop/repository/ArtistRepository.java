package com.northcoders.recordshop.repository;

import com.northcoders.recordshop.model.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends CrudRepository <Artist, Long> {

    public Artist findArtistByName(String name);
}
