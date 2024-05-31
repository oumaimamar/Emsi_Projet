package com.test.Projet.Controllers;

import com.test.Projet.models.Projet;
import com.test.Projet.models.ProjetDto;
import com.test.Projet.services.ProjetRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/projets")
public class ProjetsController {

    @Autowired ProjetRepository projetRepository;

    @GetMapping({" ", "/"})
    public String showProjetList(Model model){
        List<Projet> projets=projetRepository.findAll();
        model.addAttribute("projets",projets);
        return "projets/ProjetsList";
    }

    @GetMapping("/create")
    public  String showCreatePage(Model model){
        ProjetDto projetDto=new ProjetDto();
        model.addAttribute("projetDto",projetDto);
        return "projets/CreateProjet";
    }

    @PostMapping("/create")
    public String createProjet(
            @Valid @ModelAttribute ProjetDto projetDto,
            BindingResult result ){

        //Image Required
        if (projetDto.getImageFile().isEmpty()){
            result.addError(new FieldError("projetDto","imageFile","The image file is required"));
        }
        if (result.hasErrors()){
            return "projets/CreateProjet";
        }

        //Save the image file
        MultipartFile image= projetDto.getImageFile();
        Date dateProj=new Date();
        String storageFileName = dateProj.getTime()+ ".." + image.getOriginalFilename();

        try{
            String uploadDir = "public/images/";
            Path uploadPath= Paths.get(uploadDir);

            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream,Paths.get(uploadDir+storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }

        }catch (Exception ex){
            System.out.println("Exception" + ex.getMessage());
        }


        //Save Projet in Database

        Projet projet=new Projet();
        projet.setNameProj(projetDto.getNameProj());
        projet.setNameOwner(projetDto.getNameOwner());
        projet.setSecteurActivite(projetDto.getSecteurActivite());
        projet.setMontantProj(projetDto.getMontantProj());
        projet.setDescription(projetDto.getDescription());
        projet.setStatus(projetDto.getStatus());
        projet.setDateProj(dateProj);
        projet.setImageFileName(storageFileName);

        projetRepository.save(projet);

        return "redirect:/projets/";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id){

        try{
            Projet projet =projetRepository.findById(id).get();
            model.addAttribute("projet",projet);


            ProjetDto projetDto=new ProjetDto();
            projetDto.setNameProj(projet.getNameProj());
            projetDto.setNameOwner(projet.getNameOwner());
            projetDto.setSecteurActivite(projet.getSecteurActivite());
            projetDto.setMontantProj(projet.getMontantProj());
            projetDto.setDescription(projet.getDescription());
            projetDto.setStatus(projet.getStatus());

            model.addAttribute("projetDto",projetDto);

        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/projets/";
        }

        return "/projets/EditProjet";
    }


    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id,
                                @Valid @ModelAttribute ProjetDto projetDto,
                                BindingResult result){

        try{
            Projet projet=projetRepository.findById(id).get();
            model.addAttribute("projet",projet);

            if (result.hasErrors()){
                return  "projets/EditProjet";
            }

            if (!projetDto.getImageFile().isEmpty()){
                //delete old image
                String uploadDir ="public/images/";
                Path oldImagePath= Paths.get(uploadDir + projet.getImageFileName());

                try{
                    Files.delete(oldImagePath);
                }catch (Exception ex){
                    System.out.println("Exception: " + ex.getMessage());
                }

                //save new image File
                MultipartFile image= projetDto.getImageFile();
                Date dateProj=new Date();
                String storageFileName = dateProj.getTime()+ ".." + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream,Paths.get(uploadDir+storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                projet.setImageFileName(storageFileName);
            }

            //Update the other Attribute
            projet.setNameProj(projetDto.getNameProj());
            projet.setNameOwner(projetDto.getNameOwner());
            projet.setSecteurActivite(projetDto.getSecteurActivite());
            projet.setMontantProj(projetDto.getMontantProj());
            projet.setDescription(projetDto.getDescription());
            projet.setStatus(projetDto.getStatus());

            projetRepository.save(projet);
        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/projets/";
    }

    @GetMapping("/delete")
    public  String deleteProjet(@RequestParam int id){

        try{
            Projet projet=projetRepository.findById(id).get();

            //delete projet image
            Path imagePath = Paths.get("projets/images/" + projet.getImageFileName());

            try{
                Files.delete(imagePath);

            }catch (Exception ex){
                System.out.println("Exception: " + ex.getMessage());}

            //delete the projet
            projetRepository.delete(projet);


        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/projets/";
    }


}

