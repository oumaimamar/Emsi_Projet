package com.test.Projet.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity @Data
@Table(name = "projets")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameProj;
    private String nameOwner;
    private String secteurActivite;
    private double montantProj;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String Status;
    private Date dateProj;
    private String imageFileName;
}
