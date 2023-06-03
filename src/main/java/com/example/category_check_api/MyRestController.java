package com.example.category_check_api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class MyRestController {
    //create function for reading file
    @Autowired
    ResourceLoader resourceLoader;
    private List<String> readFile(){

        List<String> categories = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:categories.txt");
            InputStream inputStream = resource.getInputStream();
            byte[] fileBytes = FileCopyUtils.copyToByteArray(inputStream);
            String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
            String[] lines = fileContent.split("\\r?\\n");
            for (String line : lines) {
                categories.add(line.toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  categories;
    }

    // get for all categories in the file

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllNames(){
        List<String> categories = readFile();
        if(!categories.isEmpty()){
            return ResponseEntity.ok(categories);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }



    //get to check if name is valid - return true/false
    @GetMapping("/check/{cat_name}")
    public ResponseEntity<Boolean> checkName(@PathVariable String cat_name){
        List<String> categories = readFile();
        if(categories.contains(cat_name.toLowerCase())){
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.ok(false);
        }
    }
}
