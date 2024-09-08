package com.northcoders.recordshop.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Publisher {

    @Id
    @GeneratedValue
    @Column (name = "publisher_id")
    Long publisherId;

    @Column
    String name;


}
