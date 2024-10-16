package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import com.clap.pause.exception.ImageProcessingFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final StorageService storageService;
    private final ImageProperties imageProperties;

    public String saveImage(MultipartFile file) {
        var image = convertMultipartFileToFile(file);
        return storageService.uploadImage(image);
    }

    public List<String> saveImages(List<MultipartFile> files) {
        var imageFiles = convertMultipartFilesToFiles(files);
        return storageService.uploadImages(imageFiles);
    }

    public byte[] getImage(String image) {
        return storageService.getImage(image);
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        try {
            var inputStream = file.getInputStream();
            var image = ImageIO.read(inputStream);

            var jpgImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            jpgImage.createGraphics()
                    .drawImage(image, 0, 0, null);

            var fileName = UUID.randomUUID() + ".jpg";
            var filePath = Paths.get(imageProperties.uploadDir(), fileName)
                    .toString();

            var convertedImage = new File(filePath);
            ImageIO.write(jpgImage, "jpg", convertedImage);

            return convertedImage;
        } catch (IOException exception) {
            throw new ImageProcessingFailedException("jpg/png/jpeg 형식의 이미지를 업로드 해주세요.");
        }
    }

    private List<File> convertMultipartFilesToFiles(List<MultipartFile> files) {
        return files.stream()
                .map(this::convertMultipartFileToFile)
                .toList();
    }
}
