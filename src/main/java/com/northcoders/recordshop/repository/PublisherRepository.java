package com.northcoders.recordshop.repository;

import com.northcoders.recordshop.model.Publisher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends CrudRepository <Publisher, Long> {
}
