package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import com.clap.pause.exception.ImageProcessingFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalStorageService implements StorageService {

    private final ImageProperties imageProperties;

    public String uploadImage(File file) {
        try {
            var fileName = file.getName();
            var path = Paths.get(imageProperties.uploadDir(), fileName);
            Files.copy(file.toPath(), path);
            return "/images/" + fileName;
        } catch (NullPointerException | IOException exception) {
            throw new ImageProcessingFailedException("이미지를 저장할 수 없습니다.");
        }
    }

    public List<String> uploadImages(List<File> files) {
        var images = new ArrayList<String>();
        for (var file : files) {
            var image = uploadImage(file);
            images.add(image);
        }
        return images;
    }

    public byte[] getImage(String image) {
        try {
            var path = Paths.get("src/main/resources/static/images/" + image);
            return Files.readAllBytes(path);
        } catch (IOException exception) {
            throw new ImageProcessingFailedException("존재하지 않는 파일입니다.");
        }
    }
}
