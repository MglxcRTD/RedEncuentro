package com.redencuentro.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String subirImagen(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
        return (String) uploadResult.get("secure_url");
    }
}
