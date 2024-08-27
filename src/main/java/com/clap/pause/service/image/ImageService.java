package com.clap.pause.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final StorageService storageService;

    public String saveImage(MultipartFile file) {
        var image = convertToJpg(file);
        return storageService.uploadImage(image);
    }

    public List<String> saveImages(List<MultipartFile> files) {
        var images = new ArrayList<File>();
        for (var file : files) {
            images.add(convertToJpg(file));
        }
        return storageService.uploadImages(images);
    }

    private File convertToJpg(MultipartFile file) {
        try {
            var inputStream = file.getInputStream();
            var image = ImageIO.read(inputStream);

            var jpgImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            jpgImage.createGraphics()
                    .drawImage(image, 0, 0, null);

            var fileName = UUID.randomUUID() + ".jpg";
            var convertedImage = new File(fileName);
            ImageIO.write(jpgImage, "jpg", convertedImage);

            return convertedImage;
        } catch (IOException exception) {
            return null;
        }
    }
}
