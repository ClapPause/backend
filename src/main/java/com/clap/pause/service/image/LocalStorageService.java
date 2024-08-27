package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalStorageService implements StorageService {

    final ImageProperties imageProperties;

    public String uploadImage(File file) {
        var image = "";
        return image;
    }

    public List<String> uploadImages(List<File> files) {
        var images = new ArrayList<String>();
        return images;
    }
}
