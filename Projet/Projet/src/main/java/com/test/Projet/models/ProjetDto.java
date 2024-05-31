package com.test.Projet.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class ProjetDto {
    @NotEmpty(message = "the name is required")
    private String nameProj;
    @NotEmpty(message = "the nameOwner is required")
    private String nameOwner;
    @NotEmpty(message = "the secteurActivite is required")
    private String secteurActivite;
    @Min(1000)
    private double montantProj;

@Size(min = 5,message = "the description should be at least 5 characters")
@Size(max = 200,message = "the description cannot exceed 200 characters")
private String description;

    @NotEmpty(message = "the Status is required")
    private String Status;
    private MultipartFile imageFile;
}
