package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final ImageProperties imageProperties;

    public String uploadImage(MultipartFile file) {

        var image = "";
        return image;
    }

    public List<String> uploadImages(List<MultipartFile> files) {
        var images = new ArrayList<String>();
        return images;
    }
}
