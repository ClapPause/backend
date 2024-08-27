package com.clap.pause.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final StorageService storageService;

    public String saveImage(MultipartFile file) {
        return storageService.uploadImage(file);
    }

    public List<String> saveImages(List<MultipartFile> files) {
        return storageService.uploadImages(files);
    }
}
